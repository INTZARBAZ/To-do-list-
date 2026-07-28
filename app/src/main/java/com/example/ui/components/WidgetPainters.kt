package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.widget.PatternType
import com.example.widget.WidgetStyleConfig

@Composable
fun ThemeBackgroundCanvas(
    config: WidgetStyleConfig,
    opacity: Float = 1.0f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val alpha = opacity.coerceIn(0.1f, 1.0f)

    val baseBgColor = try {
        Color(android.graphics.Color.parseColor(config.backgroundColorHex)).copy(alpha = alpha)
    } catch (e: Exception) {
        Color.White.copy(alpha = alpha)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(baseBgColor)
    ) {
        when (config.patternType) {
            PatternType.GRAPH_GRID -> {
                GraphGridCanvas(alpha = alpha)
            }
            PatternType.STICKY_NOTE -> {
                StickyNoteCanvas(alpha = alpha)
            }
            PatternType.DASHED_LINES -> {
                FloralCottagecoreCanvas(alpha = alpha)
            }
            PatternType.SUNBURST_PERIWINKLE -> {
                SunburstPurpleCanvas(alpha = alpha)
            }
            PatternType.SAGE_PLANNER -> {
                SageGreenPlannerCanvas(alpha = alpha)
            }
            PatternType.RAINBOW_DOODLE -> {
                RainbowDoodleCanvas(alpha = alpha)
            }
            PatternType.BLUE_OIL_PAINT -> {
                BlueOilPaintCanvas(alpha = alpha)
            }
            PatternType.LIME_HIGHLIGHTER -> {
                LimeHighlighterCanvas(alpha = alpha)
            }
            PatternType.COZY_LATTE -> {
                CozyLatteCanvas(alpha = alpha)
            }
            PatternType.TERRACOTTA_ARCH -> {
                TerracottaArchCanvas(alpha = alpha)
            }
            PatternType.BLOSSOM_CHECKLIST -> {
                BlossomChecklistCanvas(alpha = alpha)
            }
            PatternType.KAWAII_PINK -> {
                KawaiiPinkCanvas(alpha = alpha)
            }
            PatternType.RAINBOW_ROWS -> {
                // Handled via getPastelRowColor
            }
            else -> {
                if (config.gradientColorsHex.size >= 2) {
                    val colors = config.gradientColorsHex.map {
                        try { Color(android.graphics.Color.parseColor(it)).copy(alpha = alpha) } catch (e: Exception) { baseBgColor }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(colors))
                    )
                }
            }
        }

        content()
    }
}

/**
 * 1. Graph Grid Notebook
 * - Crisp 20px x 20px grid of light blue lines (#E3F2FD)
 * - Vertical double red margin line (#FFCDD2) at x = 36dp
 */
@Composable
fun GraphGridCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridPx = 20.dp.toPx()
        val gridColor = Color(0xFFE3F2FD).copy(alpha = alpha)
        val redMarginColor = Color(0xFFFFCDD2).copy(alpha = alpha)

        // Draw vertical grid lines
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1.dp.toPx()
            )
            x += gridPx
        }

        // Draw horizontal grid lines
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
            y += gridPx
        }

        // Draw vertical double red margin line at x = 36dp
        val marginX1 = 34.dp.toPx()
        val marginX2 = 38.dp.toPx()
        val marginStroke = 1.5.dp.toPx()

        drawLine(
            color = redMarginColor,
            start = Offset(marginX1, 0f),
            end = Offset(marginX1, size.height),
            strokeWidth = marginStroke
        )

        drawLine(
            color = redMarginColor,
            start = Offset(marginX2, 0f),
            end = Offset(marginX2, size.height),
            strokeWidth = marginStroke
        )
    }
}

/**
 * 2. Sticky Note
 * - Yellow canvas (#FFF9C4)
 * - Dark yellow tape rectangle (#FFEE58) centered at top edge (width: 60dp, height: 16dp)
 */
@Composable
fun StickyNoteCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val yellowBg = Color(0xFFFFF9C4).copy(alpha = alpha)
        val tapeColor = Color(0xFFFFEE58).copy(alpha = (alpha * 0.9f).coerceIn(0.1f, 1.0f))

        drawRect(color = yellowBg)

        // Centered top tape rectangle
        val tapeWidth = 60.dp.toPx()
        val tapeHeight = 16.dp.toPx()
        val tapeLeft = (size.width - tapeWidth) / 2f

        drawRoundRect(
            color = tapeColor,
            topLeft = Offset(tapeLeft, 0f),
            size = Size(tapeWidth, tapeHeight),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
    }
}

/**
 * 3. Floral Cottagecore
 * - Warm cream background (#FFFDF5)
 * - Dashed horizontal lines (#E0E0E0) spaced 28dp apart
 * - Flower sticker/drawing at top-right (#E57373)
 */
@Composable
fun FloralCottagecoreCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val creamBg = Color(0xFFFFFDF5).copy(alpha = alpha)
        val dashedColor = Color(0xFFE0E0E0).copy(alpha = alpha)
        val flowerColor = Color(0xFFE57373).copy(alpha = alpha)

        drawRect(color = creamBg)

        // Dashed lines spaced 28dp apart
        val lineSpacing = 28.dp.toPx()
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(12.dp.toPx(), 8.dp.toPx()), 0f)

        var y = lineSpacing
        while (y < size.height) {
            drawLine(
                color = dashedColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = dashEffect
            )
            y += lineSpacing
        }

        // Top-right flower petals
        val flowerCenterX = size.width - 24.dp.toPx()
        val flowerCenterY = 24.dp.toPx()
        val petalRadius = 6.dp.toPx()

        val offsets = listOf(
            Offset(0f, -petalRadius),
            Offset(0f, petalRadius),
            Offset(-petalRadius, 0f),
            Offset(petalRadius, 0f)
        )

        for (offset in offsets) {
            drawCircle(
                color = flowerColor,
                radius = petalRadius,
                center = Offset(flowerCenterX + offset.x, flowerCenterY + offset.y)
            )
        }

        // Flower center
        drawCircle(
            color = Color(0xFFFFD54F).copy(alpha = alpha),
            radius = 4.dp.toPx(),
            center = Offset(flowerCenterX, flowerCenterY)
        )
    }
}

/**
 * 4. Sunburst Purple
 * - Outer border frame in periwinkle (#C5CAE9) with 16dp corner radius
 * - Inner card inset by 12dp in pure white (#FFFFFF) with 12dp corner radius
 * - Sun icon top-right (#FBC02D)
 */
@Composable
fun SunburstPurpleCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val frameColor = Color(0xFFC5CAE9).copy(alpha = alpha)
        val innerCardColor = Color.White.copy(alpha = alpha)
        val sunColor = Color(0xFFFBC02D).copy(alpha = alpha)

        // Outer periwinkle frame
        drawRoundRect(
            color = frameColor,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        // Inner white card inset by 12dp
        val inset = 12.dp.toPx()
        drawRoundRect(
            color = innerCardColor,
            topLeft = Offset(inset, inset),
            size = Size(size.width - inset * 2, size.height - inset * 2),
            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
        )

        // Sun vector drawing at top-right
        val sunCenterX = size.width - 28.dp.toPx()
        val sunCenterY = 28.dp.toPx()
        val sunRadius = 8.dp.toPx()

        drawCircle(
            color = sunColor,
            radius = sunRadius,
            center = Offset(sunCenterX, sunCenterY)
        )

        // Sun rays
        for (i in 0 until 8) {
            val angle = Math.toRadians(i * 45.0)
            val rayStart = Offset(
                (sunCenterX + (sunRadius + 3.dp.toPx()) * Math.cos(angle)).toFloat(),
                (sunCenterY + (sunRadius + 3.dp.toPx()) * Math.sin(angle)).toFloat()
            )
            val rayEnd = Offset(
                (sunCenterX + (sunRadius + 7.dp.toPx()) * Math.cos(angle)).toFloat(),
                (sunCenterY + (sunRadius + 7.dp.toPx()) * Math.sin(angle)).toFloat()
            )
            drawLine(
                color = sunColor,
                start = rayStart,
                end = rayEnd,
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}

/**
 * 5. Sage Green Planner
 * - Outer frame in soft sage green (#C8E6C9)
 * - Inner task container in off-white (#FAFAFA)
 */
@Composable
fun SageGreenPlannerCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val sageBg = Color(0xFFC8E6C9).copy(alpha = alpha)
        val innerBg = Color(0xFFFAFAFA).copy(alpha = alpha)

        drawRoundRect(
            color = sageBg,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        val inset = 10.dp.toPx()
        drawRoundRect(
            color = innerBg,
            topLeft = Offset(inset, inset),
            size = Size(size.width - inset * 2, size.height - inset * 2),
            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
        )
    }
}

/**
 * 7. Cyberpunk Neon
 * - Pure black background (#000000)
 * - Border gradient from Cyan (#00E5FF) to Hot Pink (#FF007F) with stroke width 2.5dp
 */
@Composable
fun CyberpunkNeonCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val blackBg = Color.Black.copy(alpha = alpha)
        drawRoundRect(
            color = blackBg,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        val gradientBrush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF00E5FF).copy(alpha = alpha),
                Color(0xFFFF007F).copy(alpha = alpha)
            )
        )

        val strokeWidth = 2.5.dp.toPx()
        val halfStroke = strokeWidth / 2f

        drawRoundRect(
            brush = gradientBrush,
            topLeft = Offset(halfStroke, halfStroke),
            size = Size(size.width - strokeWidth, size.height - strokeWidth),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
            style = Stroke(width = strokeWidth)
        )
    }
}

/**
 * Helper to get alternating row colors for Pastel Rainbow Rows
 */
fun getPastelRowColor(index: Int): Color {
    val rowColors = listOf(
        Color(0xFFF8BBD0), // Row 0
        Color(0xFFE1BEE7), // Row 1
        Color(0xFFC5CAE9), // Row 2
        Color(0xFFB2EBF2), // Row 3
        Color(0xFFC8E6C9)  // Row 4
    )
    return rowColors[index % rowColors.size]
}

/**
 * 8. Rainbow Doodle Theme Canvas
 * - Pure White background
 * - Solid Magenta border (#A2007C)
 * - Thin horizontal notebook lines in alternating rainbow pastels
 */
@Composable
fun RainbowDoodleCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color.White.copy(alpha = alpha))

        // Magenta Border
        val borderStroke = 2.dp.toPx()
        val halfBorder = borderStroke / 2f
        drawRoundRect(
            color = Color(0xFFA2007C).copy(alpha = alpha),
            topLeft = Offset(halfBorder, halfBorder),
            size = Size(size.width - borderStroke, size.height - borderStroke),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
            style = Stroke(width = borderStroke)
        )

        // Thin horizontal notebook lines in rainbow colors
        val lineColors = listOf(
            Color(0xFFFF80AB), // Pink
            Color(0xFF80DEEA), // Teal/Cyan
            Color(0xFFFFE082), // Yellow
            Color(0xFFB39DDB), // Purple
            Color(0xFF81C784)  // Green
        )
        val lineSpacing = 24.dp.toPx()
        var y = 32.dp.toPx()
        var colorIdx = 0

        while (y < size.height - 12.dp.toPx()) {
            drawLine(
                color = lineColors[colorIdx % lineColors.size].copy(alpha = alpha * 0.7f),
                start = Offset(12.dp.toPx(), y),
                end = Offset(size.width - 12.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
            y += lineSpacing
            colorIdx++
        }
    }
}

/**
 * 9. Blue Oil Paint Theme Canvas
 * - Deep Cobalt Blue (#0038A8) background
 * - Warm Ochre Yellow (#F4C430) artistic brush stroke accents
 */
@Composable
fun BlueOilPaintCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cobaltBg = Color(0xFF0038A8).copy(alpha = alpha)
        val ochreYellow = Color(0xFFF4C430).copy(alpha = alpha)

        drawRoundRect(
            color = cobaltBg,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        // Warm Ochre brush stroke arc at top right
        val path = Path().apply {
            moveTo(size.width - 60.dp.toPx(), 0f)
            cubicTo(
                size.width - 40.dp.toPx(), 20.dp.toPx(),
                size.width - 10.dp.toPx(), 15.dp.toPx(),
                size.width, 35.dp.toPx()
            )
            lineTo(size.width, 0f)
            close()
        }
        drawPath(path = path, color = ochreYellow.copy(alpha = alpha * 0.85f))

        // Bottom left subtle brush stroke accent
        drawCircle(
            color = ochreYellow.copy(alpha = alpha * 0.25f),
            radius = 18.dp.toPx(),
            center = Offset(18.dp.toPx(), size.height - 18.dp.toPx())
        )
    }
}

/**
 * 10. Lime Highlighter Theme Canvas
 * - Minimalist white background
 * - Neon Lime Green (#B5FF39) header highlight banner
 * - Soft light-gray horizontal lines
 */
@Composable
fun LimeHighlighterCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color.White.copy(alpha = alpha))

        // Neon Lime Green header highlight rectangle
        val highlightHeight = 30.dp.toPx()
        drawRect(
            color = Color(0xFFB5FF39).copy(alpha = alpha * 0.85f),
            topLeft = Offset(12.dp.toPx(), 10.dp.toPx()),
            size = Size(size.width - 24.dp.toPx(), highlightHeight)
        )

        // Soft light-gray horizontal lines
        val lineSpacing = 26.dp.toPx()
        val lineGray = Color(0xFFE0E0E0).copy(alpha = alpha)
        var y = 50.dp.toPx()

        while (y < size.height) {
            drawLine(
                color = lineGray,
                start = Offset(12.dp.toPx(), y),
                end = Offset(size.width - 12.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
            y += lineSpacing
        }
    }
}

/**
 * 11. Cozy Latte Theme Canvas
 * - Warm cafe cream canvas (#FAF0E6)
 * - Espresso brown border (#4A2C2A)
 * - Coffee cup & steam accent
 */
@Composable
fun CozyLatteCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val creamBg = Color(0xFFFAF0E6).copy(alpha = alpha)
        val espressoBorder = Color(0xFF4A2C2A).copy(alpha = alpha)

        drawRoundRect(
            color = creamBg,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        val borderStroke = 2.dp.toPx()
        val halfBorder = borderStroke / 2f
        drawRoundRect(
            color = espressoBorder,
            topLeft = Offset(halfBorder, halfBorder),
            size = Size(size.width - borderStroke, size.height - borderStroke),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
            style = Stroke(width = borderStroke)
        )

        // Latte art steam curve top right
        val steamColor = Color(0xFF8D6E63).copy(alpha = alpha * 0.6f)
        val steamPath = Path().apply {
            moveTo(size.width - 28.dp.toPx(), 28.dp.toPx())
            cubicTo(
                size.width - 22.dp.toPx(), 20.dp.toPx(),
                size.width - 32.dp.toPx(), 14.dp.toPx(),
                size.width - 24.dp.toPx(), 8.dp.toPx()
            )
        }
        drawPath(path = steamPath, color = steamColor, style = Stroke(width = 2.dp.toPx()))
    }
}

/**
 * 12. Terracotta Arch Theme Canvas
 * - Muted Sand / Linen background (#F4EBE1)
 * - Terracotta Clay Arch (#C85A32)
 * - Muted Sage Green (#8A9A86) accent shape
 */
@Composable
fun TerracottaArchCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val sandBg = Color(0xFFF4EBE1).copy(alpha = alpha)
        val terracottaColor = Color(0xFFC85A32).copy(alpha = alpha * 0.85f)
        val sageColor = Color(0xFF8A9A86).copy(alpha = alpha * 0.85f)

        drawRoundRect(
            color = sandBg,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        // Terracotta Arch at top right
        val archWidth = 36.dp.toPx()
        val archHeight = 44.dp.toPx()
        val archLeft = size.width - archWidth - 12.dp.toPx()
        val archTop = 8.dp.toPx()

        val archPath = Path().apply {
            moveTo(archLeft, archTop + archHeight)
            lineTo(archLeft, archTop + archWidth / 2)
            arcTo(
                rect = Rect(archLeft, archTop, archLeft + archWidth, archTop + archWidth),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(archLeft + archWidth, archTop + archHeight)
            close()
        }
        drawPath(path = archPath, color = terracottaColor)

        // Muted Sage circle accent at bottom left
        drawCircle(
            color = sageColor,
            radius = 12.dp.toPx(),
            center = Offset(24.dp.toPx(), size.height - 24.dp.toPx())
        )
    }
}

/**
 * 13. Blossom Checklist Theme Canvas
 * - Soft Blossom Cream (#FFF5F6) background
 * - Sakura 5-petal flower graphics (#FFB7C5)
 * - Floral green leaf accents (#90B77D)
 */
@Composable
fun BlossomChecklistCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val blossomBg = Color(0xFFFFF5F6).copy(alpha = alpha)
        val sakuraPetal = Color(0xFFFFB7C5).copy(alpha = alpha)
        val sakuraCenter = Color(0xFFE91E63).copy(alpha = alpha)
        val leafGreen = Color(0xFF90B77D).copy(alpha = alpha)

        drawRoundRect(
            color = blossomBg,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        // Top right Sakura flower
        val centerX = size.width - 24.dp.toPx()
        val centerY = 24.dp.toPx()
        val petalR = 5.5.dp.toPx()

        for (i in 0 until 5) {
            val angle = Math.toRadians(i * 72.0)
            val px = (centerX + 7.dp.toPx() * Math.cos(angle)).toFloat()
            val py = (centerY + 7.dp.toPx() * Math.sin(angle)).toFloat()
            drawCircle(color = sakuraPetal, radius = petalR, center = Offset(px, py))
        }
        drawCircle(color = sakuraCenter, radius = 3.5.dp.toPx(), center = Offset(centerX, centerY))

        // Small leaf accent next to flower
        drawCircle(
            color = leafGreen,
            radius = 4.dp.toPx(),
            center = Offset(centerX - 16.dp.toPx(), centerY + 4.dp.toPx())
        )
    }
}

/**
 * 14. Kawaii Pink Motivation Theme Canvas
 * - Pastel Strawberry Pink background (#FFE4E1)
 * - Bright Kawaii Pink border (#FF69B4)
 * - Sparkle 4-point star graphics in gold (#FFD700)
 */
@Composable
fun KawaiiPinkCanvas(alpha: Float = 1.0f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val pinkBg = Color(0xFFFFE4E1).copy(alpha = alpha)
        val brightPink = Color(0xFFFF69B4).copy(alpha = alpha)
        val goldSparkle = Color(0xFFFFD700).copy(alpha = alpha)

        drawRoundRect(
            color = pinkBg,
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )

        // Bright Kawaii Pink Border
        val borderStroke = 2.dp.toPx()
        val halfBorder = borderStroke / 2f
        drawRoundRect(
            color = brightPink,
            topLeft = Offset(halfBorder, halfBorder),
            size = Size(size.width - borderStroke, size.height - borderStroke),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
            style = Stroke(width = borderStroke)
        )

        // Sparkle 4-point star drawing helper at top right
        val starCenterX = size.width - 26.dp.toPx()
        val starCenterY = 24.dp.toPx()
        val starSize = 8.dp.toPx()

        val starPath = Path().apply {
            moveTo(starCenterX, starCenterY - starSize)
            quadraticTo(starCenterX, starCenterY, starCenterX + starSize, starCenterY)
            quadraticTo(starCenterX, starCenterY, starCenterX, starCenterY + starSize)
            quadraticTo(starCenterX, starCenterY, starCenterX - starSize, starCenterY)
            quadraticTo(starCenterX, starCenterY, starCenterX, starCenterY - starSize)
            close()
        }
        drawPath(path = starPath, color = goldSparkle)
    }
}
