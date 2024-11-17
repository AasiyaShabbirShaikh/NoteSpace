package com.example.notespace.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notespace.R

class ReminderNotificationWorker(context: Context, workParameters: WorkerParameters) : Worker(context, workParameters) {

    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "Untitled note"
        val description = inputData.getString("message") ?: ""
        val time = inputData.getString("time") ?: "Default Time"

        showNotification(title, description,time)

        return Result.success()
    }

    private fun showNotification(title: String, description: String, time: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = System.currentTimeMillis().toInt()

        val clearIntent = Intent(applicationContext, NotificationActionHandler::class.java).apply {
            action = "CLEAR_NOTIFICATION"
            putExtra("notification_id", notificationId)
        }

        val clearPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, clearIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val laterIntent = Intent(applicationContext, NotificationActionHandler::class.java).apply {
            action = "SNOOZE_NOTIFICATION"
            putExtra("notification_id", notificationId)
            putExtra("snooze_time", System.currentTimeMillis() + 300000)
            putExtra("title", title)
            putExtra("message", description)
            putExtra("time", time)
        }

        val laterPendingIntent = PendingIntent.getBroadcast(applicationContext,1,laterIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, "REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.notification_asset)
            .setContentTitle(title)
            .setContentText(description)
            .setContentInfo(time)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(0,"Clear",clearPendingIntent)
            .addAction(0,"Later",laterPendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}