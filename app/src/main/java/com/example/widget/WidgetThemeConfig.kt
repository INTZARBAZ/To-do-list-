package com.example.widget

import com.example.data.model.PastelPaletteType

data class WidgetColors(
    val backgroundColorHex: String,
    val titleColorHex: String,
    val subtextColorHex: String,
    val itemBgColorHex: String,
    val activeCheckColorHex: String,
    val inactiveCheckColorHex: String,
    val activeTextColorHex: String,
    val completedTextColorHex: String
)

object WidgetThemeConfig {

    fun getWidgetColors(theme: PastelPaletteType): WidgetColors {
        val config = WidgetThemeCatalog.getConfigForPalette(theme)
        return WidgetColors(
            backgroundColorHex = config.backgroundColorHex,
            titleColorHex = config.titleColorHex,
            subtextColorHex = config.subtextColorHex,
            itemBgColorHex = config.itemBgColorHex,
            activeCheckColorHex = config.activeCheckColorHex,
            inactiveCheckColorHex = config.inactiveCheckColorHex,
            activeTextColorHex = config.activeTextColorHex,
            completedTextColorHex = config.completedTextColorHex
        )
    }
}

