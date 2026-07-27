package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.PastelPaletteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WidgetThemeManager(private val context: Context, private val prefs: SharedPreferences) {

    private val _selectedWidgetTheme = MutableStateFlow(getSavedWidgetTheme())
    val selectedWidgetTheme: StateFlow<PastelPaletteType> = _selectedWidgetTheme.asStateFlow()

    private val _widgetOpacity = MutableStateFlow(getSavedWidgetOpacity())
    val widgetOpacity: StateFlow<Float> = _widgetOpacity.asStateFlow()

    private fun getSavedWidgetTheme(): PastelPaletteType {
        val themeName = prefs.getString(KEY_WIDGET_THEME, PastelPaletteType.LAVENDER.name) ?: PastelPaletteType.LAVENDER.name
        return try {
            PastelPaletteType.valueOf(themeName)
        } catch (e: Exception) {
            PastelPaletteType.LAVENDER
        }
    }

    private fun getSavedWidgetOpacity(): Float {
        return prefs.getFloat(KEY_WIDGET_OPACITY, 0.95f)
    }

    fun setWidgetTheme(theme: PastelPaletteType, isPassActive: Boolean): Boolean {
        if (!theme.isFree && !isPassActive) {
            return false
        }
        prefs.edit().putString(KEY_WIDGET_THEME, theme.name).apply()
        _selectedWidgetTheme.value = theme
        return true
    }

    fun setWidgetOpacity(opacity: Float) {
        val clamped = opacity.coerceIn(0.1f, 1.0f)
        prefs.edit().putFloat(KEY_WIDGET_OPACITY, clamped).apply()
        _widgetOpacity.value = clamped
    }

    companion object {
        private const val KEY_WIDGET_THEME = "selected_pastel_widget_theme"
        private const val KEY_WIDGET_OPACITY = "home_screen_widget_opacity"
    }
}
