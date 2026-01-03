package com.example.serials.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.serials.data.db.dao.SerialDao
import com.example.serials.data.db.entity.SerialEntity

@Database(entities = [SerialEntity::class], version = 2, exportSchema = false)
abstract class SerialsDatabase: RoomDatabase() {

    abstract fun dao(): SerialDao

    companion object {
        fun getDatabase(context: Context) : SerialsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                SerialsDatabase::class.java,
                "serials_bd"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}