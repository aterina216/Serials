package com.example.serials.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.serials.data.remote.dto.SerialDetails
import com.example.serials.services.ReminderReceiver

object Reminder {

    private const val TAG = "Reminder"

    fun showReminder(
        context: Context,
        serialDetails: SerialDetails,
        time: Long
    ) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (!PermissionHelper.hasNotificationPermission(context))
            {
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }

        Log.d(TAG, "=== НАЧАЛО УСТАНОВКИ НАПОМИНАНИЯ ===")
        Log.d(TAG, "Сериал: ${serialDetails.Title}")
        Log.d(TAG, "Время: $time (${java.util.Date(time)})")
        Log.d(TAG, "Текущее время: ${System.currentTimeMillis()}")

        // 1. Проверяем, что время в будущем
        if (time <= System.currentTimeMillis()) {
            Log.e(TAG, "ОШИБКА: Время в прошлом!")
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = "com.example.serials.REMINDER_ACTION"
            putExtra("title", serialDetails.Title ?: "Сериал")
            putExtra("message", "Не забудьте посмотреть!")
            putExtra("imdbId", serialDetails.imdbID)
            putExtra("posterUrl", serialDetails.Poster)
        }

        Log.d(TAG, "Создан Intent: action=${intent.action}, extras=${intent.extras}")

        val requestCode = (serialDetails.imdbID.hashCode() and 0xffff).toInt()
        Log.d(TAG, "RequestCode: $requestCode")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        Log.d(TAG, "PendingIntent создан: $pendingIntent")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            Log.d(TAG, "Будильник установлен через setExactAndAllowWhileIdle")
        }
        else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }
}