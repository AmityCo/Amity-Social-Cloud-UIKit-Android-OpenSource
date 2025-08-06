package com.amity.socialcloud.uikit.community.compose.utils

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AmityClipPlayerHelper {

    private var exoPlayer: ExoPlayer? = null
    private val videoUrlCache = mutableMapOf<Int, String>()
    private val mediaItemMapping = mutableMapOf<Int, Int>() // Maps page index to media item index
    private var coroutineScope: CoroutineScope? = null

    // State flows for playback tracking
    private val _isVideoPlaybackReady = MutableStateFlow(false)
    val isVideoPlaybackReady: StateFlow<Boolean> get() = _isVideoPlaybackReady

    private val _isVideoError = MutableStateFlow(false)
    val isVideoError: StateFlow<Boolean> get() = _isVideoError

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> get() = _isMuted

    private val _currentPlayingPage = MutableStateFlow(0)
    val currentPlayingPage: StateFlow<Int> get() = _currentPlayingPage

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    _isVideoPlaybackReady.value = true
                    _isVideoError.value = false
                }
                Player.STATE_ENDED -> {
                    // Auto-replay when clip ends
                    exoPlayer?.seekTo(0)
                    exoPlayer?.play()
                }
                Player.STATE_IDLE -> {
                    _isVideoPlaybackReady.value = false
                }
                else -> { }
            }
        }

        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
            _isVideoError.value = true
            _isVideoPlaybackReady.value = false
            println("ExoPlayer error: ${error.message}")
        }
    }

    fun setup(player: ExoPlayer, scope: CoroutineScope) {
        this.exoPlayer = player
        this.coroutineScope = scope

        setupListener()
        _isMuted.value = player.volume == 0f
    }

    fun setCurrentPlayingPage(page: Int) {
        _currentPlayingPage.value = page
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
        exoPlayer?.volume = if (_isMuted.value) 0f else 1f
    }

    fun setMuted(muted: Boolean) {
        _isMuted.value = muted
        exoPlayer?.volume = if (muted) 0f else 1f
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }

    fun loadVideoUrl(index: Int, priority: Boolean, onComplete: (String?) -> Unit) {
        coroutineScope?.launch(Dispatchers.IO) {
            try {
                // Check cache first
                if (videoUrlCache.containsKey(index)) {
                    withContext(Dispatchers.Main) {
                        onComplete(videoUrlCache[index])
                    }
                    return@launch
                }

                // Get post data
                val post = getCachedPost(index) ?: return@launch
                val data = post.getChildren().firstOrNull()?.getData() as? AmityPost.Data.CLIP ?: return@launch

                // Extract URL with timeout for first clip to prevent long blocking
                val url = try {
                    if (priority) {
                        // Use non-blocking approach if possible for initial load
                        kotlinx.coroutines.withTimeout(3000) {
                            data.getClip().blockingGet().getClipUrl()
                        }
                    } else {
                        // Normal loading for subsequent clips
                        data.getClip().blockingGet().getClipUrl()
                    }
                } catch (e: Exception) {
                    println("Error loading clip URL: ${e.message}")
                    null
                }

                // Cache and return
                if (url != null) {
                    videoUrlCache[index] = url
                    withContext(Dispatchers.Main) {
                        onComplete(url)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onComplete(null)
                    }
                }
            } catch (e: Exception) {
                println("Error in loadVideoUrl: ${e.message}")
                withContext(Dispatchers.Main) {
                    onComplete(null)
                }
            }
        }
    }

    // Keep a reference to posts for media loading
    private val postCache = mutableMapOf<Int, AmityPost>()

    fun cachePost(index: Int, post: AmityPost) {
        postCache[index] = post
    }

    fun getCachedPost(index: Int): AmityPost? {
        return postCache[index]
    }

    fun clearPostCache() {
        postCache.clear()
    }

    fun updateMediaItems(currentPage: Int, prefetchWindow: Int = 2) {
        val startIndex = maxOf(0, currentPage - 1)
        val endIndex = currentPage + prefetchWindow

        // Create a list of pending media items to update
        coroutineScope?.launch {
            val mediaItems = mutableListOf<Pair<Int, MediaItem>>()

            for (i in startIndex..endIndex) {
                val url = videoUrlCache[i] ?: continue
                mediaItems.add(i to MediaItem.fromUri(url))
            }

            // Only update player if we have media items and are on the right page
            if (mediaItems.isNotEmpty() && _currentPlayingPage.value == currentPage) {
                updatePlayerMediaItems(mediaItems, currentPage)
            }
        }
    }

    private fun updatePlayerMediaItems(mediaItems: List<Pair<Int, MediaItem>>, currentPage: Int) {
        val player = exoPlayer ?: return

        // Clear mapping for new items
        mediaItemMapping.clear()

        // Build new media item list
        val currentItems = (0 until player.mediaItemCount).map { player.getMediaItemAt(it) }
        val newItems = mediaItems.map { it.second }

        val needsRefresh = currentItems.size != newItems.size ||
                !currentItems.zip(newItems).all { (current, needed) ->
                    current.localConfiguration?.uri == needed.localConfiguration?.uri
                }

        if (needsRefresh) {
            // Update media items
            player.clearMediaItems()

            mediaItems.forEachIndexed { index, (pageIdx, item) ->
                player.addMediaItem(item)
                mediaItemMapping[pageIdx] = index
            }

            player.prepare()

            // Seek to current page
            val mediaItemIndex = mediaItemMapping[currentPage] ?: 0
            player.seekTo(mediaItemIndex, 0)
        }
    }

    fun preloadNextClips(currentPage: Int, totalPages: Int, prefetchCount: Int = 3) {
        coroutineScope?.launch(Dispatchers.IO) {
            val endPrefetchIdx = minOf(totalPages - 1, currentPage + prefetchCount)
            for (i in (currentPage + 1)..endPrefetchIdx) {
                if (!videoUrlCache.containsKey(i)) {
                    loadVideoUrl(i, false) { /* Just cache the URL */ }
                }
            }
        }
    }

    fun isUrlCached(index: Int): Boolean {
        return videoUrlCache.containsKey(index)
    }

    fun getUrlFromCache(index: Int): String? {
        return videoUrlCache[index]
    }

    fun clear() {
        exoPlayer?.removeListener(playerListener)
        exoPlayer = null
        coroutineScope = null
        videoUrlCache.clear()
        mediaItemMapping.clear()
        postCache.clear()
    }

    private fun setupListener() {
        exoPlayer?.let {
            it.removeListener(playerListener)
            it.addListener(playerListener)
        }
    }
}