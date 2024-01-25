package com.amity.socialcloud.uikit.community.compose.story.view.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope

@Composable
fun AmityStorySegmentTimerElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    shouldPauseTimer: Boolean,
    shouldRestart: Boolean,
    totalSegments: Int,
    currentSegment: Int,
    duration: Long,
    moveToNextSegment: () -> Unit
) {
    AmityBaseElement(
        pageScope = pageScope,
        elementId = "progress_bar"
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
        ) {
            for (index in 0 until totalSegments) {
                AmityStorySingleSegmentTimerElement(
                    elementScope = getElementScope(),
                    modifier = modifier.weight(1f),
                    shouldStart = index == currentSegment,
                    shouldRestart = index == currentSegment && shouldRestart,
                    shouldPauseTimer = shouldPauseTimer,
                    isAlreadyFinished = index < currentSegment,
                    duration = duration,
                ) {
                    moveToNextSegment()
                }
            }
        }
    }
}

@Preview
@Composable
fun AmityStorySegmentTimerElementPreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityBasePage(pageId = "story_page") {
        AmityStorySegmentTimerElement(
            pageScope = getPageScope(),
            totalSegments = 3,
            currentSegment = 2,
            duration = 2000,
            shouldPauseTimer = false,
            shouldRestart = false,
        ) {}
    }
}