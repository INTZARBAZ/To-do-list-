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
import com.example.data.preferences.PassAndThemeManager

class TaskAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_task_list)

            val passAndThemeManager = PassAndThemeManager(context)
            val widgetTheme = passAndThemeManager.selectedWidgetTheme.value
            val opacity = passAndThemeManager.widgetOpacity.value
            val colors = WidgetThemeConfig.getWidgetColors(widgetTheme)
            val config = WidgetThemeCatalog.getConfigForPalette(widgetTheme)
            val eventModel = passAndThemeManager.eventWidgetData.value

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

            views.setTextViewText(R.id.widget_title, "Pastel Checklist")
            views.setTextViewText(R.id.widget_count, "Tasks")

            val intent = Intent(context, TaskWidgetRemoteViewsService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            views.setRemoteAdapter(R.id.widget_list, intent)
            views.setEmptyView(R.id.widget_list, R.id.widget_empty_view)

            val appIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setPendingIntentTemplate(R.id.widget_list, pendingIntent)
            views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
