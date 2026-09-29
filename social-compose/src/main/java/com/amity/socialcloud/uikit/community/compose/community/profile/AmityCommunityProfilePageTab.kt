package com.amity.socialcloud.uikit.community.compose.community.profile

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.isElementExcluded
import com.amity.socialcloud.uikit.common.R as CommonR

/**
 * The tabs of the community profile, as names rather than positions.
 *
 * The row used to be four hardcoded columns and the page switched on the
 * integer 0..3. Events sits at 2 and Media at 3, so removing either tab meant
 * renumbering the ones after it — and nothing did, which is why both stayed on
 * screen after their module was switched off.
 */
enum class AmityCommunityProfilePageTab(val iconRes: Int) {
    FEED(CommonR.drawable.amity_ic_community_feed),
    PIN(CommonR.drawable.amity_ic_community_pin),
    EVENTS(com.amity.socialcloud.uikit.common.R.drawable.amity_ic_create_event),
    MEDIA(CommonR.drawable.amity_ic_community_media_tab),
}

/**
 * Which tabs this build shows, in order.
 *
 * One id per tab, spelled out, because these are the ids the module tables own:
 * Post owns the feed, pinned and media tabs, and Events the events tab. Feed owns
 * none of them - it is the global feed and For You only - so switching it off
 * leaves this row as it is. `AmityTabModuleOwnershipTest` fails if a tab is added
 * without a line here, which is the only thing standing between a new tab and
 * the bug this replaced.
 */
@Composable
fun visibleCommunityProfileTabs(
    pageScope: AmityComposePageScope?,
): List<AmityCommunityProfilePageTab> = listOfNotNull(
    AmityCommunityProfilePageTab.FEED
        .takeUnless { pageScope.isElementExcluded("community_feed_tab_button") },
    AmityCommunityProfilePageTab.PIN
        .takeUnless { pageScope.isElementExcluded("community_pin_tab_button") },
    AmityCommunityProfilePageTab.EVENTS
        .takeUnless { pageScope.isElementExcluded("event_button") },
    AmityCommunityProfilePageTab.MEDIA
        .takeUnless { pageScope.isElementExcluded("community_media_tab_button") },
)
