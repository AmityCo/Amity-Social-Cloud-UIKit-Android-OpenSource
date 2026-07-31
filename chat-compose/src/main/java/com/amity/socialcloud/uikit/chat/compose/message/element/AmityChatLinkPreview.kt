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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoader
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityLoaderSize

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

    if (data.title.isNullOrBlank() && data.imageUrl.isNullOrBlank()) {
        LinkPreviewFallback(
            isCurrentUser = isCurrentUser,
            modifier = modifier,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                context.startActivity(intent)
            },
        )
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(113.dp)
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
                .height(113.dp)
                .background(AmityTheme.token(AmityColorToken.SurfaceCardPreviewLinkDefault))
                .padding(horizontal = 10.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            if (!data.title.isNullOrBlank()) {
                Text(
                    text = data.title,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.token(AmityColorToken.TextCardPreviewLinkTitleDefault),
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = data.host,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.token(AmityColorToken.TextCardPreviewLinkDomainDefault),
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
            .width(113.dp)
            .height(113.dp),
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
                        .width(113.dp)
                        .height(113.dp)
                        .then(if (isFavicon) Modifier.padding(20.dp) else Modifier),
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .width(113.dp)
                        .height(113.dp)
                        .background(AmityTheme.token(AmityColorToken.SurfaceMediaImageBroken)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_image_slash_r),
                        contentDescription = null,
                        tint = AmityTheme.token(AmityColorToken.IconMediaImageBroken),
                        modifier = Modifier.size(40.dp),
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
            .height(113.dp)
            .clip(RoundedCornerShape(10.dp)),
    ) {
        // Left (113×113): media-loading placeholder on the medium-dark media surface — no extra
        // overlay — plus a centred loading spinner.
        Box(
            modifier = Modifier
                .width(113.dp)
                .height(113.dp)
                .background(AmityTheme.token(AmityColorToken.SurfaceMediaImageLoading)),
            contentAlignment = Alignment.Center,
        ) {
            // Designer-confirmed uploadSpinner variant (indeterminate) — replaces the former local
            // hand-rolled MediaLoadingSpinner; binds the white UploadController tokens via the atom.
            AmityLoader(variant = AmityLoaderVariant.UploadSpinner, size = AmityLoaderSize.Lg)
        }
        // Right (113×113): info-pane skeleton — the Surface/Card/PreviewLink/Skeleton pane with two
        // shimmer bars (title 80×8 + domain 54×8, r12, Surface/SkeletonEffect/Default), matching the
        // design loading state — NOT a solid block.
        Column(
            modifier = Modifier
                .weight(1f)
                .height(113.dp)
                .background(AmityTheme.token(AmityColorToken.SurfaceCardPreviewLinkSkeleton))
                .padding(horizontal = 16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(8.dp)
                    .shimmerBackground(
                        shape = RoundedCornerShape(12.dp),
                        color = AmityTheme.token(AmityColorToken.SurfaceSkeletonEffectDefault),
                    ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(54.dp)
                    .height(8.dp)
                    .shimmerBackground(
                        shape = RoundedCornerShape(12.dp),
                        color = AmityTheme.token(AmityColorToken.SurfaceSkeletonEffectDefault),
                    ),
            )
        }
    }
}

@Composable
private fun LinkPreviewFallback(
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(113.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .width(113.dp)
                .height(113.dp)
                .background(AmityTheme.token(AmityColorToken.SurfaceMediaImageBroken)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_image_slash_r),
                contentDescription = null,
                tint = AmityTheme.token(AmityColorToken.IconMediaImageBroken),
                modifier = Modifier.size(40.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .height(113.dp)
                .background(AmityTheme.token(AmityColorToken.SurfaceCardPreviewLinkDefault))
                .padding(horizontal = 10.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            Text(
                text = amityChatString("chat.preview.not.available"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.token(AmityColorToken.TextCardPreviewLinkTitleDefault),
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = amityChatString("chat.bubble.link.preview.no.data"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = AmityTheme.token(AmityColorToken.TextCardPreviewLinkDomainDefault),
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
