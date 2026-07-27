package com.example.widget

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.model.TaskEntity
import com.example.data.preferences.PassAndThemeManager

class TaskWidgetRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TaskWidgetViewsFactory(this.applicationContext)
    }
}

class TaskWidgetViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var tasks: List<TaskEntity> = emptyList()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        try {
            val db = AppDatabase.getDatabase(context)
            tasks = db.taskDao().getAllTasksSync()
        } catch (e: Exception) {
            e.printStackTrace()
            tasks = emptyList()
        }
    }

    override fun onDestroy() {
        tasks = emptyList()
    }

    override fun getCount(): Int = tasks.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position < 0 || position >= tasks.size) return RemoteViews(context.packageName, R.layout.widget_task_item)

        val task = tasks[position]
        val views = RemoteViews(context.packageName, R.layout.widget_task_item)

        val passAndThemeManager = PassAndThemeManager(context)
        val widgetTheme = passAndThemeManager.selectedWidgetTheme.value
        val colors = WidgetThemeConfig.getWidgetColors(widgetTheme)

        views.setTextViewText(R.id.widget_item_title, task.title)
        views.setTextViewText(R.id.widget_item_category, task.category)
        views.setTextColor(R.id.widget_item_category, Color.parseColor(colors.subtextColorHex))

        try {
            val itemBgColor = Color.parseColor(colors.itemBgColorHex)
            views.setInt(R.id.widget_item_container, "setBackgroundColor", itemBgColor)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (task.isCompleted) {
            views.setTextViewText(R.id.widget_item_status, "✓")
            views.setTextColor(R.id.widget_item_status, Color.parseColor(colors.activeCheckColorHex))
            views.setTextColor(R.id.widget_item_title, Color.parseColor(colors.completedTextColorHex))
        } else {
            views.setTextViewText(R.id.widget_item_status, "○")
            views.setTextColor(R.id.widget_item_status, Color.parseColor(colors.inactiveCheckColorHex))
            views.setTextColor(R.id.widget_item_title, Color.parseColor(colors.activeTextColorHex))
        }

        val fillInIntent = Intent().apply {
            putExtra("taskId", task.id)
        }
        views.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent)

        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long {
        return if (position in tasks.indices) tasks[position].id else position.toLong()
    }

    override fun hasStableIds(): Boolean = true
}
