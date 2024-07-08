package com.amity.socialcloud.uikit.community.compose.post.composer.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon

@Composable
fun AmityPostAttachmentButton(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    elementId: String,
    onClick: () -> Unit = {},
) {
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = elementId,
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(
                    color = if (isSystemInDarkTheme()) {
                        AmityTheme.colors.baseShade3
                    } else {
                        AmityTheme.colors.baseShade4
                    },
                )
                .size(32.dp)
                .clickableWithoutRipple(onClick = onClick)
                .testTag(getAccessibilityId()),
        ) {
            Icon(
                painter = painterResource(id = getConfig().getIcon()),
                contentDescription = null,
                tint = AmityTheme.colors.base,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
            )
        }
    }
}