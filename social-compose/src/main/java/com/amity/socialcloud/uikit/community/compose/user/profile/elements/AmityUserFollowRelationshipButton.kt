package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowStatus
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText

@Composable
fun AmityUserFollowRelationshipButton(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    followStatus: AmityFollowStatus,
    onClick: (AmityFollowStatus) -> Unit,
) {
    val elementId = remember(followStatus) {
        when (followStatus) {
            AmityFollowStatus.PENDING -> "pending_user_button"
            AmityFollowStatus.ACCEPTED -> "following_user_button"
            AmityFollowStatus.BLOCKED -> "unblock_user_button"
            AmityFollowStatus.NONE -> "follow_user_button"
        }
    }
    val isOutlinedButtonStyle = remember(followStatus) {
        when (followStatus) {
            AmityFollowStatus.NONE -> false
            else -> true
        }
    }

    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = elementId,
    ) {
        if (isOutlinedButtonStyle) {
            OutlinedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, AmityTheme.colors.baseShade3),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                onClick = {
                    onClick(followStatus)
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(getConfig().getIcon()),
                        contentDescription = null,
                        tint = AmityTheme.colors.base,
                    )

                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }
            }
        } else {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.highlight,
                ),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                onClick = {
                    onClick(followStatus)
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(getConfig().getIcon()),
                        contentDescription = null,
                    )

                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }
            }
        }
    }
}