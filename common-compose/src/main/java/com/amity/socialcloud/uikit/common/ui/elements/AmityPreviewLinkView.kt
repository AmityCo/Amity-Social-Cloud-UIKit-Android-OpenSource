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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.linkpreview.AmityPreviewUrl
import com.amity.socialcloud.uikit.common.linkpreview.models.AmityPreviewNoUrl
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

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

    val previewUrlCache by remember(post.getPostId(), post.getUpdatedAt()) {
        derivedStateOf {
            val postText = (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
            val url = postText.extractUrls().firstOrNull()?.url
            val postId = post.getPostId()
            val editedAt = post.getEditedAt()
            AmityPreviewUrl.getPostPreviewUrl(postId, url, editedAt)
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
            val commentText = (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""
            val url = commentText.extractUrls().firstOrNull()?.url
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
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val previewMetadata by remember(url) {
        AmityPreviewUrl.fetchMetadata(url)
    }.subscribeAsState(null)

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
        Box(
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

        HorizontalDivider(
            thickness = 1.dp,
            color = AmityTheme.colors.baseShade4,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (previewMetadata != null) {
                Text(
                    text = previewMetadata!!.domain,
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                )
                Text(
                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
                    text = previewMetadata!!.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
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