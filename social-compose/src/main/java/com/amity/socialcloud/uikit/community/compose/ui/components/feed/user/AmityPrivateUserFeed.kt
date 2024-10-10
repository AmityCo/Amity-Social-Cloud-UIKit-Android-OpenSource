package com.amity.socialcloud.uikit.community.compose.ui.components.feed.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText


@Composable
fun AmityPrivateUserFeed(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    feedType: AmityUserFeedType,
) {
    val elementId = remember(feedType) {
        when (feedType) {
            AmityUserFeedType.POST -> "private_user_feed"
            AmityUserFeedType.IMAGE -> "private_user_image_feed"
            AmityUserFeedType.VIDEO -> "private_user_video_feed"
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = elementId
        ) {
            Icon(
                painter = painterResource(getConfig().getIcon()),
                tint = AmityTheme.colors.baseShade4,
                contentDescription = "empty feed icon",
                modifier = modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = getConfig().getText(),
                style = AmityTheme.typography.title.copy(
                    color = AmityTheme.colors.baseShade3,
                ),
            )
        }

        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = elementId + "_info"
        ) {
            Text(
                text = getConfig().getText(),
                style = AmityTheme.typography.caption.copy(
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.colors.baseShade3,
                ),
            )
        }
    }
}