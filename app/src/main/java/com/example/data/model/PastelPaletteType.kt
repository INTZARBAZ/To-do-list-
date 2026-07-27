package com.example.data.model

enum class PastelPaletteType(
    val displayName: String,
    val description: String,
    val primaryColorHex: String,
    val isFree: Boolean = false
) {
    BASIC_LIGHT("Basic Light", "Clean Modern Off-White", "#6750A4", isFree = true),
    BASIC_DARK("Basic Dark", "Sleek Dark Mode Canvas", "#D0BCFF", isFree = true),
    LAVENDER("Lavender Mist", "Soft Lilac & Purple Bliss", "#8E7CC3", isFree = true),
    SOFT_ROSE("Soft Rose", "Warm Blush & Coral Pink", "#E66A85", isFree = false),
    MINT("Mint Breeze", "Refreshing Soft Sage & Teal", "#3BB0A8", isFree = false),
    OCEAN("Ocean Sky", "Calming Sky & Breeze Blue", "#487BB8", isFree = false),
    SUNNY_PEACH("Sunny Peach", "Warm Amber & Peach Glow", "#FF9800", isFree = false),
    CYBERPUNK("Cyberpunk Neon", "High Contrast Neon Purple & Cyan", "#00E5FF", isFree = false),
    TERMINAL("Terminal Green", "Retro Phosphor Green Console", "#00FF66", isFree = false),
    GLASSMORPHISM("Glassmorphism", "Translucent Frost & Pastel Glow", "#7B1FA2", isFree = false),
    WARM_NEUTRAL("Warm Neutral", "Cozy Earthy Tones & Sand", "#795548", isFree = false),
    MATERIAL_YOU("Material You", "Dynamic Color from Android Wallpaper", "#6750A4", isFree = false)
}
