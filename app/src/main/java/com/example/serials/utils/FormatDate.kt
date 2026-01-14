package com.example.serials.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object FormatDate {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatReminderTime(timeMillis: Long) : String {
        val instant = Instant.ofEpochMilli(timeMillis)
        val dateTime = instant.atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return "Напоминание: ${dateTime.format(formatter)}"
    }
}