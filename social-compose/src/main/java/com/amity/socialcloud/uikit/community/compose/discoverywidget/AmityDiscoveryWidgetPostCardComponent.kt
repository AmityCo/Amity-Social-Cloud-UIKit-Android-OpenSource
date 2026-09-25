package com.amity.socialcloud.uikit.community.compose.discoverywidget

import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.discoverywidget.elements.AmityDiscoveryWidgetPollBlock

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Constraints
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException
import androidx.compose.runtime.State
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtagMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.google.gson.JsonObject
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.link.AmityLinkPreviewMetadata
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

/**
 * Disposes the Rx subscription when the card leaves composition, so a scroll cannot leave a
 * request running against a card nobody is looking at.
 */
private suspend fun awaitPreview(url: String): AmityLinkPreviewMetadata =
    suspendCancellableCoroutine { cont ->
        val disposable = AmityCoreClient.getLinkPreviewMetadata(url)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { cont.resume(it) { _, _, _ -> } },
                { cont.resumeWithException(it) },
            )
        cont.invokeOnCancellation { disposable.dispose() }
    }

/** Survives a card leaving the LazyRow, so scrolling back does not refetch. */
private val linkPreviewCache = mutableMapOf<String, AmityLinkPreviewMetadata>()

private val CARD_HEIGHT = 480.dp
private val HEADER_HEIGHT = 52.dp
private val MEDIA_VERTICAL_PADDING = 8.dp

/** The preview's own image height; the card below it hugs its title and domain. */
private val PREVIEW_IMAGE_HEIGHT = 172.dp
private val ENGAGEMENT_HEIGHT = 36.dp

/** Caps, not budgets: the block is only as tall as the lines it actually draws. */
private val MEDIA_TEXT_MAX_HEIGHT = 96.dp

private const val TITLE_LINES_WITH_MEDIA = 1
private const val TITLE_LINES_ALONE = 4
private const val BODY_LINES_WITH_MEDIA = 2
private const val BODY_LINES_WITH_MEDIA_ALONE = 4
private const val BODY_LINES_ALONE = 64
private const val TITLE_LINES_WITH_PREVIEW = 1
private val PREVIEW_GAP = 12.dp

/** Beyond three the stack stops reading as a summary. */
private const val MAX_STACKED_REACTIONS = 3

/**
 * One post, drawn read-only. The whole card is a single click target: no reactions, no voting, no
 * comment entry, no menu, no playback — every interaction happens at the destination.
 *
 * The frame is a fixed 480 and the slots sum to it exactly. Short content leaves whitespace in the
 * text slot rather than shrinking the card, so a row of cards stays aligned.
 */
@Composable
internal fun AmityDiscoveryWidgetPostCardComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    post: AmityPost,
    onClick: () -> Unit,
) {
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "discovery_widget_post_card_component",
    ) {
        DiscoveryWidgetPostCardContent(
            modifier = modifier,
            post = post,
            onClick = onClick,
        )
    }
}

@Composable
private fun DiscoveryWidgetPostCardContent(
    modifier: Modifier = Modifier,
    post: AmityPost,
    onClick: () -> Unit,
) {
    val accessibleName = post.accessibleName(
        reactions = reactionCountLabel(post.getReactionCount()),
        comments = commentCountLabel(post.getCommentCount()),
    )
    val isMediaPost = post.hasMediaChild()
    val media = post.mediaThumbnail()
    val poll by post.pollOrNull()
    val hasText = post.postTitle().isNotBlank() || post.textContent().isNotBlank()
    val preview = if (!isMediaPost && poll == null) post.previewUrl() else null

    Column(
        modifier = modifier
            .height(CARD_HEIGHT)
            .clip(RoundedCornerShape(8.dp))
            .background(AmityTheme.colors.background)
            .border(1.dp, AmityTheme.colors.baseShade4, RoundedCornerShape(8.dp))
            .clickableWithoutRipple { onClick() }
            .semantics(mergeDescendants = true) {
                contentDescription = accessibleName
            },
    ) {
        Header(post)
        if (hasText) {
            TextBlock(
                post = post,
                withMedia = isMediaPost || poll != null,
                fills = preview != null,
                preview = preview,
                isPoll = poll != null,
            )
        }
        // The media takes whatever the caption left of the 480; a caption-less post simply leaves
        // it all, which is why no separate no-text rule is needed.
        if (poll != null) {
            AmityDiscoveryWidgetPollBlock(
                poll = poll!!,
                modifier = Modifier.weight(1f),
            )
        } else if (isMediaPost) {
            MediaBlock(
                image = media,
                showPlayIndicator = post.isVideoLike(),
                attachmentCount = post.attachmentCount(),
                modifier = Modifier.weight(1f),
            )
        } else if (!hasText) {
            // Only when there is no text block to take the slack. A second weighted child would
            // halve the space with it and truncate a caption that fits.
            Spacer(modifier = Modifier.weight(1f))
        }
        EngagementBar(post)
    }
}

@Composable
private fun Header(post: AmityPost) {
    // Avatar and content column both start at y 12; the 52 is 12 + 40 with no bottom padding.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(HEADER_HEIGHT)
            .padding(start = 16.dp, end = 16.dp, top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AmityUserAvatarView(size = 32.dp, user = post.getCreator())
        Column(
            modifier = Modifier.height(40.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                modifier = Modifier.height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = post.getCreator()?.getDisplayName().orEmpty(),
                    style = AmityTheme.typography.bodyBold.copy(color = AmityTheme.colors.base),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (post.getCreator()?.isBrand() == true) {
                    Image(
                        painter = painterResource(id = CommonComposeR.drawable.amity_ic_brand_badge),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                }
                post.getTarget().communityName()?.let { name ->
                    Icon(
                        painter = painterResource(id = CommonComposeR.drawable.amity_ic_chevron_right),
                        contentDescription = null,
                        tint = AmityTheme.colors.baseShade2,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = name,
                        style = AmityTheme.typography.bodyBold.copy(color = AmityTheme.colors.base),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (post.getTarget().isOfficialCommunity()) {
                        Icon(
                            painter = painterResource(id = CommonR.drawable.amity_ic_verified),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
            post.getCreatedAt()?.let { createdAt ->
                Text(
                    text = createdAt.readableSocialTimeDiff(),
                    style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.TextBlock(
    post: AmityPost,
    withMedia: Boolean,
    fills: Boolean = false,
    preview: String? = null,
    isPoll: Boolean = false,
) {
    val title = post.postTitle()
    val body = post.textContent()

    val hugs = withMedia && !fills
    val slot = when {
        !hugs -> Modifier.weight(1f)
        isPoll -> Modifier.height(MEDIA_TEXT_MAX_HEIGHT)
        else -> Modifier.heightIn(max = MEDIA_TEXT_MAX_HEIGHT)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(slot)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (title.isNotBlank()) {
            TitleText(
                text = title,
                style = AmityTheme.typography.titleBold.copy(
                    color = AmityTheme.colors.base,
                    textAlign = TextAlign.Start,
                ),
                maxLines = when {
                    preview != null -> TITLE_LINES_WITH_PREVIEW
                    hugs -> TITLE_LINES_WITH_MEDIA
                    else -> TITLE_LINES_ALONE
                },
            )
        }
        if (preview != null) {
            // Caption + Link: the preview keeps its own height at the bottom and the caption above
            // takes the slack, so the slack never lands between the text and the preview.
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(PREVIEW_GAP),
            ) {
                if (body.isNotBlank()) {
                    Box(modifier = Modifier.weight(1f)) {
                        BodyText(
                            text = body,
                            style = AmityTheme.typography.body.copy(color = AmityTheme.colors.base),
                            maxLines = BODY_LINES_ALONE,
                            highlights = post.highlightSpans(body),
                        )
                    }
                }
                LinkPreviewBlock(url = preview)
            }
        } else if (body.isNotBlank()) {
            if (hugs) {
                BodyText(
                    text = body,
                    style = AmityTheme.typography.body.copy(color = AmityTheme.colors.base),
                    maxLines = if (title.isNotBlank()) {
                        BODY_LINES_WITH_MEDIA
                    } else {
                        BODY_LINES_WITH_MEDIA_ALONE
                    },
                    highlights = post.highlightSpans(body),
                )
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    BodyText(
                        text = body,
                        style = AmityTheme.typography.body.copy(color = AmityTheme.colors.base),
                        maxLines = BODY_LINES_ALONE,
                        highlights = post.highlightSpans(body),
                    )
                }
            }
        }
    }
}

/** The lines that fit the slot, never more than the design's budget for that block. */
private fun lineCapFor(
    text: String,
    style: TextStyle,
    maxLines: Int,
    constraints: Constraints,
    measurer: TextMeasurer,
): Int {
    val widthPx = constraints.maxWidth
    if (!constraints.hasBoundedHeight || widthPx <= 0) return maxLines
    val probe = measurer.measure(
        text = AnnotatedString(text),
        style = style,
        constraints = Constraints(maxWidth = widthPx),
    )
    var fits = probe.lineCount
    while (fits > 1 && probe.getLineBottom(fits - 1) > constraints.maxHeight) fits--
    return minOf(maxLines, fits)
}

/**
 * A title ellipsizes and stops there: *See more* belongs to the body alone, and taking no
 * highlights keeps a mention out of a title that never renders one.
 */
@Composable
private fun TitleText(
    text: String,
    style: TextStyle,
    maxLines: Int,
) {
    val measurer = rememberTextMeasurer()
    BoxWithConstraints {
        val lineCap = remember(constraints.maxHeight, constraints.maxWidth, style, maxLines, text) {
            lineCapFor(text, style, maxLines, constraints, measurer)
        }
        Text(
            text = text,
            style = style,
            maxLines = lineCap,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * *See more* is decoration, not a control — it never expands anything, and the tap it receives is
 * the card's own. Drawn over the trailing end of the last line, on the card ground, so it reads as
 * following the ellipsis rather than as a separate row.
 */
@Composable
private fun BodyText(
    text: String,
    style: TextStyle,
    maxLines: Int,
    highlights: List<IntRange> = emptyList(),
) {
    val seeMore = amitySocialString("amity_social_button_see_more")
    val measurer = rememberTextMeasurer()
    val primary = AmityTheme.colors.primary
    val highlight = AmityTheme.colors.highlight

    BoxWithConstraints {
        val widthPx = constraints.maxWidth
        val density = LocalDensity.current
        // Where the block is height-constrained, the lines that fit decide the cap; where it hugs
        // (a media card), the caller's cap is the design's own budget.
        val lineCap = remember(constraints.maxHeight, style, maxLines, text, widthPx) {
            lineCapFor(text, style, maxLines, constraints, measurer)
        }
        val rendered = remember(text, lineCap, widthPx, style, seeMore, highlights) {
            val plain = text.withHighlights(highlights, text.length, highlight)
            if (widthPx <= 0) return@remember plain
            val laid = measurer.measure(
                text = plain,
                style = style,
                maxLines = lineCap,
                constraints = Constraints(maxWidth = widthPx),
            )
            if (!laid.hasVisualOverflow) return@remember plain

            val suffix = "\u2026 $seeMore"
            fun fits(end: Int): Boolean = !measurer.measure(
                text = AnnotatedString(text.substring(0, end).trimEnd() + suffix),
                style = style,
                maxLines = lineCap,
                constraints = Constraints(maxWidth = widthPx),
            ).hasVisualOverflow

            // Binary search the longest head the suffix still fits after; a per-character walk
            // measured the string hundreds of times for a long caption.
            var lo = 0
            var hi = laid.getLineEnd(lineCap - 1, visibleEnd = true).coerceAtMost(text.length)
            var best = -1
            while (lo <= hi) {
                val mid = (lo + hi) / 2
                if (fits(mid)) { best = mid; lo = mid + 1 } else { hi = mid - 1 }
            }
            // Nothing fits alongside the label — show the text ellipsized rather than nothing.
            if (best <= 0) return@remember plain

            buildAnnotatedString {
                val head = text.substring(0, best).trimEnd()
                append(text.withHighlights(highlights, head.length, highlight))
                append("\u2026 ")
                withStyle(SpanStyle(color = primary)) { append(seeMore) }
            }
        }

        Text(
            text = rendered,
            style = style,
            maxLines = lineCap,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun MediaBlock(
    image: AmityImage?,
    showPlayIndicator: Boolean,
    attachmentCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MEDIA_VERTICAL_PADDING)
            .background(AmityTheme.colors.baseShade4),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image?.getUrl(AmityImage.Size.MEDIUM))
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        // A cue that the destination has video. The widget never plays anything.
        if (showPlayIndicator) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_play),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
            )
        }
        // Decorative, like the counter and the play glyph: the widget shows frame 1 only, so this
        // says "there is more at the destination" rather than paging anything here.
        if (attachmentCount > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = CommonComposeR.drawable.amity_ic_chevron_right),
                    contentDescription = null,
                    tint = amityColorWhite,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        if (attachmentCount > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(width = 52.dp, height = 32.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "1/$attachmentCount",
                    style = AmityTheme.typography.body.copy(color = amityColorWhite),
                    maxLines = 1,
                )
            }
        }
    }
}

/**
 * A body URL gets a preview below the text. It is inside the card's single click target and never
 * opens the URL itself.
 */
@Composable
private fun LinkPreviewBlock(url: String) {
    var metadata by remember(url) { mutableStateOf(linkPreviewCache[url]) }
    LaunchedEffect(url) {
        if (metadata != null) return@LaunchedEffect
        runCatching {
            awaitPreview(url)
        }.onSuccess {
            linkPreviewCache[url] = it
            metadata = it
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(PREVIEW_IMAGE_HEIGHT)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(AmityTheme.colors.baseShade4),
        ) {
            metadata?.getImageUrl()?.let { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AmityTheme.colors.background)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = metadata?.getTitle().orEmpty(),
                style = AmityTheme.typography.bodyBold.copy(color = AmityTheme.colors.base),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = metadata?.getDomain().orEmpty(),
                style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun commentCountLabel(count: Int): String = amitySocialString(
    "amity_social_button_feed_comment_count_plural",
    count,
)

@Composable
private fun reactionCountLabel(count: Int): String = amitySocialString(
    "amity_social_label_reaction_count_plural",
    count.readableNumber(),
)

@Composable
private fun EngagementBar(post: AmityPost) {
    val commentCount = post.getCommentCount()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ENGAGEMENT_HEIGHT)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ReactionSummary(post)
        Text(
            text = commentCountLabel(commentCount),
            style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2),
            maxLines = 1,
        )
    }
}

@Composable
private fun ReactionSummary(post: AmityPost) {
    val total = post.getReactionCount()
    val glyphs = remember(post.getPostId(), total) {
        post.getReactionMap()
            .filterValues { it > 0 }
            .entries
            .sortedByDescending { it.value }
            .take(MAX_STACKED_REACTIONS)
            .map { AmitySocialReactions.toReaction(it.key).icon }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (total > 0) {
            glyphs.forEachIndexed { index, icon ->
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .offset(x = (-4 * index).dp)
                        .size(20.dp),
                )
            }
        }
        Text(
            text = if (total > 0) {
                total.readableNumber()
            } else {
                reactionCountLabel(total)
            },
            style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2),
            maxLines = 1,
            modifier = Modifier.padding(start = if (total > 0 && glyphs.isNotEmpty()) 4.dp else 0.dp),
        )
    }
}

private fun AmityPost.postTitle(): String = when (val data = getData()) {
    is AmityPost.Data.TEXT -> data.getTitle()
    else -> getChildren()
        .firstNotNullOfOrNull { (it.getData() as? AmityPost.Data.TEXT)?.getTitle() }
        .orEmpty()
}

private fun AmityPost.textContent(): String = when (val data = getData()) {
    is AmityPost.Data.TEXT -> data.getText().orEmpty()
    else -> getChildren()
        .firstNotNullOfOrNull { (it.getData() as? AmityPost.Data.TEXT)?.getText() }
        ?: (getData() as? AmityPost.Data.TEXT)?.getText().orEmpty()
}

private fun AmityPost.isVideoLike(): Boolean =
    getData() is AmityPost.Data.VIDEO || getData() is AmityPost.Data.CLIP ||
        getChildren().any { it.getData() is AmityPost.Data.VIDEO || it.getData() is AmityPost.Data.CLIP }

/** The first drawable thumbnail, whether the post carries it directly or through a child. */
private fun AmityPost.hasMediaChild(): Boolean {
    fun isMedia(post: AmityPost) = when (post.getType()) {
        is AmityPost.DataType.IMAGE,
        is AmityPost.DataType.VIDEO,
        is AmityPost.DataType.CLIP,
        -> true

        else -> false
    }
    return isMedia(this) || getChildren().any { isMedia(it) }
}

private fun AmityPost.mediaThumbnail(): AmityImage? {
    fun of(data: AmityPost.Data): AmityImage? = when (data) {
        is AmityPost.Data.IMAGE -> data.getImage()
        is AmityPost.Data.VIDEO -> data.getThumbnailImage()
        is AmityPost.Data.CLIP -> data.getThumbnailImage()
        else -> null
    }
    return of(getData()) ?: getChildren().firstNotNullOfOrNull { of(it.getData()) }
}

private fun AmityPost.Target.communityName(): String? =
    (this as? AmityPost.Target.COMMUNITY)?.getCommunity()?.getDisplayName()

private fun AmityPost.attachmentCount(): Int =
    getChildren().count { it.getData() !is AmityPost.Data.TEXT }.coerceAtLeast(1)

private fun AmityPost.Target.isOfficialCommunity(): Boolean =
    (this as? AmityPost.Target.COMMUNITY)?.getCommunity()?.isOfficial() == true

/** The first URL in the body drives the preview. */
private fun AmityPost.previewUrl(): String? =
    textContent().extractUrls().firstOrNull()?.url

/** Reading order, and untruncated -- what is visible is not what is announced. */
private fun AmityPost.accessibleName(reactions: String, comments: String): String = listOfNotNull(
    getCreator()?.getDisplayName(),
    getTarget().communityName(),
    postTitle().takeIf { it.isNotBlank() },
    textContent().takeIf { it.isNotBlank() },
    reactions,
    comments,
).joinToString(", ")

/**
 * Hashtags are highlighted but never tappable here -- the whole card is one target, so a span that
 * looked interactive would promise something the widget does not do.
 *
 * The span runs index..index+length+1 because the stored length excludes the leading '#', matching
 * how the feed's own renderer reads the same metadata.
 */
private fun String.withHighlights(
    spans: List<IntRange>,
    limit: Int,
    color: Color,
): AnnotatedString = buildAnnotatedString {
    val head = take(limit)
    append(head)
    spans.forEach { span ->
        val end = (span.last + 1).coerceAtMost(head.length)
        if (span.first in 0 until end) {
            addStyle(SpanStyle(color = color), span.first, end)
        }
    }
}

/**
 * Hashtags, mentions and links share one highlight colour and one rule: coloured, never tappable.
 *
 * Metadata indices address the post's own data.text, so a child's text must not be styled with them.
 * Each span runs index..index+length inclusive of the leading sigil, matching the feed's reader.
 */
private fun AmityPost.highlightSpans(body: String): List<IntRange> {
    if (getData() !is AmityPost.Data.TEXT) return emptyList()
    val metadata = getMetadata() ?: JsonObject()
    val spans = mutableListOf<IntRange>()
    AmityHashtagMetadataGetter(metadata).getHashtags().forEach {
        spans += it.getIndex()..(it.getIndex() + it.getLength())
    }
    val mentions = AmityMentionMetadataGetter(metadata)
    mentions.getMentionedUsers().forEach {
        spans += it.getIndex()..(it.getIndex() + it.getLength())
    }
    mentions.getMentionedChannels().forEach {
        spans += it.getIndex()..(it.getIndex() + it.getLength())
    }
    // The URL is highlighted in place as well as driving the preview below the text.
    body.extractUrls().forEach { spans += it.start until it.end }
    return spans
}

/**
 * The poll arrives through the pool's own polls collection, so this reads the cache the persister
 * already filled rather than issuing a second request. Null until the first emission.
 */
@Composable
private fun AmityPost.pollOrNull(): State<AmityPoll?> {
    // A poll post's parent carries the caption as TEXT; the poll itself is on a child, exactly as
    // an image post carries its image.
    val data = (getData() as? AmityPost.Data.POLL)
        ?: getChildren().firstNotNullOfOrNull { it.getData() as? AmityPost.Data.POLL }
        ?: return remember { mutableStateOf(null) }
    val state = remember(data.getPollId()) { mutableStateOf<AmityPoll?>(null) }
    DisposableEffect(data.getPollId()) {
        val disposable = data.getPoll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state.value = it }, { })
        onDispose { disposable.dispose() }
    }
    return state
}
