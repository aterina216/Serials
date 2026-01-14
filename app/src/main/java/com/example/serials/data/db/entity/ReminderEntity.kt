package com.example.serials.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "reminders",
    indices = [Index(value = ["imdbID"], unique = true)])
class ReminderEntity (
    @PrimaryKey
    val imdbID: String,
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    var isActive: Boolean = true,
    var time: Long? = null
)