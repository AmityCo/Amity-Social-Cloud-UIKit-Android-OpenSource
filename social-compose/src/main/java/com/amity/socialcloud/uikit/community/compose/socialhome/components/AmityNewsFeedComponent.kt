package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope

@Composable
fun AmityNewsFeedComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "newsfeed"
    ) {
        AmityGlobalFeedComponent(
            modifier = modifier.testTag(getAccessibilityId()),
            pageScope = pageScope
        )
    }
}