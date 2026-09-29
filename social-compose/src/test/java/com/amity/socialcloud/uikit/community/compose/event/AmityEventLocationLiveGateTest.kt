package com.amity.socialcloud.uikit.community.compose.event

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performSemanticsAction
import com.amity.socialcloud.sdk.model.social.event.AmityEventType
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.AmityLocationBottomSheet
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.AmityLocationData
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.EventPlatform
import org.junit.Assert.assertEquals
import org.junit.Test

/** PDT-5614: a Virtual event's Live stream platform goes with Live. */
class AmityEventLocationLiveGateTest : EventModuleGateTestBase() {

    private fun showVirtualLocationSheet(
        initialData: AmityLocationData = AmityLocationData(eventType = AmityEventType.VIRTUAL),
        onDone: (AmityLocationData) -> Unit = {},
    ) = composeTestRule.setContent {
        AmityLocationBottomSheet(
            shouldShow = true,
            initialData = initialData,
            onDismiss = {},
            onDone = onDone,
        )
    }

    // The semantics action, not a touch: Robolectric does not route injected
    // touches into the sheet's dialog window, so a tap would pass vacuously.
    private fun tapDone() {
        composeTestRule.onNodeWithText("Done").performSemanticsAction(SemanticsActions.OnClick)
        composeTestRule.waitForIdle()
    }

    @Test
    fun `with Live withheld a virtual event offers only the external platform`() {
        withhold("live")
        showVirtualLocationSheet()
        composeTestRule.onAllNodesWithText("Live stream").assertCountEquals(0)
        composeTestRule.onAllNodesWithContentDescription("Live stream").assertCountEquals(0)
        composeTestRule.onAllNodesWithText(LIVE_DESCRIPTION).assertCountEquals(0)
        composeTestRule.onAllNodesWithText(EXTERNAL_DESCRIPTION).assertCountEquals(1)
    }

    @Test
    fun `with Live withheld Done cannot save a live stream location`() {
        withhold("live")
        val saved = mutableListOf<AmityLocationData>()
        showVirtualLocationSheet(onDone = { saved += it })
        // The only platform left is External, which needs a link before Done
        // does anything. Defaulting to Live stream made Done live straight away.
        tapDone()
        assertEquals(emptyList<AmityLocationData>(), saved)
    }

    @Test
    fun `with Live withheld an existing live stream event reopens on External`() {
        // Editing an event saved as Live stream: the option it was saved with is
        // gone, so the sheet must not reopen on it and save it back unchanged.
        withhold("live")
        val saved = mutableListOf<AmityLocationData>()
        showVirtualLocationSheet(
            initialData = AmityLocationData(
                eventType = AmityEventType.VIRTUAL,
                platform = EventPlatform.LIVE_STREAM,
            ),
            onDone = { saved += it },
        )
        composeTestRule.onAllNodesWithText(LIVE_DESCRIPTION).assertCountEquals(0)
        tapDone()
        assertEquals(emptyList<AmityLocationData>(), saved)
    }

    @Test
    fun `with Live available Done saves the default live stream location`() {
        // The control for the Done test above: the same action does save when
        // Live is available, so an empty list there is the gate, not a dead click.
        val saved = mutableListOf<AmityLocationData>()
        showVirtualLocationSheet(onDone = { saved += it })
        tapDone()
        assertEquals(listOf(EventPlatform.LIVE_STREAM), saved.map { it.platform })
    }

    @Test
    fun `with Live available both platforms are offered`() {
        showVirtualLocationSheet()
        composeTestRule.onAllNodesWithText("Live stream").assertCountEquals(1)
        composeTestRule.onAllNodesWithText(LIVE_DESCRIPTION).assertCountEquals(1)
        composeTestRule.onAllNodesWithText(EXTERNAL_DESCRIPTION).assertCountEquals(1)
    }

    private companion object {
        // The two options' captions, which appear nowhere else on the sheet.
        const val LIVE_DESCRIPTION = "Attendees join the live stream directly on the app or website."
        const val EXTERNAL_DESCRIPTION = "Users will join the event on an external platform."
    }
}
