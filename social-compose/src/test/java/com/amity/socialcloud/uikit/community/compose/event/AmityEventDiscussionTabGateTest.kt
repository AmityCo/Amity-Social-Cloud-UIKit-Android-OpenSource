package com.amity.socialcloud.uikit.community.compose.event

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithContentDescription
import com.amity.socialcloud.uikit.community.compose.event.detail.EventTabRow
import org.junit.Test

/** PDT-5567: the Discussion tab is a post feed, and goes with Post. */
class AmityEventDiscussionTabGateTest : EventModuleGateTestBase() {

    @Test
    fun `with Post withheld the tab row keeps only the About tab`() {
        withhold("post")
        composeTestRule.setContent {
            EventTabRow(selectedIndex = 0, onTabSelected = {})
        }
        composeTestRule.onAllNodesWithContentDescription("Discussion").assertCountEquals(0)
        composeTestRule.onAllNodesWithContentDescription("About").assertCountEquals(1)
    }

    @Test
    fun `with Post available the tab row has both tabs`() {
        composeTestRule.setContent {
            EventTabRow(selectedIndex = 0, onTabSelected = {})
        }
        composeTestRule.onAllNodesWithContentDescription("Discussion").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("About").assertCountEquals(1)
    }
}
