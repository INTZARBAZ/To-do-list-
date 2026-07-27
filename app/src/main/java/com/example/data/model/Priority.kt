package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class Priority(val label: String, val colorHex: String) {
    LOW("Low", "#B8E0D2"),      // Soft Mint
    MEDIUM("Medium", "#A3C4F3"), // Soft Sky Blue
    HIGH("High", "#FFC2D1"),     // Soft Coral Rose
    URGENT("Urgent", "#FF99AC"); // Soft Crimson Rose

    fun toColor(): Color {
        return try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            Color(0xFFB8E0D2)
        }
    }
}
