package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCommunityImageFeedItem(
    modifier: Modifier = Modifier,
    data: AmityPost.Data.IMAGE?,
) {
    val thumbnailUrl = remember(data) {
        data?.getImage()?.getUrl(AmityImage.Size.MEDIUM)
    }

    var showPopupDialog by remember {
        mutableStateOf(false)
    }

    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(thumbnailUrl)
            .crossfade(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = "Image Post",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clickableWithoutRipple {
                showPopupDialog = true
            }
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(AmityTheme.colors.baseShade4)
    )

    if (showPopupDialog) {
        AmityCommunityImagePreviewDialog(data = data) {
            showPopupDialog = false
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AmityCommunityImagePreviewDialog(
    modifier: Modifier = Modifier,
    data: AmityPost.Data.IMAGE?,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(data?.getImage()?.getUrl(AmityImage.Size.FULL))
                    .crossfade(true)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "Image Post",
                contentScale = ContentScale.Fit,
                modifier = modifier.fillMaxSize()
            )

            AmityMenuButton(
                icon = R.drawable.amity_ic_close2,
                size = 32.dp,
                iconPadding = 10.dp,
                tint = Color.Black.copy(0.5f),
                background = Color.White.copy(0.8f),
                modifier = modifier
                    .align(Alignment.TopStart)
                    .offset(16.dp, 32.dp),
                onClick = {
                    onDismiss()
                }
            )
        }
    }
}