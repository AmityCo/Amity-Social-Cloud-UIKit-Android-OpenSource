package com.amity.socialcloud.uikit.community.compose.post.detail

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.core.file.AmityVideo

object AmityPostVideoPlayerHelper {

    private var exoPlayer: ExoPlayer? = null

    fun setup(exoPlayer: ExoPlayer) {
        AmityPostVideoPlayerHelper.exoPlayer = exoPlayer.apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
        }
    }

    fun add(videos: List<AmityVideo>) {
        videos.map { video ->
            exoPlayer?.apply {
                video.getVideoUrl()?.let {
                    addMediaItem(MediaItem.fromUri(it))
                }
                prepare()
            }
        }
    }

    fun playMediaItem(index: Int) {
        exoPlayer?.apply {
            seekTo(index, 0)
            pause()
        }
    }

    fun clear() {
        exoPlayer = null
    }
}