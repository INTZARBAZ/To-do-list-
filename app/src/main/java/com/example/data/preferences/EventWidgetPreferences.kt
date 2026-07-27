package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.EventWidgetModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

class EventWidgetPreferences(private val context: Context, private val prefs: SharedPreferences) {

    private val _eventWidgetModel = MutableStateFlow(getSavedEventModel())
    val eventWidgetModel: StateFlow<EventWidgetModel> = _eventWidgetModel.asStateFlow()

    fun getSavedEventModel(): EventWidgetModel {
        val title = prefs.getString(KEY_EVENT_TITLE, "My Birthday") ?: "My Birthday"
        val targetMillis = prefs.getLong(
            KEY_EVENT_TARGET_MILLIS,
            System.currentTimeMillis() + TimeUnit.DAYS.toMillis(12)
        )
        val emoji = prefs.getString(KEY_EVENT_EMOJI, "🎂") ?: "🎂"

        return EventWidgetModel(
            title = title,
            targetDateMillis = targetMillis,
            iconEmoji = emoji
        )
    }

    fun saveEventModel(event: EventWidgetModel) {
        prefs.edit()
            .putString(KEY_EVENT_TITLE, event.title)
            .putLong(KEY_EVENT_TARGET_MILLIS, event.targetDateMillis)
            .putString(KEY_EVENT_EMOJI, event.iconEmoji)
            .apply()
        _eventWidgetModel.value = event
    }

    companion object {
        private const val KEY_EVENT_TITLE = "event_widget_title"
        private const val KEY_EVENT_TARGET_MILLIS = "event_widget_target_millis"
        private const val KEY_EVENT_EMOJI = "event_widget_emoji"
    }
}
