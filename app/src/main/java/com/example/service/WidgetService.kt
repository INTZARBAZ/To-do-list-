package com.example.service

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.example.R
import com.example.widget.TaskAppWidgetProvider

object WidgetService {

    fun updateHomeScreenWidget(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, TaskAppWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list)

                val intent = Intent(context, TaskAppWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                context.sendBroadcast(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
