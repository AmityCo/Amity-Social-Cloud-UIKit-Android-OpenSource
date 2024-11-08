package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityRoundCheckbox(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    checkedColor: Color = AmityTheme.colors.primary,
    onValueChange: (Boolean) -> Unit,
) {
    val density = LocalDensity.current
    val duration = 200

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .toggleable(
                enabled = enabled,
                value = isChecked,
                role = Role.Checkbox,
                onValueChange = onValueChange
            )
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(size)
                .border(
                    width = 2.dp,
                    color = if (isChecked) Color.Transparent else AmityTheme.colors.baseShade2,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = isChecked,
                enter = slideInHorizontally(animationSpec = tween(duration)) {
                    with(density) { (size.value * -0.5).dp.roundToPx() }
                } + expandHorizontally(
                    expandFrom = Alignment.Start,
                    animationSpec = tween(duration)
                ),
                exit = fadeOut()
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = checkedColor,
                )
            }
        }
    }
}