package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack

@Composable
fun AmityAvatarFullScreenDialog(
    avatarUrl: String?,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(amityColorBlack)
                .clickableWithoutRipple { onDismiss() },
            contentAlignment = Alignment.Center,
        ) {
            if (!avatarUrl.isNullOrBlank()) {
                var loadFailed by remember(avatarUrl) { mutableStateOf(false) }
                if (!loadFailed) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatarUrl)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .listener(onError = { _, _ -> loadFailed = true })
                            .build(),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.amity_ic_chat_avatar_placeholder
                        ),
                        contentDescription = "Avatar",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(120.dp),
                    )
                }
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.amity_ic_chat_avatar_placeholder
                    ),
                    contentDescription = "Avatar",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(120.dp),
                )
            }

            // Close button — top-left
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 48.dp)
                    .size(36.dp)
                    .background(color = amityColorBlack.copy(alpha = 0.5f), shape = CircleShape)
                    .clickableWithoutRipple { onDismiss() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_close_reply),
                    contentDescription = "Close",
                    tint = amityColorWhite,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
