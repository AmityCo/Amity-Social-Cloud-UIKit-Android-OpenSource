package com.amity.socialcloud.uikit.community.compose.socialhome

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.scope.isElementExcluded

enum class AmitySocialHomePageTab {
    FOR_YOU,
    FOLLOWING,
    COMMUNITIES,
    EVENTS,
    CLIPS;
}

/**
 * Which Social Home tabs this build shows, in order.
 *
 * One id per tab, spelled out, because these are the ids the module tables own:
 * Feed owns For You, Community owns Communities, Events the events tab and
 * Post the clips tab. `AmityTabModuleOwnershipTest` fails if a tab is added
 * here without a line.
 *
 * Following is the one tab asked twice. Its chip, `following_button`, belongs
 * to Post, but the tab is the global feed (`newsfeed`), which Feed owns — so
 * with Feed off and Post on the chip survived over a body that never drew, and
 * with For You gone it was the landing tab (PDT-5571). `newsfeed_button` is the
 * chip's v1 id and Feed's, which is how Web gates the same tab; the tab stands
 * only while neither is withheld.
 */
@Composable
fun visibleSocialHomeTabs(
    pageScope: AmityComposePageScope? = null,
    isSignedInUser: Boolean = true,
): List<AmitySocialHomePageTab> = listOfNotNull(
    AmitySocialHomePageTab.FOR_YOU
        .takeIf { isSignedInUser }
        ?.takeUnless { pageScope.isElementExcluded("for_you_button") },
    AmitySocialHomePageTab.FOLLOWING
        .takeIf { isSignedInUser }
        ?.takeUnless { pageScope.isElementExcluded("following_button") }
        ?.takeUnless { pageScope.isElementExcluded("newsfeed_button") },
    AmitySocialHomePageTab.COMMUNITIES
        .takeUnless { pageScope.isElementExcluded("communities_button") },
    AmitySocialHomePageTab.EVENTS
        .takeUnless { pageScope.isElementExcluded("events_button") },
    AmitySocialHomePageTab.CLIPS
        .takeUnless { pageScope.isElementExcluded("clipsfeed_button") },
)
