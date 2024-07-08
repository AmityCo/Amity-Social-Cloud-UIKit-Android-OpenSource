package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

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
        AmityAvatarView(
            modifier = modifier,
            size = 40.dp,
            avatarUrl = user.getAvatar()?.getUrl(),
        )

        Text(
            text = user.getDisplayName() ?: "",
            style = AmityTheme.typography.body.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}