package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.image.rememberZoomState
import com.amity.socialcloud.uikit.common.ui.image.zoomable
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface

/**
 * Full-screen, zoomable preview of a user's avatar.
 *
 * Takes an already-resolved URL string rather than an [com.amity.socialcloud.sdk.model.core.file.AmityImage],
 * because a user's avatar may come from `avatarCustomUrl` — an external operator URL that has no
 * backing Amity file and therefore cannot be represented as an `AmityImage`. Callers should pass
 * `user.resolvedAvatarUrl(AmityImage.Size.LARGE)`.
 *
 * This mirrors the iOS UIKit, where the user avatar is previewed by feeding the resolved
 * `AmityUserModel.avatarURL` string into `MediaViewer`.
 *
 * Related: SLE-566 / iOS equivalent SLE-565
 */
@Composable
fun AmityUserAvatarPreviewDialog(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    onDismiss: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(avatarUrl)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    val painterState by painter.state.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(amityMediaSurface)
        ) {
            Image(
                painter = painter,
                contentDescription = "User Avatar",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(rememberZoomState()),
            )

            if (painterState !is AsyncImagePainter.State.Success) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(AmityTheme.colors.baseShade4)
                        .align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(amityColorBlack.copy(alpha = 0.5f))
                    .zIndex(Float.MAX_VALUE),
            ) {
                AmityMenuButton(
                    icon = R.drawable.amity_ic_close2,
                    size = 32.dp,
                    iconPadding = 8.dp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    onClick = onDismiss,
                )
            }
        }
    }
}
