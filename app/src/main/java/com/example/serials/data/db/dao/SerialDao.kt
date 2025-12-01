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
}