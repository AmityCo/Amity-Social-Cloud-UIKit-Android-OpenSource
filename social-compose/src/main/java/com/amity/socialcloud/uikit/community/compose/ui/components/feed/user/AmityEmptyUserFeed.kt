package com.amity.socialcloud.uikit.community.compose.ui.components.feed.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText


@Composable
fun AmityEmptyUserFeed(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    feedType: AmityUserFeedType,
) {
    val elementId = remember(feedType) {
        when (feedType) {
            AmityUserFeedType.POST -> "empty_user_feed"
            AmityUserFeedType.IMAGE -> "empty_user_image_feed"
            AmityUserFeedType.VIDEO -> "empty_user_video_feed"
        }
    }

    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = elementId,
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = getConfig().getIcon()),
                tint = AmityTheme.colors.baseShade4,
                contentDescription = "empty feed icon"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = getConfig().getText(),
                style = AmityTheme.typography.titleLegacy.copy(
                    color = AmityTheme.colors.baseShade3,
                ),
            )
        }
    }
}