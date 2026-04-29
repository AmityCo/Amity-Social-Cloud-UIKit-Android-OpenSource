package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.link.AmityLink
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.extionsions.parseUrls
import com.amity.socialcloud.uikit.common.linkpreview.AmityPreviewUrl
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewMetadataCacheItem
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewNoUrl
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.shimmerBackground
import org.joda.time.DateTime

@Composable
fun AmityPostPreviewLinkView(
    modifier: Modifier = Modifier,
    post: AmityPost,
) {
    val isAllowedPostDataType by remember(post.getPostId(), post.getUpdatedAt()) {
        derivedStateOf {
            post.getType() == AmityPost.DataType.TEXT && post.getChildren().isEmpty()
        }
    }

    val postId by remember(post.getPostId()) {
        derivedStateOf {
            post.getPostId()
        }
    }

    val postEditedAt by remember(post.getUpdatedAt()) {
        derivedStateOf {
            post.getUpdatedAt()
        }
    }

    val previewUrlCache by remember(postId, postEditedAt) {
        derivedStateOf {
            val sdkLinks = post.getLinks()
            val postText = (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
            val url = if (!sdkLinks.isNullOrEmpty()) {
                val firstLink = sdkLinks.first()
                val linkUrl = if (firstLink.getRenderPreview()) firstLink.getUrl() else null
                // Cross-validate against post text: SDK may truncate URLs (e.g. "path(1" → "path"),
                // so we must also confirm our regex finds a URL in the original text.
                // If extractUrls() returns empty, the original URL was malformed → don't preview.
                if (linkUrl != null && postText.extractUrls().isNotEmpty()) linkUrl else null
            } else {
                postText.extractUrls().firstOrNull()?.url
            }
            AmityPreviewUrl.getPostPreviewUrl(postId, url, postEditedAt)
        }
    }

    if (!isAllowedPostDataType
        || previewUrlCache == null
        || previewUrlCache is AmityPreviewNoUrl
    ) return

    AmityPreviewLinkView(
        modifier = Modifier.padding(12.dp),
        url = previewUrlCache!!.url,
    )
}

@Composable
fun AmityCommentPreviewLinkView(
    modifier: Modifier = Modifier,
    comment: AmityComment,
) {
    val isAllowedCommentDataType by remember(comment.getCommentId()) {
        derivedStateOf {
            comment.getDataTypes().contains(AmityComment.DataType.TEXT)
        }
    }

    val previewUrlCache by remember(comment.getCommentId()) {
        derivedStateOf {
            val sdkLinks = comment.getLinks()
            val commentText = (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""
            val url = if (!sdkLinks.isNullOrEmpty()) {
                val firstLink = sdkLinks.first()
                val linkUrl = if (firstLink.getRenderPreview()) firstLink.getUrl() else null
                // Cross-validate against comment text: SDK may truncate URLs (e.g. "path(1" → "path"),
                // so we must also confirm our regex finds a URL in the original text.
                if (linkUrl != null && commentText.extractUrls().isNotEmpty()) linkUrl else null
            } else {
                commentText.extractUrls().firstOrNull()?.url
            }
            val commentId = comment.getCommentId()
            val editedAt = comment.getEditedAt()
            AmityPreviewUrl.getCommentPreviewUrl(commentId, url, editedAt)
        }
    }

    if (!isAllowedCommentDataType
        || previewUrlCache == null
        || previewUrlCache is AmityPreviewNoUrl
    ) return

    AmityPreviewLinkView(
        modifier = modifier,
        url = previewUrlCache!!.url,
    )
}

@Composable
fun AmityPreviewLinkView(
    modifier: Modifier = Modifier,
    url: String,
) {
    val initialUrl = "initial"
    val placeHolderUrl = "placeholder"
    val errorUrl = "error"
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val initial = AmityPreviewMetadataCacheItem(
        url = url,
        domain = "",
        title = "",
        imageUrl = initialUrl,
        timestamp = DateTime.now()
    )

    val errorItem = AmityPreviewMetadataCacheItem(
        url = url,
        domain = "",
        title = "",
        imageUrl = "error",
        timestamp = DateTime.now()
    )

    val previewMetadata by remember(url) {
        AmityPreviewUrl.fetchMetadataFlow(url, errorItem)
    }.subscribeAsState(initial)

    if (previewMetadata.imageUrl == initialUrl
        || previewMetadata.imageUrl == errorUrl
        || previewMetadata.imageUrl.isEmpty()
    ) {
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(8.dp)
            )
            .clickableWithoutRipple {
                uriHandler.openUri(url)
            }
    ) {
        val hasImage = previewMetadata.imageUrl.isNotEmpty()
                && previewMetadata.imageUrl != errorUrl
                && previewMetadata.imageUrl != placeHolderUrl
        val showImageSection = hasImage || previewMetadata.imageUrl == placeHolderUrl

        if (showImageSection) Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .height(200.dp)
                .background(color = AmityTheme.colors.baseShade4)
        ) {
            if (previewMetadata != null) {
                if (previewMetadata!!.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(previewMetadata!!.imageUrl)
                            .crossfade(true)
                            .networkCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "Preview Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AmityTheme.colors.baseShade4)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_placeholder_image),
                        tint = AmityTheme.colors.baseShade3,
                        contentDescription = null,
                        modifier = modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_warning_triangle),
                    tint = AmityTheme.colors.baseShade3,
                    contentDescription = null,
                    modifier = modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }

        if (showImageSection) {
            HorizontalDivider(
                thickness = 1.dp,
                color = AmityTheme.colors.baseShade4,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (previewMetadata != null
                && previewMetadata.imageUrl != initialUrl
                && previewMetadata.imageUrl != placeHolderUrl
            ) {
                // Title first (bodyBold), then domain (caption shade1) — per spec + iOS
                // Fallback: if title empty use domain; if domain also empty use url.
                val displayTitle = previewMetadata!!.title.ifEmpty {
                    previewMetadata!!.domain.ifEmpty { url }
                }
                val displayDomain = previewMetadata!!.domain.ifEmpty {
                    runCatching { java.net.URI(url).host }.getOrNull()?.removePrefix("www.") ?: url
                }
                Text(
                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
                    text = displayTitle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.bodyBold
                )
                Text(
                    text = displayDomain,
                    style = AmityTheme.typography.caption.copy(
                        color = AmityTheme.colors.baseShade1,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                )
            } else if (previewMetadata.imageUrl == placeHolderUrl) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .height(10.dp)
                        .width(140.dp)
                        .shimmerBackground(
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun AmityPreviewLinkViewWithMetadata(
    modifier: Modifier = Modifier,
    url: String,
    domain: String?,
    title: String?,
    imageUrl: String?,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val hasMetadata = !domain.isNullOrEmpty() || !title.isNullOrEmpty()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(8.dp)
            )
            .clickableWithoutRipple {
                uriHandler.openUri(url.parseUrls())
            }
    ) {
        // Only show image Box if imageUrl exists
        if (!imageUrl.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .height(200.dp)
                    .background(color = AmityTheme.colors.baseShade4)
            ) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Preview Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AmityTheme.colors.baseShade4)
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = AmityTheme.colors.baseShade4,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (hasMetadata) {
                if (!title.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
                        text = title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                }
                if (!domain.isNullOrEmpty()) {
                    Text(
                        text = domain,
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade1,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                    )
                }
            } else {
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = context.getString(R.string.amity_preview_not_available_title),
                    lineHeight = 10.sp,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = context.getString(R.string.amity_preview_not_available_message),
                    maxLines = 2,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.baseShade1,
                    ),
                )
            }
        }
    }
}