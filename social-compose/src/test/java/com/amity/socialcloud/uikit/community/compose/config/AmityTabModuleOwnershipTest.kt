package com.amity.socialcloud.uikit.community.compose.config

import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageTab
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageTab
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageTab
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * A tab row that mixes modules is where switching a module off went wrong twice:
 * the Events tab and the Media tab both survived their module on Community
 * Profile, because the row was four hardcoded columns with no ids and the page
 * switched on an integer.
 *
 * The rows are now built from a list, and `visibleCommunityProfileTabs`,
 * `visibleUserProfileTabs` and `visibleSocialHomeTabs` name one element id per
 * tab. Those functions are @Composable and cannot be called here, so this test
 * guards the half that actually breaks: adding a tab to the enum without adding
 * a line to the function. The maps below must list every entry, and a new
 * constant fails the build until somebody decides which module owns it.
 */
class AmityTabModuleOwnershipTest {

    private val communityProfileTabIds = mapOf(
        AmityCommunityProfilePageTab.FEED to "community_feed_tab_button",
        AmityCommunityProfilePageTab.PIN to "community_pin_tab_button",
        AmityCommunityProfilePageTab.EVENTS to "event_button",
        AmityCommunityProfilePageTab.MEDIA to "community_media_tab_button",
    )

    private val userProfileTabIds = mapOf(
        AmityUserProfilePageTab.FEED to "user_feed_tab_button",
        AmityUserProfilePageTab.MEDIA to "user_image_feed_tab_button",
    )

    private val socialHomeTabIds = mapOf(
        AmitySocialHomePageTab.FOR_YOU to "for_you_button",
        AmitySocialHomePageTab.FOLLOWING to "following_button",
        AmitySocialHomePageTab.COMMUNITIES to "communities_button",
        AmitySocialHomePageTab.EVENTS to "events_button",
        AmitySocialHomePageTab.CLIPS to "clipsfeed_button",
    )

    @Test
    fun everyCommunityProfileTabIsAccountedFor() {
        assertEquals(
            AmityCommunityProfilePageTab.entries.toSet(),
            communityProfileTabIds.keys,
        )
    }

    @Test
    fun everyUserProfileTabIsAccountedFor() {
        assertEquals(
            AmityUserProfilePageTab.entries.toSet(),
            userProfileTabIds.keys,
        )
    }

    @Test
    fun everySocialHomeTabIsAccountedFor() {
        assertEquals(
            AmitySocialHomePageTab.entries.toSet(),
            socialHomeTabIds.keys,
        )
    }

    @Test
    fun noTwoTabsInOneRowShareAnId() {
        // Two tabs on the same id means one of them can never be hidden alone,
        // which is how a mixed-module row goes wrong in the other direction.
        for (ids in listOf(communityProfileTabIds, userProfileTabIds, socialHomeTabIds)) {
            assertEquals(ids.size, ids.values.toSet().size)
        }
    }
}
