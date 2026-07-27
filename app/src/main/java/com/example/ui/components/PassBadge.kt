package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun PassBadge(
    isPassActive: Boolean,
    remainingMillis: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hours = (remainingMillis / (1000 * 60 * 60)).toInt()
    val minutes = ((remainingMillis / (1000 * 60)) % 60).toInt()
    val seconds = ((remainingMillis / 1000) % 60).toInt()

    val badgeText = if (isPassActive) {
        if (hours > 0) {
            String.format(Locale.getDefault(), "24h Pass: %dh %dm", hours, minutes)
        } else {
            String.format(Locale.getDefault(), "24h Pass: %dm %ds", minutes, seconds)
        }
    } else {
        "24h Pass Expired • Get Pass"
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isPassActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
        label = "passBadgeBg"
    )

    val textColor = if (isPassActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        shadowElevation = 2.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = if (isPassActive) Icons.Default.Star else Icons.Default.CardGiftcard,
                contentDescription = "Pass Status",
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = badgeText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = textColor
            )
        }
    }
}
