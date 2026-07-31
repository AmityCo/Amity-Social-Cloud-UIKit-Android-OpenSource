package com.amity.socialcloud.uikit.chat.compose.message.element

import android.net.Uri
import androidx.compose.runtime.compositionLocalOf

/**
 * Provides a list of recently sent video URIs for local thumbnail extraction.
 * This allows video message bubbles to generate thumbnails from the local file
 * before the server-side thumbnail becomes available.
 */
val LocalSentVideoUris = compositionLocalOf<List<Uri>> { emptyList() }

/**
 * MessageId-keyed cache of the LOCAL video file path learned during upload (from the upload-info
 * stream). Composable state does not survive LazyColumn item recycling: right after a video
 * message syncs, the list item is recreated, the synced message data carries no local path, and
 * the server thumbnail is usually not generated yet — without this cache the bubble falls to the
 * grey play-placeholder even though we had a perfect local thumbnail a frame earlier.
 * Small LRU (32 entries), in-memory only.
 */
internal object AmitySentVideoPathCache {
    private const val MAX = 32
    private val paths = object : LinkedHashMap<String, String>(0, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?) = size > MAX
    }

    @Synchronized
    fun put(messageId: String, path: String) {
        paths[messageId] = path
    }

    @Synchronized
    fun get(messageId: String): String? = paths[messageId]
}
