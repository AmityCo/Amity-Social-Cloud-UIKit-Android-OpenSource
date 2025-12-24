package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText

@Composable
fun AmityCreateCommunityCard(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    onClick: () -> Unit,
) {
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = "create_community_card"
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(80.dp)
                .clickableWithoutRipple {
                    onClick()
                }
                .testTag(getAccessibilityId())
        ) {
            // Plus icon box
            AmityBaseElement(
                pageScope = pageScope,
                componentScope = componentScope,
                elementId = "create_community_icon"
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .testTag(getAccessibilityId()),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = getConfig().getIcon()),
                        contentDescription = "Create community",
                        tint = AmityTheme.colors.base,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // "Create community" text
            AmityBaseElement(
                pageScope = pageScope,
                componentScope = componentScope,
                elementId = "create_community_text"
            ) {
                Text(
                    text = getConfig().getText(),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.testTag(getAccessibilityId())
                )
            }
        }
    }
}
