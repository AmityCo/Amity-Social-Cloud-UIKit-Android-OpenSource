package com.amity.socialcloud.uikit.community.compose.ui.components.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityColors
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@Composable
fun AmityFilledRadioIndicator(
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val unselectedBorderColor = if (enabled) AmityTheme.colors.baseShade3 else AmityTheme.colors.baseShade4
    val selectedBorderColor = if (enabled) AmityTheme.colors.primary else AmityTheme.colors.baseShade3
    val selectedBackgroundColor = if (enabled) amityColorWhite else AmityTheme.colors.baseShade2

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .then(
                if (selected) {
                    Modifier.background(selectedBorderColor)
                } else {
                    Modifier.border(
                        width = 2.dp,
                        color = unselectedBorderColor,
                        shape = CircleShape,
                    )
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(selectedBackgroundColor),
            )
        }
    }
}