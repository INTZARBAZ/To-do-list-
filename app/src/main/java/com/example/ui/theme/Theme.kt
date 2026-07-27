package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import com.example.data.model.PastelPaletteType

// 1. Basic Light
private val BasicLightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    primaryContainer = Color(0xFFEADDFF),
    secondaryContainer = Color(0xFFE8DEF8),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF1D192B),
    onSurface = Color(0xFF1C1B1F),
    onSurfaceVariant = Color(0xFF49454F),
    background = Color(0xFFFEF7FF)
)

// 2. Basic Dark
private val BasicDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    primaryContainer = Color(0xFF4F378B),
    secondaryContainer = Color(0xFF4A4458),
    surface = Color(0xFF141218),
    onPrimary = Color(0xFF381E72),
    onSecondary = Color(0xFF332D41),
    onSurface = Color(0xFFE6E1E5),
    onSurfaceVariant = Color(0xFFCAC4D0),
    background = Color(0xFF141218)
)

// 3. Lavender Mist
private val LavenderColorScheme = lightColorScheme(
    primary = LavenderPrimary,
    secondary = LavenderSecondary,
    primaryContainer = LavenderContainer,
    secondaryContainer = Color(0xFFE8DEF8),
    surface = Color.White,
    onPrimary = Color(0xFF1D192B),
    onSecondary = LavenderOnSurface,
    onSurface = NaturalTextPrimary,
    onSurfaceVariant = NaturalTextMuted,
    background = NaturalBackground
)

// 4. Soft Rose
private val SoftRoseColorScheme = lightColorScheme(
    primary = SoftRosePrimary,
    secondary = SoftRoseSecondary,
    primaryContainer = SoftRoseContainer,
    secondaryContainer = Color(0xFFFFD8E1),
    surface = Color.White,
    onPrimary = Color(0xFF1D192B),
    onSecondary = SoftRoseOnSurface,
    onSurface = NaturalTextPrimary,
    onSurfaceVariant = NaturalTextMuted,
    background = NaturalBackground
)

// 5. Mint Breeze
private val MintColorScheme = lightColorScheme(
    primary = MintPrimary,
    secondary = MintSecondary,
    primaryContainer = MintContainer,
    secondaryContainer = Color(0xFFD4F5E9),
    surface = Color.White,
    onPrimary = Color(0xFF1D192B),
    onSecondary = MintOnSurface,
    onSurface = NaturalTextPrimary,
    onSurfaceVariant = NaturalTextMuted,
    background = NaturalBackground
)

// 6. Ocean Sky
private val OceanColorScheme = lightColorScheme(
    primary = OceanPrimary,
    secondary = OceanSecondary,
    primaryContainer = OceanContainer,
    secondaryContainer = Color(0xFFD3E5FF),
    surface = Color.White,
    onPrimary = Color(0xFF1D192B),
    onSecondary = OceanOnSurface,
    onSurface = NaturalTextPrimary,
    onSurfaceVariant = NaturalTextMuted,
    background = NaturalBackground
)

// 7. Sunny Peach
private val SunnyPeachColorScheme = lightColorScheme(
    primary = Color(0xFFFFB74D),
    secondary = Color(0xFFFFE0B2),
    primaryContainer = Color(0xFFFFF3E0),
    secondaryContainer = Color(0xFFFFECB3),
    surface = Color.White,
    onPrimary = Color(0xFF3E2723),
    onSecondary = Color(0xFF4E342E),
    onSurface = Color(0xFF2C1B18),
    onSurfaceVariant = Color(0xFF6D4C41),
    background = Color(0xFFFFFBF7)
)

// 8. Cyberpunk Neon
private val CyberpunkColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF),
    secondary = Color(0xFFFF007F),
    primaryContainer = Color(0xFF00363A),
    secondaryContainer = Color(0xFF38001F),
    surface = Color(0xFF0A0E17),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFFFFFFFF),
    onSurface = Color(0xFF00E5FF),
    onSurfaceVariant = Color(0xFFFF80AB),
    background = Color(0xFF05070C)
)

// 9. Terminal Green
private val TerminalColorScheme = darkColorScheme(
    primary = Color(0xFF00FF66),
    secondary = Color(0xFF00DD55),
    primaryContainer = Color(0xFF003311),
    secondaryContainer = Color(0xFF002208),
    surface = Color(0xFF0D1117),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF00FF66),
    onSurface = Color(0xFF00FF66),
    onSurfaceVariant = Color(0xFF00CC52),
    background = Color(0xFF05080A)
)

// 10. Glassmorphism
private val GlassmorphismColorScheme = lightColorScheme(
    primary = Color(0xFFAB47BC),
    secondary = Color(0xFFE1BEE7),
    primaryContainer = Color(0xFFF3E5F5),
    secondaryContainer = Color(0xFFEDE7F6),
    surface = Color(0xFFFBF8FE),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF311B92),
    onSurface = Color(0xFF1A237E),
    onSurfaceVariant = Color(0xFF5C6BC0),
    background = Color(0xFFF7F2FA)
)

// 11. Warm Neutral
private val WarmNeutralColorScheme = lightColorScheme(
    primary = Color(0xFFA1887F),
    secondary = Color(0xFFD7CCC8),
    primaryContainer = Color(0xFFEFEBE9),
    secondaryContainer = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF3E2723),
    onSurface = Color(0xFF2C1B18),
    onSurfaceVariant = Color(0xFF5D4037),
    background = Color(0xFFFAF8F5)
)

@Composable
fun PastelTodoTheme(
    paletteType: PastelPaletteType = PastelPaletteType.LAVENDER,
    useSystemFont: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme: ColorScheme = when (paletteType) {
        PastelPaletteType.BASIC_LIGHT -> BasicLightColorScheme
        PastelPaletteType.BASIC_DARK -> BasicDarkColorScheme
        PastelPaletteType.LAVENDER -> LavenderColorScheme
        PastelPaletteType.SOFT_ROSE -> SoftRoseColorScheme
        PastelPaletteType.MINT -> MintColorScheme
        PastelPaletteType.OCEAN -> OceanColorScheme
        PastelPaletteType.SUNNY_PEACH -> SunnyPeachColorScheme
        PastelPaletteType.CYBERPUNK -> CyberpunkColorScheme
        PastelPaletteType.TERMINAL -> TerminalColorScheme
        PastelPaletteType.GLASSMORPHISM -> GlassmorphismColorScheme
        PastelPaletteType.WARM_NEUTRAL -> WarmNeutralColorScheme
        PastelPaletteType.MATERIAL_YOU -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                LavenderColorScheme
            }
        }
    }

    val defaultTypography = Typography
    val effectiveTypography = if (useSystemFont) {
        Typography(
            displayLarge = defaultTypography.displayLarge.copy(fontFamily = FontFamily.Default),
            displayMedium = defaultTypography.displayMedium.copy(fontFamily = FontFamily.Default),
            displaySmall = defaultTypography.displaySmall.copy(fontFamily = FontFamily.Default),
            headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = FontFamily.Default),
            headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = FontFamily.Default),
            headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = FontFamily.Default),
            titleLarge = defaultTypography.titleLarge.copy(fontFamily = FontFamily.Default),
            titleMedium = defaultTypography.titleMedium.copy(fontFamily = FontFamily.Default),
            titleSmall = defaultTypography.titleSmall.copy(fontFamily = FontFamily.Default),
            bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = FontFamily.Default),
            bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = FontFamily.Default),
            bodySmall = defaultTypography.bodySmall.copy(fontFamily = FontFamily.Default),
            labelLarge = defaultTypography.labelLarge.copy(fontFamily = FontFamily.Default),
            labelMedium = defaultTypography.labelMedium.copy(fontFamily = FontFamily.Default),
            labelSmall = defaultTypography.labelSmall.copy(fontFamily = FontFamily.Default)
        )
    } else {
        defaultTypography
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = effectiveTypography,
        content = content
    )
}
