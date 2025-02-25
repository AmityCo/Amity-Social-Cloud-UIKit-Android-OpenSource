package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.uikit.community.compose.databinding.AmityExoSocialPlayerBinding


@Composable
fun AmityPostMediaVideoPlayer(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer?,
    isVisible: Boolean,
) {
    AndroidViewBinding(
        modifier = modifier.fillMaxSize(),
        factory = AmityExoSocialPlayerBinding::inflate,
    ) {
        if (isVisible) {
            this.exoPlayer.player = exoPlayer
        } else {
            this.exoPlayer.player = null
        }
    }
}