package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.preferences.PassAndThemeManager
import com.example.service.WidgetService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_TOGGLE_TASK) {
            val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)
            if (taskId != -1L) {
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(context)
                        val task = db.taskDao().getTaskByIdSync(taskId)
                        if (task != null) {
                            val updatedTask = task.copy(isCompleted = !task.isCompleted)
                            db.taskDao().updateTaskSync(updatedTask)
                            WidgetService.updateHomeScreenWidget(context)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_TOGGLE_TASK = "com.example.ACTION_TOGGLE_TASK"
        const val EXTRA_TASK_ID = "com.example.EXTRA_TASK_ID"

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_task_list)

            val passAndThemeManager = PassAndThemeManager(context)
            val widgetTheme = passAndThemeManager.selectedWidgetTheme.value
            val opacity = passAndThemeManager.widgetOpacity.value
            val colors = WidgetThemeConfig.getWidgetColors(widgetTheme)
            val config = WidgetThemeCatalog.getConfigForPalette(widgetTheme)

            // Set Background opacity
            val alphaInt = (opacity * 255).toInt().coerceIn(20, 255)
            val baseColorInt = Color.parseColor(colors.backgroundColorHex)
            val r = Color.red(baseColorInt)
            val g = Color.green(baseColorInt)
            val b = Color.blue(baseColorInt)
            val bgColor = Color.argb(alphaInt, r, g, b)

            views.setInt(R.id.widget_container_root, "setBackgroundColor", bgColor)
            views.setTextColor(R.id.widget_title, Color.parseColor(colors.titleColorHex))
            views.setTextColor(R.id.widget_count, Color.parseColor(colors.subtextColorHex))
            views.setTextColor(R.id.widget_empty_view, Color.parseColor(colors.subtextColorHex))

            // Get count of pending tasks
            try {
                val db = AppDatabase.getDatabase(context)
                val tasks = db.taskDao().getAllTasksSync()
                val pendingCount = tasks.count { !it.isCompleted }
                views.setTextViewText(R.id.widget_count, "$pendingCount Pending")
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_count, "Tasks")
            }

            val stickerEmoji = when (config.accentSticker) {
                AccentSticker.SUN -> " ☀️"
                AccentSticker.FLOWER -> " 🌸"
                AccentSticker.PIN -> " 📌"
                AccentSticker.HEART -> " 💖"
                AccentSticker.PALETTE -> " 🎨"
                AccentSticker.COFFEE -> " ☕"
                AccentSticker.ARCH -> " 🏛️"
                AccentSticker.SAKURA -> " 🌸"
                AccentSticker.SPARKLE -> " ✨"
                AccentSticker.NONE -> ""
            }
            views.setTextViewText(R.id.widget_title, "Pastel Checklist$stickerEmoji")

            val intent = Intent(context, TaskWidgetRemoteViewsService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            views.setRemoteAdapter(R.id.widget_list, intent)
            views.setEmptyView(R.id.widget_list, R.id.widget_empty_view)

            // PendingIntent for toggling task completion when clicking items in the list
            val toggleIntent = Intent(context, TaskAppWidgetProvider::class.java).apply {
                action = ACTION_TOGGLE_TASK
            }
            val togglePendingIntent = PendingIntent.getBroadcast(
                context,
                100,
                toggleIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            views.setPendingIntentTemplate(R.id.widget_list, togglePendingIntent)

            // App launch intent when clicking widget title
            val appIntent = Intent(context, MainActivity::class.java)
            val appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_title, appPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
