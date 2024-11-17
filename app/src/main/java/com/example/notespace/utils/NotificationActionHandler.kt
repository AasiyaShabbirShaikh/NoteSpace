package com.example.notespace.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


class NotificationActionHandler : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationActionHandler", "onReceive called with action: ${intent.action}")
        val notificationId = intent.getIntExtra("notification_id", -1)

        when (intent.action) {
            "CLEAR_NOTIFICATION" -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
            "SNOOZE_NOTIFICATION" -> {
                val snoozeTime = intent.getLongExtra("snooze_time", System.currentTimeMillis() + 60000)
                val title = intent.getStringExtra("title") ?:  "Untitled not"
                val message = intent.getStringExtra("message") ?: ""
                val time = intent.getStringExtra("time") ?: ""

                scheduleSnoozeTime(context, title, message, time, snoozeTime)
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
    }

    private fun scheduleSnoozeTime(context: Context, title:String, message: String,time: String, snoozeTime: Long){
        val delayTime= snoozeTime - System.currentTimeMillis()
        val snoozeNotification = System.currentTimeMillis().toInt()
        val inputData = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .putString("time", time)
            .putInt("notification_Id", snoozeNotification)
            .build()

        val snoozeWorkRequest = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
            .setInitialDelay(delayTime, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(snoozeWorkRequest)
    }
}