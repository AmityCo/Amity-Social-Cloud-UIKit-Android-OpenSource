package com.amity.socialcloud.uikit.community.compose.event

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import com.amity.socialcloud.uikit.community.compose.event.detail.elements.AmityEventPostCreationSuccessBottomSheet
import org.junit.Test

/** PDT-5617: the event-created sheet's post-to-feed prompt goes with Post. */
class AmityEventCreatedSheetGateTest : EventModuleGateTestBase() {

    private fun showSheet() = composeTestRule.setContent {
        AmityEventPostCreationSuccessBottomSheet(
            shouldShow = true,
            onDismiss = {},
            onPostToFeed = {},
        )
    }

    @Test
    fun `with Post withheld the success sheet keeps only its title`() {
        withhold("post")
        showSheet()
        composeTestRule.onAllNodesWithText("Your event was created successfully").assertCountEquals(1)
        composeTestRule.onAllNodesWithText("Post to feed").assertCountEquals(0)
        composeTestRule.onAllNodesWithText("Maybe later").assertCountEquals(0)
        composeTestRule.onAllNodesWithText("find and join", substring = true).assertCountEquals(0)
    }

    @Test
    fun `with Post available the success sheet offers post to feed`() {
        showSheet()
        composeTestRule.onAllNodesWithText("Your event was created successfully").assertCountEquals(1)
        composeTestRule.onAllNodesWithText("find and join", substring = true).assertCountEquals(1)
        composeTestRule.onAllNodesWithText("Post to feed").assertCountEquals(1)
        composeTestRule.onAllNodesWithText("Maybe later").assertCountEquals(1)
    }
}
