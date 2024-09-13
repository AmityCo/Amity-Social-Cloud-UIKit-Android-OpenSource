package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityUserView(
    modifier: Modifier = Modifier,
    user: AmityUser,
    onClick: (AmityUser) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickableWithoutRipple {
                onClick(user)
            }
    ) {
        AmityUserAvatarView(
            modifier = modifier,
            size = 40.dp,
            user = user,
        )

        Row (
            modifier = Modifier.weight(1f)
        ){
            Text(
                text = user.getDisplayName() ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.body.copy(
                    fontWeight = FontWeight.SemiBold
                ),

            )

            val isBrandUser = user.isBrand()
            if (isBrandUser) {
                val badge = R.drawable.amity_ic_brand_badge
                Image(
                    painter = painterResource(id = badge),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 4.dp)
                        .testTag("user_view/brand_user_icon")
                )
            }
        }
    }
}