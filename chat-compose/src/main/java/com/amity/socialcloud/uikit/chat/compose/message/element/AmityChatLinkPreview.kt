package com.amity.socialcloud.uikit.chat.compose.message.element

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@Composable
fun AmityChatLinkPreview(
    url: String,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
) {
    var previewData by remember(url) { mutableStateOf(AmityLinkPreviewFetcher.getCached(url)) }
    var isLoading by remember(url) { mutableStateOf(!AmityLinkPreviewFetcher.isCached(url)) }
    var fetchFailed by remember(url) { mutableStateOf(AmityLinkPreviewFetcher.isCached(url) && AmityLinkPreviewFetcher.getCached(url) == null) }

    val context = LocalContext.current

    LaunchedEffect(url) {
        if (!AmityLinkPreviewFetcher.isCached(url)) {
            val data = AmityLinkPreviewFetcher.fetchPreview(url)
            previewData = data
            isLoading = false
            fetchFailed = data == null
        }
    }

    if (isLoading) {
        LinkPreviewSkeleton(isCurrentUser = isCurrentUser, modifier = modifier)
        return
    }

    if (fetchFailed) {
        LinkPreviewFallback(
            url = url,
            isCurrentUser = isCurrentUser,
            modifier = modifier,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        )
        return
    }

    val data = previewData ?: return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                context.startActivity(intent)
            },
    ) {
        // Image section (left)
        val imageUrl = data.imageUrl
        if (imageUrl != null) {
            LinkPreviewImage(
                imageUrl = imageUrl,
                isCurrentUser = isCurrentUser,
            )
        }

        // Content section (right)
        Column(
            modifier = Modifier
                .weight(1f)
                .height(96.dp)
                .background(
                    if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.15f)
                    else AmityTheme.colors.baseShade4
                )
                .padding(horizontal = 10.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            if (!data.title.isNullOrBlank()) {
                Text(
                    text = data.title,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isCurrentUser) amityColorWhite else AmityTheme.colors.baseInverse,
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = data.host,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isCurrentUser) amityColorWhite.copy(alpha = 0.7f)
                    else AmityTheme.colors.baseShade1,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun LinkPreviewImage(
    imageUrl: String,
    isCurrentUser: Boolean,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )
    val painterState by painter.state.collectAsState()

    Box(
        modifier = Modifier
            .width(96.dp)
            .height(96.dp),
        contentAlignment = Alignment.Center,
    ) {
        when (painterState) {
            is AsyncImagePainter.State.Success -> {
                val state = painterState as AsyncImagePainter.State.Success
                val intrinsicWidth = state.painter.intrinsicSize.width
                val isFavicon = intrinsicWidth > 0 && intrinsicWidth <= 64f
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = if (isFavicon) ContentScale.Fit else ContentScale.Crop,
                    modifier = Modifier
                        .width(96.dp)
                        .height(96.dp)
                        .then(if (isFavicon) Modifier.padding(20.dp) else Modifier),
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(96.dp)
                        .background(
                            if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.3f)
                            else AmityTheme.colors.baseShade3
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_chat_link_error),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun LinkPreviewSkeleton(
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(10.dp)),
    ) {
        Box(
            modifier = Modifier
                .width(96.dp)
                .height(96.dp)
                .background(
                    if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.3f)
                    else AmityTheme.colors.baseShade3
                ),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .height(96.dp)
                .background(
                    if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.15f)
                    else AmityTheme.colors.baseShade4
                )
                .padding(10.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.3f)
                        else AmityTheme.colors.baseShade3
                    ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(54.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.3f)
                        else AmityTheme.colors.baseShade3
                    ),
            )
        }
    }
}

@Composable
private fun LinkPreviewFallback(
    url: String,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val host = try {
        val uri = java.net.URI(url)
        uri.host?.removePrefix("www.") ?: url
    } catch (_: Exception) {
        amityChatString("chat.bubble.link.preview.no.data")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .width(96.dp)
                .height(96.dp)
                .background(
                    if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.3f)
                    else AmityTheme.colors.baseShade3
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.amity_ic_chat_link_error),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .height(96.dp)
                .background(
                    if (isCurrentUser) AmityTheme.colors.primary.copy(alpha = 0.15f)
                    else AmityTheme.colors.baseShade4
                )
                .padding(horizontal = 10.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            Text(
                text = amityChatString("chat.preview.not.available"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCurrentUser) amityColorWhite else AmityTheme.colors.baseInverse,
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = host,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isCurrentUser) amityColorWhite.copy(alpha = 0.7f)
                    else AmityTheme.colors.baseShade1,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
