package com.grama.angana.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val purpose: String,
    val date: String,
    val timeSlot: String,
    val contactNumber: String,
    val status: String
)

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val date: String,
    val category: String
)

@Entity(tableName = "maintenance_items")
data class MaintenanceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val targetAmount: Double,
    val collectedAmount: Double,
    val supporters: Int
)

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings")
    fun observeBookings(): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<BookingEntity>)

    @Query("SELECT COUNT(*) FROM bookings WHERE date = :date AND timeSlot = :timeSlot AND status = 'APPROVED'")
    suspend fun countApprovedBySlot(date: String, timeSlot: String): Int
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY date ASC")
    fun observeEvents(): Flow<List<EventEntity>>
}

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_items")
    fun observeItems(): Flow<List<MaintenanceEntity>>
}

@Database(
    entities = [BookingEntity::class, EventEntity::class, MaintenanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
    abstract fun eventDao(): EventDao
    abstract fun maintenanceDao(): MaintenanceDao
}
