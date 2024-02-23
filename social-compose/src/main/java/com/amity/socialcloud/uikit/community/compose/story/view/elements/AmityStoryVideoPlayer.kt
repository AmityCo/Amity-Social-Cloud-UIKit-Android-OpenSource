package com.amity.socialcloud.uikit.community.compose.story.view.elements

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun AmityStoryVideoPlayer(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer?,
    isVisible: Boolean,
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            PlayerView(it).apply {
                useController = false
            }
        },
        update = {
            if (isVisible) {
                it.player = exoPlayer
            } else {
                it.player = null
            }
        }
    )
}