package com.grama.angana.domain.repository

import com.grama.angana.domain.model.Booking
import com.grama.angana.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>
    suspend fun registerWithEmail(name: String, email: String, phone: String, password: String): Result<Unit>
    suspend fun logout()
}

interface BookingRepository {
    fun observeBookings(): Flow<List<Booking>>
    suspend fun submitBooking(booking: Booking): Result<Unit>
}
