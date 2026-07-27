package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.PastelPaletteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppThemeManager(private val context: Context, private val prefs: SharedPreferences) {

    private val _selectedTheme = MutableStateFlow(getSavedTheme())
    val selectedTheme: StateFlow<PastelPaletteType> = _selectedTheme.asStateFlow()

    private val _useSystemFont = MutableStateFlow(getSavedUseSystemFont())
    val useSystemFont: StateFlow<Boolean> = _useSystemFont.asStateFlow()

    private fun getSavedTheme(): PastelPaletteType {
        val themeName = prefs.getString(KEY_APP_THEME, PastelPaletteType.LAVENDER.name) ?: PastelPaletteType.LAVENDER.name
        return try {
            PastelPaletteType.valueOf(themeName)
        } catch (e: Exception) {
            PastelPaletteType.LAVENDER
        }
    }

    private fun getSavedUseSystemFont(): Boolean {
        return prefs.getBoolean(KEY_USE_SYSTEM_FONT, false)
    }

    fun setAppTheme(theme: PastelPaletteType, isPassActive: Boolean): Boolean {
        if (!theme.isFree && !isPassActive) {
            return false
        }
        prefs.edit().putString(KEY_APP_THEME, theme.name).apply()
        _selectedTheme.value = theme
        return true
    }

    fun setUseSystemFont(useSystem: Boolean) {
        prefs.edit().putBoolean(KEY_USE_SYSTEM_FONT, useSystem).apply()
        _useSystemFont.value = useSystem
    }

    companion object {
        private const val KEY_APP_THEME = "selected_pastel_app_theme"
        private const val KEY_USE_SYSTEM_FONT = "use_system_font_setting"
    }
}
