package com.amity.socialcloud.uikit.community.compose.post.composer.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.video.VideoFrameDecoder
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityClipAttachmentElement(
    post: AmityPost?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // These will only be recalculated when post changes, not on every keystroke
    val postId = remember(post) { post?.getPostId() ?: "" }

    // Create image loader once
    val imageLoader = remember {
        ImageLoader.Builder(context.applicationContext)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }

    // Get clip data once per post
    val clipData = remember(postId) {
        post?.getChildren()?.firstOrNull()?.getData() as? AmityPost.Data.CLIP
    }

    // Only recalculate these values if clipData changes
    val thumbnail = remember(clipData) {
        clipData?.getThumbnailImage()?.getUrl()
    }

    val aspect = remember(clipData) {
        clipData?.getDisplayMode() ?: AmityClip.DisplayMode.FILL
    }

    clipData?.let {
        Box(
            modifier = modifier
                .height(142.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            // Use a key to prevent image flickering
            key(thumbnail) {
                AsyncImage(
                    model = thumbnail,
                    imageLoader = imageLoader,
                    contentDescription = null,
                    contentScale = if (aspect == AmityClip.DisplayMode.FILL) ContentScale.Crop else ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("image_view")
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
                    .background(
                        color = Color(0x88000000),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_play_v4),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}