package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.image.rememberZoomState
import com.amity.socialcloud.uikit.common.ui.image.zoomable
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBase
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBaseShade4
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.AmityProductTagBadge
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia

/**
 * Full-screen image viewer for the composer's pre-upload attachments.
 *
 * The composer only ever holds local [AmityPostMedia] (a Uri, no post id yet), while the
 * published-post viewer (AmityPostMediaPreviewDialog) pages over uploaded AmityPost children --
 * so that dialog can't be reused here without teaching it a second, local-media code path
 * through every branch (alt-text menu, product-tag resolution, etc). This mirrors its
 * pager/zoom/dismiss idioms for the composer's own local list instead, leaving the published
 * viewer untouched.
 */
@Composable
fun AmitySelectedMediaPreviewDialog(
    modifier: Modifier = Modifier,
    mediaList: List<AmityPostMedia>,
    initialMediaKey: String,
    mediaProductTags: Map<String, List<AmityProduct>> = emptyMap(),
    isProductCatalogueEnabled: Boolean = false,
    onDismiss: () -> Unit,
    onTagProductClick: (AmityPostMedia) -> Unit = {},
) {
    if (mediaList.isEmpty()) {
        onDismiss()
        return
    }

    var verticalDragAmount by remember { mutableFloatStateOf(0f) }
    val initialPage = remember {
        mediaList.indexOfFirst { it.url.toString() == initialMediaKey }.coerceAtLeast(0)
    }
    val pagerState = rememberPagerState(initialPage = initialPage) { mediaList.size }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        AmityBaseComponent(
            componentId = "selected_media_preview",
            needScaffold = true,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    key = { it },
                    modifier = modifier
                        .fillMaxSize()
                        .background(amityMediaSurface)
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    if (verticalDragAmount > 0) {
                                        onDismiss()
                                    }
                                    verticalDragAmount = 0f
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                verticalDragAmount += dragAmount
                            }
                        }
                ) { index ->
                    val media = mediaList.getOrNull(index)
                    var aspectRatio by remember { mutableStateOf<Float?>(null) }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (media != null) {
                            val imageBoxModifier = if (aspectRatio != null) {
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(aspectRatio!!)
                            } else {
                                Modifier.fillMaxSize()
                            }

                            Box(modifier = imageBoxModifier) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(media.url)
                                        .crossfade(true)
                                        .networkCachePolicy(CachePolicy.ENABLED)
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .memoryCachePolicy(CachePolicy.ENABLED)
                                        .build(),
                                    contentDescription = "Image ${index + 1} of ${mediaList.size}",
                                    contentScale = ContentScale.Fit,
                                    onSuccess = { result ->
                                        val size = result.painter.intrinsicSize
                                        if (size.width > 0 && size.height > 0) {
                                            aspectRatio = size.width / size.height
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .zoomable(rememberZoomState()),
                                )

                                val pageTagCount = mediaProductTags[media.id]?.size ?: 0
                                if (isProductCatalogueEnabled && pageTagCount > 0) {
                                    AmityProductTagBadge(
                                        count = pageTagCount,
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(end = 12.dp, bottom = 12.dp),
                                        onClick = { onTagProductClick(media) }
                                    )
                                }
                            }
                        }
                    }
                }

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(amityColorBlack.copy(alpha = 0.5f)),
                ) {
                    val (closeBtn, counter) = createRefs()

                    AmityMenuButton(
                        size = 32.dp,
                        iconPadding = 8.dp,
                        background = amityColorBaseShade4,
                        tint = amityColorBase,
                        modifier = Modifier
                            .zIndex(Float.MAX_VALUE)
                            .constrainAs(closeBtn) {
                                top.linkTo(parent.top, margin = 16.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                            },
                    ) {
                        onDismiss()
                    }

                    Text(
                        text = "${pagerState.currentPage + 1} / ${mediaList.size}",
                        style = AmityTheme.typography.titleLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = amityColorWhite
                        ),
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Photo ${pagerState.currentPage + 1} of ${mediaList.size}"
                            }
                            .constrainAs(counter) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(closeBtn.bottom)
                            }
                    )
                }

            }

            BackHandler {
                onDismiss()
            }
        }
    }
}
