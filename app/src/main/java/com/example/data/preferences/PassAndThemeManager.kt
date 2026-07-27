package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.PastelPaletteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

class PassAndThemeManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("pastel_todo_prefs", Context.MODE_PRIVATE)

    val appThemeManager = AppThemeManager(context, prefs)
    val widgetThemeManager = WidgetThemeManager(context, prefs)
    val eventWidgetPreferences = EventWidgetPreferences(context, prefs)

    private val _passExpirationTimestamp = MutableStateFlow(getSavedExpiration())
    val passExpirationTimestamp: StateFlow<Long> = _passExpirationTimestamp.asStateFlow()

    val selectedTheme: StateFlow<PastelPaletteType> = appThemeManager.selectedTheme
    val useSystemFont: StateFlow<Boolean> = appThemeManager.useSystemFont

    val selectedWidgetTheme: StateFlow<PastelPaletteType> = widgetThemeManager.selectedWidgetTheme
    val widgetOpacity: StateFlow<Float> = widgetThemeManager.widgetOpacity
    val eventWidgetData = eventWidgetPreferences.eventWidgetModel

    init {
        // First install initialization: grant initial 24h pass for a warm welcome
        if (!prefs.contains(KEY_PASS_EXPIRATION)) {
            val initialPass = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)
            saveExpiration(initialPass)
        }
    }

    private fun getSavedExpiration(): Long {
        return prefs.getLong(KEY_PASS_EXPIRATION, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24))
    }

    private fun saveExpiration(timestamp: Long) {
        prefs.edit().putLong(KEY_PASS_EXPIRATION, timestamp).apply()
        _passExpirationTimestamp.value = timestamp
    }

    fun isPassActive(): Boolean {
        return System.currentTimeMillis() < _passExpirationTimestamp.value
    }

    fun getPassRemainingMillis(): Long {
        val remaining = _passExpirationTimestamp.value - System.currentTimeMillis()
        return if (remaining > 0) remaining else 0L
    }

    /**
     * Extend Pass by hours:
     * 1 Ad watched = extend timestamp by 8 hours;
     * 2 Ads watched = extend by 24 hours.
     */
    fun extendPassByHours(hours: Int) {
        val currentTime = System.currentTimeMillis()
        val currentExpiration = _passExpirationTimestamp.value
        val baseTime = if (currentExpiration > currentTime) currentExpiration else currentTime
        val extensionMillis = TimeUnit.HOURS.toMillis(hours.toLong())
        val newExpiration = baseTime + extensionMillis
        saveExpiration(newExpiration)
    }

    fun setTheme(theme: PastelPaletteType): Boolean {
        return appThemeManager.setAppTheme(theme, isPassActive())
    }

    fun setWidgetTheme(theme: PastelPaletteType): Boolean {
        return widgetThemeManager.setWidgetTheme(theme, isPassActive())
    }

    fun setUseSystemFont(useSystem: Boolean) {
        appThemeManager.setUseSystemFont(useSystem)
    }

    fun setWidgetOpacity(opacity: Float) {
        widgetThemeManager.setWidgetOpacity(opacity)
    }

    companion object {
        private const val KEY_PASS_EXPIRATION = "pass_expiration_timestamp"
    }
}
