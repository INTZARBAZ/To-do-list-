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
