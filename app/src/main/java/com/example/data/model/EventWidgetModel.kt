package com.example.data.model

import java.util.concurrent.TimeUnit

data class EventWidgetModel(
    val title: String = "My Birthday",
    val targetDateMillis: Long = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(12),
    val iconEmoji: String = "🎂"
) {
    fun getDaysRemaining(): Long {
        val diff = targetDateMillis - System.currentTimeMillis()
        val days = diff / (1000 * 60 * 60 * 24)
        return if (days >= 0) days else 0
    }
}
