package com.example.serials.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.serials.data.db.entity.SerialEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface SerialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerialsToDB(serials: List<SerialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerialToDB(serial: SerialEntity)

    @Query("SELECT * FROM serials")
    fun getSerilals(): Flow<List<SerialEntity>>

    @Query("SELECT * FROM serials WHERE category = :category")
    suspend fun getSerialsFromCategory(category: String): List<SerialEntity>

    @Query("UPDATE serials SET status = :status WHERE imdbID = :imdbId")
    suspend fun updateSerialStatus(imdbId: String, status: String?)

    @Query("SELECT status FROM serials WHERE imdbID = :id")
    suspend fun getStatus(id: String): String?

    @Query("SELECT * FROM serials WHERE status = :status")
    suspend fun getSerialsByStatus(status: String?): List<SerialEntity>

    @Query("SELECT COUNT(*) FROM serials")
    suspend fun getTotalCount(): Int

    @Query("SELECT * FROM serials WHERE imdbID = :imdbId")
    suspend fun getSerialByImdbId(imdbId: String): SerialEntity?

    @Query("SELECT imdbID FROM serials")
    suspend fun getAllImdbIds(): List<String>

    @Query("UPDATE serials SET watchedAt = :wathchedAt WHERE imdbID = :imdbId")
    suspend fun updateCurrentTime(imdbId: String, wathchedAt: Long?)

    @Query("SELECT * FROM serials WHERE watchedAt IS NOT NULL ORDER BY watchedAt DESC LIMIT 100")
    suspend fun getHistorySerials(): List<SerialEntity>

    @Query("UPDATE serials SET watchedAt = NULL")
    suspend fun clearHistory()
}