package com.example.widget

import com.example.data.model.PastelPaletteType

enum class ThemeTier {
    FREE,
    STANDARD_PREMIUM, // Pass Required
    GOLD_VIP          // Pass Required
}

enum class PatternType {
    GRAPH_GRID,
    STICKY_NOTE,
    RAINBOW_ROWS,
    DASHED_LINES,
    SAGE_PLANNER,
    SUNBURST_PERIWINKLE,
    RAINBOW_DOODLE,
    BLUE_OIL_PAINT,
    LIME_HIGHLIGHTER,
    COZY_LATTE,
    TERRACOTTA_ARCH,
    BLOSSOM_CHECKLIST,
    KAWAII_PINK
}

enum class WidgetFontFamily {
    STANDARD,
    HANDWRITTEN,
    MONOSPACE
}

enum class AccentSticker {
    NONE,
    SUN,
    FLOWER,
    PIN,
    HEART,
    PALETTE,
    COFFEE,
    ARCH,
    SAKURA,
    SPARKLE
}

enum class RowHighlightStyle {
    SOLID,
    ALTERNATING_PASTEL,
    HIGHLIGHT_BAR
}

data class WidgetStyleConfig(
    val id: String,
    val paletteType: PastelPaletteType,
    val name: String,
    val description: String,
    val tier: ThemeTier,
    val backgroundColorHex: String,
    val gradientColorsHex: List<String> = emptyList(),
    val patternType: PatternType,
    val fontFamily: WidgetFontFamily = WidgetFontFamily.STANDARD,
    val accentSticker: AccentSticker = AccentSticker.NONE,
    val rowHighlightStyle: RowHighlightStyle = RowHighlightStyle.SOLID,
    val titleColorHex: String = "#1C1B1F",
    val subtextColorHex: String = "#79747E",
    val itemBgColorHex: String = "#FFFFFF",
    val activeCheckColorHex: String = "#8E7CC3",
    val inactiveCheckColorHex: String = "#1D192B",
    val activeTextColorHex: String = "#1C1B1F",
    val completedTextColorHex: String = "#79747E",
    val borderColorHex: String? = null
) {
    val isFree: Boolean
        get() = tier == ThemeTier.FREE
}

object WidgetThemeCatalog {

    val presetCatalog: List<WidgetStyleConfig> = listOf(
        // Theme 1: Graph Grid Notebook (Free Tier)
        WidgetStyleConfig(
            id = "graph_grid",
            paletteType = PastelPaletteType.SUNNY_PEACH,
            name = "Graph Grid Notebook",
            description = "Crisp light blue grid, double red margin, pastel task highlights",
            tier = ThemeTier.FREE,
            backgroundColorHex = "#FFFFFF",
            titleColorHex = "#1A237E",
            subtextColorHex = "#5C6BC0",
            itemBgColorHex = "#F5F7FA",
            activeCheckColorHex = "#64B5F6",
            inactiveCheckColorHex = "#1A237E",
            activeTextColorHex = "#1A237E",
            completedTextColorHex = "#90CAF9",
            fontFamily = WidgetFontFamily.MONOSPACE,
            patternType = PatternType.GRAPH_GRID,
            rowHighlightStyle = RowHighlightStyle.HIGHLIGHT_BAR
        ),

        // Theme 2: Sticky Note (Free Tier)
        WidgetStyleConfig(
            id = "sticky_note",
            paletteType = PastelPaletteType.LAVENDER,
            name = "Sticky Note",
            description = "Yellow notepad canvas with top tape accent and pin",
            tier = ThemeTier.FREE,
            backgroundColorHex = "#FFF9C4",
            titleColorHex = "#5D4037",
            subtextColorHex = "#8D6E63",
            itemBgColorHex = "#FFFDE7",
            activeCheckColorHex = "#FBC02D",
            inactiveCheckColorHex = "#5D4037",
            activeTextColorHex = "#3E2723",
            completedTextColorHex = "#A1887F",
            fontFamily = WidgetFontFamily.HANDWRITTEN,
            patternType = PatternType.STICKY_NOTE,
            accentSticker = AccentSticker.PIN
        ),

        // Theme 3: Pastel Rainbow Rows (Pass Required)
        WidgetStyleConfig(
            id = "pastel_rainbow_rows",
            paletteType = PastelPaletteType.TERMINAL,
            name = "Pastel Rainbow Rows",
            description = "Soft purple card frame with alternating pastel task background rows",
            tier = ThemeTier.STANDARD_PREMIUM,
            backgroundColorHex = "#F3E5F5",
            borderColorHex = "#CE93D8",
            titleColorHex = "#4A148C",
            subtextColorHex = "#7B1FA2",
            itemBgColorHex = "#F8BBD0",
            activeCheckColorHex = "#AB47BC",
            inactiveCheckColorHex = "#4A148C",
            activeTextColorHex = "#260033",
            completedTextColorHex = "#BA68C8",
            fontFamily = WidgetFontFamily.STANDARD,
            patternType = PatternType.RAINBOW_ROWS,
            rowHighlightStyle = RowHighlightStyle.ALTERNATING_PASTEL
        ),

        // Theme 4: Floral Cottagecore (Pass Required)
        WidgetStyleConfig(
            id = "floral_cottagecore",
            paletteType = PastelPaletteType.SOFT_ROSE,
            name = "Floral Cottagecore",
            description = "Warm cream canvas, dashed ruled lines, top corner floral accent",
            tier = ThemeTier.STANDARD_PREMIUM,
            backgroundColorHex = "#FFFDF5",
            gradientColorsHex = listOf("#FFFDF5", "#FFF3E0"),
            borderColorHex = "#FFD700",
            titleColorHex = "#4A3E3D",
            subtextColorHex = "#8C7A78",
            itemBgColorHex = "#FFF8F0",
            activeCheckColorHex = "#E66A85",
            inactiveCheckColorHex = "#4A3E3D",
            activeTextColorHex = "#4A3E3D",
            completedTextColorHex = "#BCAAA4",
            fontFamily = WidgetFontFamily.HANDWRITTEN,
            patternType = PatternType.DASHED_LINES,
            accentSticker = AccentSticker.FLOWER
        ),

        // Theme 5: Sage Green Planner (Pass Required)
        WidgetStyleConfig(
            id = "sage_green_planner",
            paletteType = PastelPaletteType.MINT,
            name = "Sage Green Planner",
            description = "Soft sage green outer frame with inset off-white task checklist",
            tier = ThemeTier.GOLD_VIP,
            backgroundColorHex = "#C8E6C9",
            borderColorHex = "#A5D6A7",
            titleColorHex = "#1B5E20",
            subtextColorHex = "#388E3C",
            itemBgColorHex = "#FAFAFA",
            activeCheckColorHex = "#2E7D32",
            inactiveCheckColorHex = "#1B5E20",
            activeTextColorHex = "#1B5E20",
            completedTextColorHex = "#81C784",
            fontFamily = WidgetFontFamily.STANDARD,
            patternType = PatternType.SAGE_PLANNER
        ),

        // Theme 6: Sunburst Periwinkle (Pass Required)
        WidgetStyleConfig(
            id = "sunburst_periwinkle",
            paletteType = PastelPaletteType.OCEAN,
            name = "Sunburst Periwinkle",
            description = "Lavender frame, inset white card with top-right corner sun graphic",
            tier = ThemeTier.GOLD_VIP,
            backgroundColorHex = "#C5CAE9",
            gradientColorsHex = listOf("#C5CAE9", "#E8EAF6"),
            borderColorHex = "#FFD700",
            titleColorHex = "#4A148C",
            subtextColorHex = "#7E57C2",
            itemBgColorHex = "#FFFFFF",
            activeCheckColorHex = "#7E57C2",
            inactiveCheckColorHex = "#4A148C",
            activeTextColorHex = "#311B92",
            completedTextColorHex = "#B39DDB",
            fontFamily = WidgetFontFamily.STANDARD,
            patternType = PatternType.SUNBURST_PERIWINKLE,
            accentSticker = AccentSticker.SUN
        ),

        // Theme 7: Rainbow Doodle (Pass Required)
        WidgetStyleConfig(
            id = "rainbow_doodle",
            paletteType = PastelPaletteType.RAINBOW_DOODLE,
            name = "Rainbow Doodle",
            description = "Playful magenta border with thin colored horizontal notebook lines",
            tier = ThemeTier.STANDARD_PREMIUM,
            backgroundColorHex = "#FFFFFF",
            borderColorHex = "#A2007C",
            titleColorHex = "#A2007C",
            subtextColorHex = "#E91E63",
            itemBgColorHex = "#FFF5F8",
            activeCheckColorHex = "#A2007C",
            inactiveCheckColorHex = "#A2007C",
            activeTextColorHex = "#212121",
            completedTextColorHex = "#BDBDBD",
            fontFamily = WidgetFontFamily.HANDWRITTEN,
            patternType = PatternType.RAINBOW_DOODLE,
            accentSticker = AccentSticker.HEART
        ),

        // Theme 8: Blue Oil Paint (Pass Required)
        WidgetStyleConfig(
            id = "blue_oil_paint",
            paletteType = PastelPaletteType.BLUE_OIL_PAINT,
            name = "Blue Oil Paint",
            description = "Deep cobalt blue canvas with warm ochre yellow stroke accents",
            tier = ThemeTier.STANDARD_PREMIUM,
            backgroundColorHex = "#0038A8",
            borderColorHex = "#F4C430",
            titleColorHex = "#F4C430",
            subtextColorHex = "#E0E0E0",
            itemBgColorHex = "#002366",
            activeCheckColorHex = "#F4C430",
            inactiveCheckColorHex = "#F4C430",
            activeTextColorHex = "#FFFFFF",
            completedTextColorHex = "#90CAF9",
            fontFamily = WidgetFontFamily.MONOSPACE,
            patternType = PatternType.BLUE_OIL_PAINT,
            accentSticker = AccentSticker.PALETTE
        ),

        // Theme 9: Lime Highlighter (Pass Required)
        WidgetStyleConfig(
            id = "lime_highlighter",
            paletteType = PastelPaletteType.LIME_HIGHLIGHTER,
            name = "Lime Highlighter",
            description = "Clean white canvas with neon lime header highlight and soft gray lines",
            tier = ThemeTier.STANDARD_PREMIUM,
            backgroundColorHex = "#FFFFFF",
            borderColorHex = "#B5FF39",
            titleColorHex = "#1B5E20",
            subtextColorHex = "#424242",
            itemBgColorHex = "#FAFAFA",
            activeCheckColorHex = "#212121",
            inactiveCheckColorHex = "#212121",
            activeTextColorHex = "#212121",
            completedTextColorHex = "#9E9E9E",
            fontFamily = WidgetFontFamily.STANDARD,
            patternType = PatternType.LIME_HIGHLIGHTER
        ),

        // Theme 10: Cozy Latte (Pass Required)
        WidgetStyleConfig(
            id = "cozy_latte",
            paletteType = PastelPaletteType.COZY_LATTE,
            name = "Cozy Latte",
            description = "Warm cafe cream background with espresso brown borders and soft typography",
            tier = ThemeTier.GOLD_VIP,
            backgroundColorHex = "#FAF0E6",
            borderColorHex = "#4A2C2A",
            titleColorHex = "#4A2C2A",
            subtextColorHex = "#8D6E63",
            itemBgColorHex = "#F5E6D3",
            activeCheckColorHex = "#4A2C2A",
            inactiveCheckColorHex = "#4A2C2A",
            activeTextColorHex = "#3E2723",
            completedTextColorHex = "#A1887F",
            fontFamily = WidgetFontFamily.HANDWRITTEN,
            patternType = PatternType.COZY_LATTE,
            accentSticker = AccentSticker.COFFEE
        ),

        // Theme 11: Terracotta Arch (Pass Required)
        WidgetStyleConfig(
            id = "terracotta_arch",
            paletteType = PastelPaletteType.TERRACOTTA_ARCH,
            name = "Terracotta Arch",
            description = "Muted sand linen canvas with warm clay arch geometry and sage accents",
            tier = ThemeTier.GOLD_VIP,
            backgroundColorHex = "#F4EBE1",
            borderColorHex = "#C85A32",
            titleColorHex = "#C85A32",
            subtextColorHex = "#8A9A86",
            itemBgColorHex = "#FFF8F0",
            activeCheckColorHex = "#C85A32",
            inactiveCheckColorHex = "#C85A32",
            activeTextColorHex = "#3E2723",
            completedTextColorHex = "#BCAAA4",
            fontFamily = WidgetFontFamily.STANDARD,
            patternType = PatternType.TERRACOTTA_ARCH,
            accentSticker = AccentSticker.ARCH
        ),

        // Theme 12: Blossom Checklist (Pass Required)
        WidgetStyleConfig(
            id = "blossom_checklist",
            paletteType = PastelPaletteType.BLOSSOM_CHECKLIST,
            name = "Blossom Checklist",
            description = "Soft blossom cream canvas with sakura flower petal drawings and soft script",
            tier = ThemeTier.GOLD_VIP,
            backgroundColorHex = "#FFF5F6",
            borderColorHex = "#FFB7C5",
            titleColorHex = "#C2185B",
            subtextColorHex = "#90B77D",
            itemBgColorHex = "#FFFFFF",
            activeCheckColorHex = "#E91E63",
            inactiveCheckColorHex = "#C2185B",
            activeTextColorHex = "#4A148C",
            completedTextColorHex = "#F48FB1",
            fontFamily = WidgetFontFamily.HANDWRITTEN,
            patternType = PatternType.BLOSSOM_CHECKLIST,
            accentSticker = AccentSticker.SAKURA
        ),

        // Theme 13: Kawaii Pink Motivation (Pass Required)
        WidgetStyleConfig(
            id = "kawaii_pink",
            paletteType = PastelPaletteType.KAWAII_PINK,
            name = "Kawaii Pink Motivation",
            description = "Pastel strawberry pink with bright kawaii pink accents and sparkle stars",
            tier = ThemeTier.GOLD_VIP,
            backgroundColorHex = "#FFE4E1",
            borderColorHex = "#FF69B4",
            titleColorHex = "#FF1493",
            subtextColorHex = "#F50057",
            itemBgColorHex = "#FFF0F5",
            activeCheckColorHex = "#FF1493",
            inactiveCheckColorHex = "#FF69B4",
            activeTextColorHex = "#880E4F",
            completedTextColorHex = "#FF80AB",
            fontFamily = WidgetFontFamily.HANDWRITTEN,
            patternType = PatternType.KAWAII_PINK,
            accentSticker = AccentSticker.SPARKLE
        )
    )

    fun getConfigForPalette(palette: PastelPaletteType): WidgetStyleConfig {
        return presetCatalog.find { it.paletteType == palette } ?: presetCatalog.first()
    }

    fun getConfigById(id: String): WidgetStyleConfig {
        return presetCatalog.find { it.id == id } ?: presetCatalog.first()
    }
}
