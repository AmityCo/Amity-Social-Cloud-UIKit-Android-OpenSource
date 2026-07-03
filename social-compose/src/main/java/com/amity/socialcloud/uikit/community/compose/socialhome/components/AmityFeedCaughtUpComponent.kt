package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityTypography
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialConfigString

/**
 * "Caught up" end-of-feed CTA for the For You feed.
 *
 * Spec: front-end-tech-specs/UIKIT/components/AmityFeedCaughtUpComponent/v1.md
 *
 * Rendered as the final cell of [AmityForYouFeedComponent] when the
 * live-collection signals hasMore=false (REQ-001a) OR when the first page
 * resolves empty (REQ-001b — For You has no separate empty state, REQ-007).
 *
 * Pure UI; no SDK calls. The CTA invokes [onSwitchRequested] which the parent
 * uses to switch the SocialHomePage active tab to Following (REQ-004).
 */
@Composable
fun AmityFeedCaughtUpComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onSwitchRequested: () -> Unit = {},
) {
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "amity_feed_caught_up_component"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .background(AmityTheme.colors.background)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = AmityTheme.colors.baseShade3,
                modifier = Modifier.size(40.dp)
            )
            AmityBaseElement(
                pageScope = pageScope,
                componentScope = getComponentScope(),
                elementId = "feed_caught_up_title"
            ) {
                Text(
                    text = amitySocialConfigString("amity_social_feed_caught_up_title"),
                    style = AmityTheme.typography.titleBold,
                    color = AmityTheme.colors.baseShade2,
                    textAlign = TextAlign.Center,
                    modifier = modifier.testTag(getAccessibilityId())
                )
            }
            AmityBaseElement(
                pageScope = pageScope,
                componentScope = getComponentScope(),
                elementId = "feed_caught_up_cta_button"
            ) {
                // REQ-005: tap target meets the 48dp Android min (Button default).
                // REQ-006: platform-default ripple via Button.
                TextButton(
                    onClick = onSwitchRequested
                ) {
                    Text(DefaultAmitySocialStringProvider.getInstance().getString("amity_social_feed_caught_up_cta"), style = AmityTheme.typography.bodyBold.copy(
                        color = AmityTheme.colors.primary
                    ))
                }
            }
        }
    }
}
