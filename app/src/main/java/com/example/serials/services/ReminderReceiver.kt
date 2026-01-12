package com.example.serials.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.serials.MainActivity
import com.example.serials.R

class ReminderReceiver: BroadcastReceiver() {
    private val TAG = "ReminderReceiver"

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(p0: Context?, p1: Intent?) {

        Log.d(TAG, "=== BROADCAST ПОЛУЧЕН ===")
        Log.d(TAG, "Context: $p0")
        Log.d(TAG, "Intent: $p1")

        if (p0 == null || p0== null) {
            Log.e(TAG, "Context или Intent null!")
            return
        }

        val title = p1?.getStringExtra("title") ?: ""
        val message = p1?.getStringExtra("message") ?: ""
        val imdbId = p1?.getStringExtra("imdbId") ?: ""

        showNotification(p0, title, message, imdbId)
    }

    private fun createNotificationChannel(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
              CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
                setShowBadge(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(context: Context,
                                 title: String,
                                 message: String,
                                 imdbId: String){

        createNotificationChannel(context)

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("imdbId", imdbId)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.outline_movie_24)
            setContentTitle(title)
            setContentText(message)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }.build()

        val notificationId = System.currentTimeMillis().toInt()
        NotificationManagerCompat.from(context)
            .notify(notificationId, notification)
    }

    companion object {
        const val CHANNEL_ID = "serials_reminder"
        const val CHANNEL_NAME = "Напоминания о сериалах"
        const val CHANNEL_DESCRIPTION = "Уведомления о предстоящих сериалах и эпизодах"
    }
}