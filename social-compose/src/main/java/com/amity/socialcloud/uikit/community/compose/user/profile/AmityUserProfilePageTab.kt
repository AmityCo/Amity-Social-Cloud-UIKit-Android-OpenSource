package com.amity.socialcloud.uikit.community.compose.user.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.isElementExcluded
import com.amity.socialcloud.uikit.common.R as CommonR

/**
 * The tabs of the user profile, as names rather than positions — the same
 * change [com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageTab]
 * makes, for the same reason.
 *
 * Both tabs belong to Feed, so with Feed off the row has nothing left to draw:
 * the caller renders no row, no divider and no filter, rather than an empty
 * strip above a filter that filters nothing.
 */
enum class AmityUserProfilePageTab(val iconRes: Int) {
    FEED(CommonR.drawable.amity_ic_community_feed),
    MEDIA(CommonR.drawable.amity_ic_community_media_tab),
}

@Composable
fun visibleUserProfileTabs(
    pageScope: AmityComposePageScope?,
): List<AmityUserProfilePageTab> = listOfNotNull(
    AmityUserProfilePageTab.FEED
        .takeUnless { pageScope.isElementExcluded("user_feed_tab_button") },
    AmityUserProfilePageTab.MEDIA
        .takeUnless { pageScope.isElementExcluded("user_image_feed_tab_button") },
)
