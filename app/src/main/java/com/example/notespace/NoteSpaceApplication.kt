package com.example.notespace

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteSpaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }


    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(NotificationManager::class.java)

            // Create the notification channel
            val channel = NotificationChannel(
                "REMINDER_CHANNEL",  // Channel ID
                "Reminder Notifications",  // Channel name
                NotificationManager.IMPORTANCE_HIGH // Importance level
            ).apply {
                description = " Reminder notifications"
            }

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }
}
