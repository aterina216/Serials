package com.example.serials.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "serials",
    indices = [Index(value = ["imdbID"], unique = true)])
data class SerialEntity(
    @PrimaryKey
    val imdbID: String,
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    var category: String? = null,
    var status: String? = null
)
