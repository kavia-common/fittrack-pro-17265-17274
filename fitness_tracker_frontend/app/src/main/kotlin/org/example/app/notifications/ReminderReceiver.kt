package org.example.app.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import org.example.app.FitnessTrackerApp
import org.example.app.MainActivity
import org.example.app.R

/**
 * Receives the scheduled alarm and posts a simple notification to remind logging.
 * Checks for POST_NOTIFICATIONS permission on Android 13+ before posting.
 */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val contentIntent = PendingIntent.getActivity(
            context,
            2001,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, FitnessTrackerApp.Notifications.CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.reminder_title))
            .setContentText(context.getString(R.string.reminder_text))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
                if (!granted) {
                    // Permission not granted; skip posting notification.
                    return
                }
            }
            NotificationManagerCompat.from(context).notify(3001, notification)
        } catch (se: SecurityException) {
            // In case permission is revoked at runtime; safely ignore.
        }
    }
}
