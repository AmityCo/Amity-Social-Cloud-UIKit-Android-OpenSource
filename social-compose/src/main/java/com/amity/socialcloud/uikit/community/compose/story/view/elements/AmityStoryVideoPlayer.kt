package com.amity.socialcloud.uikit.community.compose.story.view.elements

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


@UnstableApi
@Composable
fun AmityStoryVideoPlayer(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer?,
    isVisible: Boolean,
    scaleMode: Int ? = null,
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),  // Use fillMaxSize for global video player
        factory = {
            Log.d("AmityStoryVideoPlayer", "Factory called - creating PlayerView")
            PlayerView(it).apply {
                useController = false
            }
        },
        update = { playerView ->
            if (isVisible) {
                Log.d("AmityStoryVideoPlayer", "Attaching player to view - playerView: $playerView")
                playerView.player = exoPlayer
                exoPlayer?.let { player ->
                    Log.d("AmityStoryVideoPlayer", "Player attached - isPlaying: ${player.isPlaying}, currentIndex: ${player.currentMediaItemIndex}, playbackState: ${player.playbackState}")
                }
            } else {
                Log.d("AmityStoryVideoPlayer", "Detaching player from view - playerView: $playerView")
                playerView.player = null
            }

            scaleMode?.let { scale ->
                playerView.resizeMode = scale
            }

        }
    )
}