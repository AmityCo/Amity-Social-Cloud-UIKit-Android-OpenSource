package com.amity.socialcloud.uikit.sample.networklog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * One line of the timeline. Fixed height so the list can virtualize at the volumes a
 * livestream produces. Monospace is functional here — timestamps, statuses and byte counts
 * are read as columns.
 *
 * Spec: front-end-tech-specs/UIKIT/elements/AmityNetworkLogRow/v1.md
 */
@Composable
fun AmityNetworkLogRow(
    entry: AmityNetworkLogEntry,
    isExpanded: Boolean = false,
    indent: Boolean = false,
    onClick: () -> Unit = {},
) {
    val isGroup = entry.variant == AmityNetworkLogRowVariant.GROUP
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(
                if (isGroup) AmityTheme.colors.backgroundShade1 else AmityTheme.colors.background
            )
            .clickable { onClick() }
            .padding(start = if (indent) 26.dp else 12.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = entry.timestampLabel.take(8),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = AmityTheme.colors.baseShade2,
            modifier = Modifier.width(66.dp),
            maxLines = 1,
        )
        TypeChip(entry.chipLabel)
        Text(
            text = if (isGroup) {
                (if (isExpanded) "▾ " else "▸ ") + entry.primaryLabel
            } else entry.primaryLabel,
            fontSize = 12.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isGroup) FontWeight.SemiBold else FontWeight.Normal,
            color = AmityTheme.colors.base,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.dp),
        )
        entry.statusLabel?.let {
            Text(
                text = it,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = AmityNetworkLogPalette.status(entry.statusClass),
                textAlign = TextAlign.End,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun TypeChip(label: String) {
    val colors = AmityNetworkLogPalette.chip(label)
    Box(
        modifier = Modifier
            .width(50.dp)
            .clip(RoundedCornerShape(3.dp))
            .then(
                if (colors.outlined) {
                    Modifier.border(1.dp, colors.fg, RoundedCornerShape(3.dp))
                } else {
                    Modifier.background(colors.bg)
                }
            )
            .padding(vertical = 3.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = colors.fg,
            maxLines = 1,
        )
    }
}
