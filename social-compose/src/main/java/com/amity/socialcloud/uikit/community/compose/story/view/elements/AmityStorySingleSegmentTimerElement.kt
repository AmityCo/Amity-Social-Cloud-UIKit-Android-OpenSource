package com.amity.socialcloud.uikit.community.compose.story.view.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposeElementScope
import com.amity.socialcloud.uikit.community.compose.utils.asColor
import com.amity.socialcloud.uikit.community.compose.utils.getBackgroundColor
import com.amity.socialcloud.uikit.community.compose.utils.getValue
import kotlinx.coroutines.delay


@Composable
fun AmityStorySingleSegmentTimerElement(
    modifier: Modifier = Modifier,
    elementScope: AmityComposeElementScope,
    shouldStart: Boolean = false,
    shouldRestart: Boolean = false,
    shouldPauseTimer: Boolean = false,
    isAlreadyFinished: Boolean = false,
    duration: Long = 0,
    onTimerFinished: () -> Unit
) {
    var currentProgress by remember {
        mutableStateOf(if (isAlreadyFinished) 1f else 0f)
    }

    LinearProgressIndicator(
        progress = currentProgress,
        color = elementScope.getConfig().getValue("progress_color").asColor(),
        trackColor = elementScope.getConfig().getBackgroundColor(),
        strokeCap = StrokeCap.Round,
        modifier = modifier.fillMaxWidth()
    )

    LaunchedEffect(shouldStart, shouldRestart, shouldPauseTimer, isAlreadyFinished, duration) {
        if (shouldStart) {
            if (shouldRestart) {
                currentProgress = 0f
            }
            if (!shouldPauseTimer) {
                currentProgress = if (currentProgress == 1f) 0f else currentProgress
                loadProgress(duration, currentProgress) { progress ->
                    currentProgress = progress
                    if (progress == 1f) {
                        onTimerFinished()
                    }
                }
            }
        } else {
            currentProgress = if (isAlreadyFinished) 1f else 0f
        }
    }
}

suspend fun loadProgress(duration: Long, currentProgress: Float, updateProgress: (Float) -> Unit) {
    val totalInterval = 150
    val delayInterval = duration / totalInterval
    val startFrom = (currentProgress * totalInterval).toInt()

    for (i in startFrom..totalInterval) {
        updateProgress(i.toFloat() / totalInterval)
        delay(delayInterval)
    }
}

@Preview(showBackground = true)
@Composable
fun AmityStorySingleSegmentTimerElementPreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityBasePage(pageId = "story_page") {
        AmityBaseElement(
            pageScope = getPageScope(),
            elementId = "progress_bar"
        ) {
            AmityStorySingleSegmentTimerElement(
                elementScope = getElementScope(),
            ) {}
        }
    }
}