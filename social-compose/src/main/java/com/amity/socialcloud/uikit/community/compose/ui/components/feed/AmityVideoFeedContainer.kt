package com.amity.socialcloud.uikit.community.compose.ui.components.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityVideoFeedContainer(
    availablePostIds: Set<String> = emptySet(), // Pass current available post IDs
    content: @Composable (
        openDialog: (AmityPost.Data.VIDEO, String?, onBottomSheetRequest: (() -> Unit)?) -> Unit
    ) -> Unit
) {
    var dialogData by remember { mutableStateOf<Triple<AmityPost.Data.VIDEO?, String?, (() -> Unit)?>?>(null) }

    content { videoData, postId, onBottomSheetRequest ->
        dialogData = Triple(videoData, postId, onBottomSheetRequest)
    }

    dialogData?.let { (videoData, postId, onBottomSheetRequest) ->

        // Check if the current dialog's post still exists in the available post IDs
        val isItemDeleted = postId != null && 
                           availablePostIds.isNotEmpty() && 
                           !availablePostIds.contains(postId)

        if (isItemDeleted) {
            VideoNotAvailableDialog(
                onDismiss = { 
                    dialogData = null 
                }
            )
        } else if (videoData != null) {
            AmityProfileVideoFeedItemPreviewDialog(
                data = videoData,
                postId = postId,
                onPostClick = { clickedPostId ->
                    onBottomSheetRequest?.invoke()
                }
            ) {
                dialogData = null
            }
        }
    }
}

@Composable
private fun VideoNotAvailableDialog(
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
                    painter = painterResource(id = R.drawable.amity_ic_video_not_available),
                    contentDescription = "Video Not Available",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This video is no longer available.",
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(400),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
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
