package com.grama.angana.domain.model

enum class UserRole { PUBLIC_USER, PANCHAYAT_ADMIN }

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole
)

data class Booking(
    val id: String,
    val userId: String,
    val name: String,
    val purpose: String,
    val date: String,
    val timeSlot: String,
    val contactNumber: String,
    val status: String
)

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val category: String
)

data class MaintenanceItem(
    val id: String,
    val title: String,
    val targetAmount: Double,
    val collectedAmount: Double,
    val supporters: Int
)
