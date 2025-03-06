package com.amity.socialcloud.uikit.community.compose.utils

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.utils.getVideoUrlWithFallbackQuality
import kotlinx.coroutines.flow.MutableStateFlow

object AmityStoryVideoPlayerHelper {

    private var exoPlayer: ExoPlayer? = null

    private val urlMapping = mutableMapOf<String, Int>()

    private val _duration by lazy {
        MutableStateFlow(15_000L)
    }
    val duration get() = _duration

    private val videoDurationListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            when (playbackState) {
                Player.STATE_READY -> {
                    exoPlayer?.duration?.let {
                        _duration.value = it
                    }
                }

                else -> {}
            }
        }
    }

    private val _isVideoPlaybackReady by lazy {
        MutableStateFlow(false)
    }
    val isVideoPlaybackReady get() = _isVideoPlaybackReady

    private val videoPlaybackStateListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            when (playbackState) {
                Player.STATE_READY -> {
                    _isVideoPlaybackReady.value = true
                }

                else -> {
                    _isVideoPlaybackReady.value = false
                }
            }
        }
    }

    fun setup(exoPlayer: ExoPlayer) {
        this.exoPlayer = exoPlayer

        setupListener()
    }

    fun add(stories: List<AmityListItem>) {
        stories.asSequence().filterIsInstance<AmityListItem.StoryItem>()
            .map { it.story }
            .filter {
                it.getDataType() == AmityStory.DataType.VIDEO
            }.map { story ->
                val video = (story.getData() as AmityStory.Data.VIDEO).getVideo()

                story.getStoryId() to video
            }.map { (storyId, video) ->
                if (!urlMapping.containsKey(storyId)) {
                    urlMapping[storyId] = exoPlayer?.mediaItemCount ?: 0
                    exoPlayer?.apply {
                        val fileUrl = video?.getVideoUrlWithFallbackQuality()
                        if (fileUrl.isNullOrEmpty()) {
                            video?.getUri()?.let {
                                addMediaItem(MediaItem.fromUri(it))
                            }
                        } else {
                            addMediaItem(MediaItem.fromUri(fileUrl))
                        }
                        prepare()
                    }
                }
            }.toList()
    }

    fun playMediaItem(storyId: String) {
        exoPlayer?.apply {
            val index = urlMapping[storyId] ?: 0
            if (index != exoPlayer?.currentMediaItemIndex) {
                seekTo(index, 0)
            }
        }
    }

    fun resetPlaybackIndex() {
        exoPlayer?.apply {
            seekTo(0)
        }
    }

    fun clear() {
        exoPlayer?.removeListener(videoDurationListener)
        exoPlayer?.removeListener(videoPlaybackStateListener)
        exoPlayer = null

        urlMapping.clear()
    }

    private fun setupListener() {
        exoPlayer?.addListener(videoDurationListener)
        exoPlayer?.addListener(videoPlaybackStateListener)
    }
}