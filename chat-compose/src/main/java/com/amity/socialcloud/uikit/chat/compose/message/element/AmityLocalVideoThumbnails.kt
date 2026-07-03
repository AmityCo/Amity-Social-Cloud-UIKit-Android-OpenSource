package com.amity.socialcloud.uikit.chat.compose.message.element

import android.net.Uri
import androidx.compose.runtime.compositionLocalOf

/**
 * Provides a list of recently sent video URIs for local thumbnail extraction.
 * This allows video message bubbles to generate thumbnails from the local file
 * before the server-side thumbnail becomes available.
 */
val LocalSentVideoUris = compositionLocalOf<List<Uri>> { emptyList() }
