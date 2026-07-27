package com.example.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class TaskReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_TASK_REMINDER) {
            val taskId = intent.getLongExtra(EXTRA_TASK_ID, 0L)
            val title = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Task Reminder"
            val notes = intent.getStringExtra(EXTRA_TASK_NOTES) ?: ""
            val category = intent.getStringExtra(EXTRA_CATEGORY) ?: "General"

            showNotification(context, taskId, title, notes, category)
        }
    }

    private fun showNotification(
        context: Context,
        taskId: Long,
        title: String,
        notes: String,
        category: String
    ) {
        NotificationHelper.createNotificationChannel(context)

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("🌸 $category: $title")
            .setContentText(notes.ifEmpty { "You have a scheduled task right now!" })
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(taskId.toInt(), notification)
    }

    companion object {
        const val ACTION_TASK_REMINDER = "com.example.ACTION_TASK_REMINDER"
        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TASK_TITLE = "extra_task_title"
        const val EXTRA_TASK_NOTES = "extra_task_notes"
        const val EXTRA_CATEGORY = "extra_category"
    }
}
