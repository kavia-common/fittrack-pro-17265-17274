package org.example.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

/**
 * Application class used for one-time initialization such as notification channel setup.
 */
class FitnessTrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createReminderChannel()
    }

    private fun createReminderChannel() {
        // Create reminder channel for daily notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = Notifications.CHANNEL_ID_REMINDERS
            val channelName = "Daily Reminders"
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Reminders to log activities and workouts"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    object Notifications {
        const val CHANNEL_ID_REMINDERS = "fittrack_reminders"
    }
}
