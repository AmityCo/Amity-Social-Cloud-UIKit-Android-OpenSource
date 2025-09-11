package com.amity.socialcloud.uikit.community.compose.utils

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.utils.getVideoUrlWithFallbackQuality
import kotlinx.coroutines.flow.MutableStateFlow

object AmityStoryVideoPlayerHelper {

    // Map of targetId to ExoPlayer instance
    private val exoPlayerInstances = mutableMapOf<String, ExoPlayer>()

    // Map of targetId to URL mapping for that target
    private val targetUrlMappings = mutableMapOf<String, MutableMap<String, Int>>()

    // Track which target is currently active
    private var currentActiveTargetId: String? = null
    private var currentActiveStoryId: String? = null

    private val _duration by lazy {
        MutableStateFlow(15_000L)
    }
    val duration get() = _duration

    private val videoDurationListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            when (playbackState) {
                Player.STATE_READY -> {
                    // Find which player triggered this and get its duration
                    exoPlayerInstances.values.find { it.playbackState == Player.STATE_READY }?.duration?.let { duration ->
                        _duration.value = duration
                    }
                }

                else -> {
                    // Other state
                }
            }
        }
    }

    private val _isVideoPlaybackReady by lazy {
        MutableStateFlow(false)
    }
    val isVideoPlaybackReady get() = _isVideoPlaybackReady

    private val _isVideoEnded by lazy {
        MutableStateFlow(false)
    }
    val isVideoEnded get() = _isVideoEnded

    private val videoPlaybackStateListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            when (playbackState) {
                Player.STATE_READY -> {
                    _isVideoPlaybackReady.value = true
                    _isVideoEnded.value = false
                }

                Player.STATE_ENDED -> {
                    _isVideoPlaybackReady.value = false
                    _isVideoEnded.value = true
                }

                else -> {
                    _isVideoPlaybackReady.value = false
                    _isVideoEnded.value = false
                }
            }
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            super.onVideoSizeChanged(videoSize)
        }

        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()
        }
    }

    fun setup(targetId: String, exoPlayer: ExoPlayer) {
        // Store the ExoPlayer instance for this target
        exoPlayerInstances[targetId] = exoPlayer

        // Initialize URL mapping for this target
        if (!targetUrlMappings.containsKey(targetId)) {
            targetUrlMappings[targetId] = mutableMapOf()
        }

        // Add listeners to this ExoPlayer
        exoPlayer.addListener(videoDurationListener)
        exoPlayer.addListener(videoPlaybackStateListener)
    }

    fun add(targetId: String, stories: List<AmityListItem>) {
        val exoPlayer = exoPlayerInstances[targetId]
        val urlMapping = targetUrlMappings[targetId]

        if (exoPlayer == null || urlMapping == null) {
            return
        }

        stories.asSequence().filterIsInstance<AmityListItem.StoryItem>()
            .map { it.story }
            .filter {
                it.getDataType() == AmityStory.DataType.VIDEO
            }.map { story ->
                val video = (story.getData() as AmityStory.Data.VIDEO).getVideo()
                story.getStoryId() to video
            }.map { (storyId, video) ->
                if (!urlMapping.containsKey(storyId)) {
                    val currentCount = exoPlayer.mediaItemCount
                    urlMapping[storyId] = currentCount

                    exoPlayer.apply {
                        val fileUrl = video?.getVideoUrlWithFallbackQuality()
                        if (fileUrl.isNullOrEmpty()) {
                            video?.getUri()?.let { uri ->
                                addMediaItem(MediaItem.fromUri(uri))
                            }
                        } else {
                            addMediaItem(MediaItem.fromUri(fileUrl))
                        }
                        prepare()
                    }
                } else {
                    // Story already exists
                }
            }.toList()
    }

    fun playMediaItem(targetId: String, storyId: String, forcePlay: Boolean = false) {

        val exoPlayer = exoPlayerInstances[targetId]
        val urlMapping = targetUrlMappings[targetId]

        if (exoPlayer == null || urlMapping == null) {
            return
        }

        // If this story is already active and playing, no need to do anything
        if (currentActiveTargetId == targetId && currentActiveStoryId == storyId && exoPlayer.isPlaying) {
            return
        }

        // If another target/story is active and we're not forcing play, ignore
        if (!forcePlay && (currentActiveTargetId != null && currentActiveTargetId != targetId)) {
            return
        }

        // Stop any currently playing video from other targets
        if (currentActiveTargetId != null && currentActiveTargetId != targetId) {
            pauseAllOtherTargets(targetId)
        }

        // Set this as the active target and story
        currentActiveTargetId = targetId
        currentActiveStoryId = storyId
        _isVideoEnded.value = false  // Reset video ended state when starting new video

        val index = urlMapping[storyId]
        if (index == null) {
            return
        }

        if (index != exoPlayer.currentMediaItemIndex) {
            try {
                exoPlayer.seekTo(index, 0)
                exoPlayer.play()
            } catch (e: Exception) {
                // Handle seek error, maybe log it
            }
        } else {
            if (!exoPlayer.isPlaying) {
                // Player not playing, starting playback
                exoPlayer.play()
            } else {
                // Player already playing
            }
        }
    }

    private fun pauseAllOtherTargets(exceptTargetId: String) {
        exoPlayerInstances.forEach { (targetId, exoPlayer) ->
            if (targetId != exceptTargetId && exoPlayer.isPlaying) {
                exoPlayer.pause()
            }
        }
    }

    fun stopCurrentVideo() {
        if (currentActiveTargetId != null && currentActiveStoryId != null) {
            val wasActiveTarget = currentActiveTargetId
            val wasActiveStory = currentActiveStoryId

            exoPlayerInstances[currentActiveTargetId]?.pause()

            currentActiveTargetId = null
            currentActiveStoryId = null

        } else {
            // No active video to stop

        }
    }

    fun stopVideoIfActive(targetId: String, storyId: String) {

        if (currentActiveTargetId == targetId && currentActiveStoryId == storyId) {
            stopCurrentVideo()
        } else {
            // do nothing
        }
    }

    fun isVideoActive(targetId: String, storyId: String): Boolean {
        return currentActiveTargetId == targetId && currentActiveStoryId == storyId
    }

    fun resetPlaybackIndex(targetId: String) {
        exoPlayerInstances[targetId]?.apply {
            seekTo(0)
        }
    }

    fun clearMediaItems(targetId: String) {

        // Clear active states if this target was active
        if (currentActiveTargetId == targetId) {
            currentActiveTargetId = null
            currentActiveStoryId = null
            _isVideoEnded.value = false
        }

        exoPlayerInstances[targetId]?.apply {
            stop()
            clearMediaItems()
        }
        targetUrlMappings[targetId]?.clear()
    }

    fun clear(targetId: String? = null) {
        if (targetId != null) {
            // Clear specific target
            exoPlayerInstances[targetId]?.let { exoPlayer ->
                exoPlayer.removeListener(videoDurationListener)
                exoPlayer.removeListener(videoPlaybackStateListener)
                exoPlayer.release()
            }
            exoPlayerInstances.remove(targetId)
            targetUrlMappings.remove(targetId)

            if (currentActiveTargetId == targetId) {
                currentActiveTargetId = null
                currentActiveStoryId = null
            }
        } else {
            // Clear all targets
            exoPlayerInstances.values.forEach { exoPlayer ->
                exoPlayer.removeListener(videoDurationListener)
                exoPlayer.removeListener(videoPlaybackStateListener)
                exoPlayer.release()
            }
            exoPlayerInstances.clear()
            targetUrlMappings.clear()
            currentActiveTargetId = null
            currentActiveStoryId = null
        }
    }
}