package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EventWidgetModel
import com.example.data.model.PastelPaletteType
import com.example.ui.components.ThemeBackgroundCanvas
import com.example.ui.components.getPastelRowColor
import com.example.widget.AccentSticker
import com.example.widget.PatternType
import com.example.widget.ThemeTier
import com.example.widget.WidgetStyleConfig
import com.example.widget.WidgetThemeCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetCustomizerScreen(
    currentWidgetTheme: PastelPaletteType,
    currentWidgetOpacity: Float,
    isPassActive: Boolean,
    eventWidgetModel: EventWidgetModel = EventWidgetModel(),
    onApplyWidgetTheme: (PastelPaletteType, Float) -> Unit,
    onSaveEventWidgetData: (EventWidgetModel) -> Unit = {},
    onTriggerAdDialog: () -> Unit,
    onTriggerAdDialogWithHours: (Int) -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val catalogPresets = WidgetThemeCatalog.presetCatalog

    var selectedConfigPreview by remember(currentWidgetTheme) {
        mutableStateOf(WidgetThemeCatalog.getConfigForPalette(currentWidgetTheme))
    }
    var opacityPreview by remember(currentWidgetOpacity) { mutableFloatStateOf(currentWidgetOpacity) }

    val previewTitleColor = remember(selectedConfigPreview) {
        try { Color(android.graphics.Color.parseColor(selectedConfigPreview.titleColorHex)) } catch (e: Exception) { Color(0xFF1C1B1F) }
    }

    val previewSubtextColor = remember(selectedConfigPreview) {
        try { Color(android.graphics.Color.parseColor(selectedConfigPreview.subtextColorHex)) } catch (e: Exception) { Color(0xFF79747E) }
    }

    val previewItemBgColor = remember(selectedConfigPreview) {
        try { Color(android.graphics.Color.parseColor(selectedConfigPreview.itemBgColorHex)) } catch (e: Exception) { Color.White }
    }

    val previewActiveCheckColor = remember(selectedConfigPreview) {
        try { Color(android.graphics.Color.parseColor(selectedConfigPreview.activeCheckColorHex)) } catch (e: Exception) { Color(0xFF8E7CC3) }
    }

    val previewInactiveCheckColor = remember(selectedConfigPreview) {
        try { Color(android.graphics.Color.parseColor(selectedConfigPreview.inactiveCheckColorHex)) } catch (e: Exception) { Color(0xFF1D192B) }
    }

    val previewActiveTextColor = remember(selectedConfigPreview) {
        try { Color(android.graphics.Color.parseColor(selectedConfigPreview.activeTextColorHex)) } catch (e: Exception) { Color(0xFF1C1B1F) }
    }

    val previewCompletedTextColor = remember(selectedConfigPreview) {
        try { Color(android.graphics.Color.parseColor(selectedConfigPreview.completedTextColorHex)) } catch (e: Exception) { Color(0xFF79747E) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Home Screen Widget Customizer",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_widget_customizer")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            onApplyWidgetTheme(selectedConfigPreview.paletteType, opacityPreview)
                            Toast.makeText(context, "Widget theme applied to home screen!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_apply_widget_theme")
                    ) {
                        Icon(Icons.Default.Widgets, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Apply Widget Theme", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // TOP SECTION: Home Screen Mockup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE8EAF6),
                                Color(0xFFE1BEE7),
                                Color(0xFFFFF9C4),
                                Color(0xFFE0F2F1)
                            )
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(28.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Status Bar Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "9:41",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF37474F)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("5G  100%", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF37474F))
                        }
                    }

                    // Live To-Do Widget Preview
                    ThemeBackgroundCanvas(
                        config = selectedConfigPreview,
                        opacity = opacityPreview,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (selectedConfigPreview.borderColorHex != null) 2.dp else 1.dp,
                                color = try {
                                    Color(android.graphics.Color.parseColor(selectedConfigPreview.borderColorHex ?: selectedConfigPreview.titleColorHex))
                                } catch (e: Exception) {
                                    previewTitleColor.copy(alpha = 0.3f)
                                },
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Today's Checklist",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = previewTitleColor
                                    )
                                    val stickerEmoji = when (selectedConfigPreview.accentSticker) {
                                        AccentSticker.SUN -> " ☀️"
                                        AccentSticker.FLOWER -> " 🌸"
                                        AccentSticker.PIN -> " 📌"
                                        AccentSticker.NONE -> ""
                                    }
                                    if (stickerEmoji.isNotEmpty()) {
                                        Text(text = stickerEmoji, fontSize = 15.sp)
                                    }
                                }

                                Text(
                                    text = "2 Pending",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = previewSubtextColor
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Task Item 0
                            val item0Bg = if (selectedConfigPreview.patternType == PatternType.RAINBOW_ROWS) getPastelRowColor(0) else previewItemBgColor
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = item0Bg,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("○ ", fontWeight = FontWeight.Bold, color = previewInactiveCheckColor)
                                    Text("Have a glass of water 💧", fontSize = 12.sp, color = previewActiveTextColor)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Task Item 1
                            val item1Bg = if (selectedConfigPreview.patternType == PatternType.RAINBOW_ROWS) getPastelRowColor(1) else previewItemBgColor
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = item1Bg,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("○ ", fontWeight = FontWeight.Bold, color = previewInactiveCheckColor)
                                    Text("Morning jogging 🏃", fontSize = 12.sp, color = previewActiveTextColor)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Task Item 2
                            val item2Bg = if (selectedConfigPreview.patternType == PatternType.RAINBOW_ROWS) getPastelRowColor(2) else previewItemBgColor
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = item2Bg,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("✓ ", fontWeight = FontWeight.Bold, color = previewActiveCheckColor)
                                    Text("Have lunch with Jenny 🥗", fontSize = 12.sp, color = previewCompletedTextColor)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mock Home Screen App Icons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MockAppIcon(icon = Icons.Default.CameraAlt, label = "Camera", color = Color(0xFFE53935))
                        MockAppIcon(icon = Icons.Default.Message, label = "Chat", color = Color(0xFF43A047))
                        MockAppIcon(icon = Icons.Default.Image, label = "Photos", color = Color(0xFF1E88E5))
                        MockAppIcon(icon = Icons.Default.MusicNote, label = "Music", color = Color(0xFFFB8C00))
                    }
                }
            }

            // MIDDLE SECTION: Opacity Slider
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Background Opacity",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${(opacityPreview * 100).toInt()}%",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Slider(
                        value = opacityPreview,
                        onValueChange = { opacityPreview = it },
                        valueRange = 0.0f..1.0f,
                        steps = 19,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("slider_widget_opacity")
                    )
                }
            }

            // BOTTOM SECTION: Horizontal Carousel of 6 Widget Theme Thumbnails
            Text(
                text = "Core To-Do Themes (6 Presets)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("widget_theme_catalogue_grid")
            ) {
                items(catalogPresets) { config ->
                    val isSelected = selectedConfigPreview.id == config.id
                    val isLocked = !config.isFree && !isPassActive

                    val themePrimaryColor = remember(config.activeCheckColorHex) {
                        try { Color(android.graphics.Color.parseColor(config.activeCheckColorHex)) } catch (e: Exception) { Color(0xFF8E7CC3) }
                    }

                    val cardBackgroundModifier = when (config.tier) {
                        ThemeTier.FREE -> Modifier.background(MaterialTheme.colorScheme.surface)
                        ThemeTier.STANDARD_PREMIUM -> Modifier.background(themePrimaryColor.copy(alpha = 0.12f))
                        ThemeTier.GOLD_VIP -> Modifier.background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))
                            )
                        )
                    }

                    val borderStroke = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.5.dp else 1.dp,
                        color = if (isSelected) themePrimaryColor else MaterialTheme.colorScheme.outlineVariant
                    )

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = borderStroke,
                        modifier = Modifier
                            .width(160.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .then(cardBackgroundModifier)
                            .clickable {
                                if (isLocked) {
                                    val hoursToGrant = if (config.tier == ThemeTier.GOLD_VIP) 24 else 8
                                    onTriggerAdDialogWithHours(hoursToGrant)
                                } else {
                                    selectedConfigPreview = config
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(themePrimaryColor)
                                )

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = themePrimaryColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (isLocked) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = "Pass Required",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "PASS",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                } else if (config.isFree) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = "FREE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = config.name,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = config.description,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MockAppIcon(icon: ImageVector, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF37474F))
    }
}
