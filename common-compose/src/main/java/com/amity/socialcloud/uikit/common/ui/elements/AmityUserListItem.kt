package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityUserListItem(
    modifier: Modifier = Modifier,
    user: AmityUser,
    showRightMenu: Boolean = true,
    onUserClick: (AmityUser) -> Unit = {},
    onRightMenuClick: (AmityUser) -> Unit = {},
    rightMenuContent: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick(user) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        AmityUserAvatarView(
            user = user,
            size = 40.dp,
        )

        Row(modifier = modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = modifier.width(IntrinsicSize.Max)
            ) {
                Text(
                    text = user.getDisplayName() ?: "",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    modifier = modifier
                        .weight(1f)
                )

                if (user.isBrand()) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp)
                            .testTag("user_view/brand_user_icon")
                    )
                }
            }
        }

        if (showRightMenu) {
            if (rightMenuContent == null) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_more_horiz),
                    contentDescription = "Action",
                    modifier = modifier
                        .size(24.dp)
                        .padding(start = 4.dp)
                        .clickableWithoutRipple {
                            onRightMenuClick(user)
                        },
                    tint = AmityTheme.colors.base,
                )
            } else {
                rightMenuContent()
            }
        }
    }
}