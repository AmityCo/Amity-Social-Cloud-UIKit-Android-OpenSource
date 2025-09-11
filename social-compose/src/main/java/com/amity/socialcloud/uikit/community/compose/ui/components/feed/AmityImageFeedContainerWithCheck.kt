package com.amity.socialcloud.uikit.community.compose.ui.components.feed

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import kotlinx.coroutines.delay

/**
 * Advanced container that can periodically check if the displayed item still exists
 */
@Composable
fun AmityImageFeedContainerWithCheck(
    availablePostIds: Set<String> = emptySet(), // Current available post IDs
    checkInterval: Long = 1000L, // Check every 1 second
    isItemStillAvailable: (String?) -> Boolean = { postId ->
        // Default implementation checks if postId is in the available set
        postId == null || availablePostIds.contains(postId)
    }, // Callback to check if item exists
    content: @Composable (
        openDialog: (AmityImage?, String?, onBottomSheetRequest: (() -> Unit)?) -> Unit
    ) -> Unit
) {
    var dialogData by remember { mutableStateOf<Triple<AmityImage?, String?, (() -> Unit)?>?>(null) }
    var isItemDeleted by remember { mutableStateOf(false) }

    content { image, postId, onBottomSheetRequest ->
        dialogData = Triple(image, postId, onBottomSheetRequest)
        isItemDeleted = false
    }

    // Periodically check if the item still exists
    LaunchedEffect(dialogData, availablePostIds) {
        while (dialogData != null) {
            val currentPostId = dialogData?.second
            if (currentPostId != null && !isItemStillAvailable(currentPostId)) {
                isItemDeleted = true
                break
            }
            delay(checkInterval)
        }
    }

    dialogData?.let { (image, postId, onBottomSheetRequest) ->

        if (isItemDeleted) {
            ImageNotAvailableDialog(
                onDismiss = { 
                    dialogData = null 
                    isItemDeleted = false
                }
            )
        } else {
            AmityProfileImageFeedItemPreviewDialog(
                data = image,
                postId = postId,
                onPostClick = { clickedPostId ->
                    onBottomSheetRequest?.invoke()
                }
            ) {
                dialogData = null
                isItemDeleted = false
            }
        }
    }
}

@Composable
private fun ImageNotAvailableDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_image_not_available),
                    contentDescription = "Image Not Available",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This image is no longer available and may have been deleted.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(0.8f),
                    textAlign = TextAlign.Center
                )
            }

            AmityMenuButton(
                icon = R.drawable.amity_ic_close2,
                size = 32.dp,
                iconPadding = 10.dp,
                tint = Color.Black.copy(0.5f),
                background = Color.White.copy(0.8f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                onClick = onDismiss
            )
        }
    }
}
