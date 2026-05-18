package com.grama.angana.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.grama.angana.data.local.AppDatabase
import com.grama.angana.data.local.BookingDao
import com.grama.angana.data.repository.AuthRepositoryImpl
import com.grama.angana.data.repository.BookingRepositoryImpl
import com.grama.angana.domain.repository.AuthRepository
import com.grama.angana.domain.repository.BookingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlatformModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "grama_angana.db").build()

    @Provides
    fun provideBookingDao(db: AppDatabase): BookingDao = db.bookingDao()

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindBookingRepository(impl: BookingRepositoryImpl): BookingRepository
}
