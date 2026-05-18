package com.grama.angana.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.grama.angana.data.local.BookingDao
import com.grama.angana.data.local.BookingEntity
import com.grama.angana.domain.model.Booking
import com.grama.angana.domain.model.User
import com.grama.angana.domain.model.UserRole
import com.grama.angana.domain.repository.AuthRepository
import com.grama.angana.domain.repository.BookingRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private object Collections {
    const val USERS = "users"
    const val BOOKINGS = "bookings"
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { instance ->
            val current = instance.currentUser
            if (current == null) {
                trySend(null)
            } else {
                firestore.collection(Collections.USERS).document(current.uid).get()
                    .addOnSuccessListener { doc ->
                        trySend(
                            User(
                                id = current.uid,
                                name = doc.getString("name").orEmpty(),
                                email = current.email.orEmpty(),
                                phone = doc.getString("phone").orEmpty(),
                                role = if (doc.getString("role") == "PANCHAYAT_ADMIN") UserRole.PANCHAYAT_ADMIN else UserRole.PUBLIC_USER
                            )
                        )
                    }
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun registerWithEmail(name: String, email: String, phone: String, password: String): Result<Unit> = runCatching {
        val credential = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = credential.user?.uid ?: error("Failed to create user")
        firestore.collection(Collections.USERS).document(uid)
            .set(mapOf("name" to name, "email" to email, "phone" to phone, "role" to "PUBLIC_USER"))
            .await()
    }

    override suspend fun logout() = auth.signOut()
}

class BookingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val bookingDao: BookingDao
) : BookingRepository {
    override fun observeBookings(): Flow<List<Booking>> = callbackFlow {
        val listener = firestore.collection(Collections.BOOKINGS).addSnapshotListener { value, _ ->
            val bookings = value?.documents.orEmpty().map { doc ->
                Booking(
                    id = doc.id,
                    userId = doc.getString("userId").orEmpty(),
                    name = doc.getString("name").orEmpty(),
                    purpose = doc.getString("purpose").orEmpty(),
                    date = doc.getString("date").orEmpty(),
                    timeSlot = doc.getString("timeSlot").orEmpty(),
                    contactNumber = doc.getString("contactNumber").orEmpty(),
                    status = doc.getString("status").orEmpty()
                )
            }
            trySend(bookings)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun submitBooking(booking: Booking): Result<Unit> = runCatching {
        val duplicate = bookingDao.countApprovedBySlot(booking.date, booking.timeSlot) > 0
        check(!duplicate) { "Time slot already booked." }

        firestore.collection(Collections.BOOKINGS).add(
            mapOf(
                "userId" to booking.userId,
                "name" to booking.name,
                "purpose" to booking.purpose,
                "date" to booking.date,
                "timeSlot" to booking.timeSlot,
                "contactNumber" to booking.contactNumber,
                "status" to "PENDING"
            )
        ).await()
        bookingDao.upsertAll(
            listOf(
                BookingEntity(
                    id = booking.id,
                    userId = booking.userId,
                    name = booking.name,
                    purpose = booking.purpose,
                    date = booking.date,
                    timeSlot = booking.timeSlot,
                    contactNumber = booking.contactNumber,
                    status = "PENDING"
                )
            )
        )
    }
}
