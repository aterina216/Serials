package com.example.serials.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "serials")
data class SerialEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
)
