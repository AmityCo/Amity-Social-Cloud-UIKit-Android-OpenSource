package com.amity.socialcloud.uikit.community.compose.discoverywidget.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.common.compose.R as CommonR

/**
 * The 32dp carousel paging button. State lives in the glyph tint only — the fill stays Base/Shade4
 * whether the button is enabled or not.
 */
@Composable
fun AmityDiscoveryWidgetNavButton(
    modifier: Modifier = Modifier,
    back: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(AmityTheme.colors.baseShade4)
            .clickableWithoutRipple(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(
                id = if (back) CommonR.drawable.amity_ic_chevron_left
                else CommonR.drawable.amity_ic_chevron_right
            ),
            contentDescription = amitySocialString(
                if (back) "amity_social_button_previous" else "amity_social_button_next"
            ),
            tint = if (enabled) AmityTheme.colors.base else AmityTheme.colors.baseShade3,
            modifier = Modifier.size(24.dp),
        )
    }
}
