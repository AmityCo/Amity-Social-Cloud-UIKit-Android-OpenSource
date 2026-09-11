package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.AmityMediaRatio
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.RenderModeEnum
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import kotlinx.coroutines.launch

@Composable
fun AmityPostMediaElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    clipClick: (childPost: AmityPost) -> Unit = {},
    refreshKey: Int = 0,
) {
    val postChildren = remember(
        post.getPostId(),
        post.getEditedAt(),
        post.getUpdatedAt(),
        post.getChildren().size
    ) {
        post.getChildren()
    }
    if (postChildren.isEmpty()) return

    when (postChildren.first().getData()) {
        is AmityPost.Data.IMAGE,
        is AmityPost.Data.VIDEO,
        is AmityPost.Data.CLIP,
            -> {
        }

        else -> return
    }

    // Full-bleed and square-cornered whether the post holds one attachment or ten, so media reads
    // the same way in the feed regardless of how much of it there is.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        when (postChildren.first().getData()) {
            is AmityPost.Data.IMAGE -> AmityChildPostMediaElement(
                post = post,
                isVideoPost = false,
                refreshKey = refreshKey,
            )

            is AmityPost.Data.VIDEO -> AmityChildPostMediaElement(
                post = post,
                isVideoPost = true,
                refreshKey = refreshKey,
            )

            is AmityPost.Data.CLIP -> AmityChildPostMediaElement(
                modifier = modifier.aspectRatio(9 / 16f),
                post = post,
                clipClick = {
                    clipClick(it)
                },
                isVideoPost = true,
                refreshKey = refreshKey,
            )

            else -> {}
        }
    }
}

@Composable
fun AmityChildPostMediaElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    clipClick: (AmityPost) -> Unit = {},
    isVideoPost: Boolean,
    refreshKey: Int = 0,
) {
    val postChildrenList = remember(post.getPostId(), post.getUpdatedAt()) {
        post.getChildren().take(10)
    }

    val showMediaDialog = remember { mutableStateOf(false) }
    val selectedFileId = remember { mutableStateOf("") }

    // Carousel page, hoisted so the full-screen viewer can report back which frame it ended on
    // and the carousel can scroll to it on dismiss, instead of always landing on the entry frame.
    // Keyed on refreshKey so a refresh starts a fresh pager state instead of restoring the old page.
    val pagerState = key(refreshKey) {
        rememberPagerState(pageCount = { postChildrenList.size })
    }
    val coroutineScope = rememberCoroutineScope()
    var lastViewedFileId by remember { mutableStateOf<String?>(null) }

    // State for product tag bottom sheet
    var showProductTagSheet by remember { mutableStateOf(false) }
    var selectedProducts by remember { mutableStateOf<List<AmityProduct>>(emptyList()) }
    var selectedProduct by remember { mutableStateOf<AmityProduct?>(null) }
    val disposables = remember { io.reactivex.rxjava3.disposables.CompositeDisposable() }
    val context = LocalContext.current
    val behavior = AmitySocialBehaviorHelper.globalBehavior

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            disposables.clear()
        }
    }

    val onMediaDialogDismiss: () -> Unit = {
        showMediaDialog.value = false
        lastViewedFileId?.let { fileId ->
            val index = postChildrenList.indexOfFirst { it.getPostId() == fileId }
            if (index >= 0) {
                coroutineScope.launch { pagerState.scrollToPage(index) }
            }
        }
    }
    val onMediaDialogPageChanged: (String) -> Unit = { fileId -> lastViewedFileId = fileId }

    if (showMediaDialog.value && selectedFileId.value.isNotEmptyOrBlank()) {
        if (isVideoPost) {
            // Use AmityVideoPlayerPage for video posts
            AmityVideoPlayerPage(
                childPosts = post.getChildren(),
                selectedFileId = selectedFileId.value,
                post = post,
                onDismiss = onMediaDialogDismiss,
                onPageChanged = onMediaDialogPageChanged,
            )
        } else {
            // Use AmityPostMediaPreviewDialog for image posts
            AmityPostMediaPreviewDialog(
                childPosts = post.getChildren(),
                isVideoPost = isVideoPost,
                isPostCreator = post.getCreatorId() == AmityCoreClient.getUserId(),
                selectedFileId = selectedFileId.value,
                onDismiss = onMediaDialogDismiss,
                onPageChanged = onMediaDialogPageChanged,
            )
        }
    }

    // Product tag bottom sheet
    if (showProductTagSheet && selectedProducts.isNotEmpty()) {
        AmityProductTagListComponent(
            productTags = selectedProducts,
            renderMode = if (isVideoPost) RenderModeEnum.VIDEO else RenderModeEnum.IMAGE,
            onDismiss = { showProductTagSheet = false },
            onProductClick = { product ->
                val handled = behavior.onPostProductTagClick(
                    AmityGlobalBehavior.Context(
                        pageContext = context,
                        product = product,
                    )
                )
                if (!handled) {
                    selectedProduct = product
                }
            },
        )
    }

    selectedProduct?.let { p ->
        AmityProductWebViewBottomSheet(
            product = p,
            onDismiss = {
                selectedProduct = null
            }
        )
    }

    // Callback for product tag badge click
    val onProductTagClick: (AmityPost) -> Unit = { childPost ->
        val mediaTags = childPost.getProductTags().filterIsInstance<AmityProductTag.Media>()
        val productIds = mediaTags.map { it.productId }

        if (productIds.isNotEmpty()) {
            // Query products from productIds
            val disposable = io.reactivex.rxjava3.core.Observable.fromIterable(productIds)
                .flatMapSingle { productId ->
                    AmityCoreClient.newProductRepository()
                        .getProduct(productId)
                        .firstOrError()
                }
                .toList()
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                    { products ->
                        selectedProducts = products
                        showProductTagSheet = true
                    },
                    { error ->
                        android.util.Log.e("ProductTagBadge", "Error fetching products", error)
                    }
                )
            disposables.add(disposable)
        }
    }

    val onFrameClick: (AmityPost) -> Unit = {
        if (it.getData() is AmityPost.Data.CLIP) {
            clipClick(it)
        } else {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }
    }

    if (postChildrenList.size <= 1) {
        postChildrenList.firstOrNull()?.let { onlyChild ->
            AmityPostMediaImageChildrenOne(
                modifier = modifier,
                isVideoPost = isVideoPost,
                postChild = onlyChild,
                onProductTagClick = onProductTagClick,
                onClick = onFrameClick,
            )
        }
    } else {
        AmityPostMediaCarousel(
            postChildren = postChildrenList,
            isVideoPost = isVideoPost,
            pagerState = pagerState,
            onProductTagClick = onProductTagClick,
            onClick = onFrameClick,
        )
    }
}

@Composable
fun AmityPostMediaImageChildrenOne(
    modifier: Modifier = Modifier,
    postChild: AmityPost,
    isVideoPost: Boolean,
    onProductTagClick: (AmityPost) -> Unit = {},
    onClick: (AmityPost) -> Unit,
) {
    val productTagCount = remember(postChild.getPostId(), postChild.getUpdatedAt()) {
        getProductTagCount(postChild)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        AmityPostImageView(
            modifier = Modifier
                .semantics {
                    role = Role.Image
                    contentDescription =
                        if (isVideoPost) "Video 1 of 1" else "Photo 1 of 1: ${getAltText(postChild)}"
                },
            post = postChild,
            onClick = { onClick(postChild) }
        )
        if (isVideoPost) {
            AmityPostMediaPlayButton(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        AmityProductTagBadge(
            count = productTagCount,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 12.dp),
            onClick = { onProductTagClick(postChild) }
        )
    }
}

/**
 * Multi-attachment carousel: one full-width frame per child post (up to 10), swipeable,
 * with a locked aspect ratio, an "n/total" counter, and a pagination indicator below.
 */
@Composable
fun AmityPostMediaCarousel(
    modifier: Modifier = Modifier,
    postChildren: List<AmityPost>,
    isVideoPost: Boolean,
    pagerState: PagerState = rememberPagerState(pageCount = { postChildren.size }),
    onProductTagClick: (AmityPost) -> Unit = {},
    onClick: (AmityPost) -> Unit,
) {
    val frameRatio = rememberFrameAspectRatio(postChildren.firstOrNull())

    val productTagCounts = remember(
        postChildren.joinToString(separator = "|") { "${it.getPostId()}@${it.getUpdatedAt()}" }
    ) {
        postChildren.map { getProductTagCount(it) }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(frameRatio)
        ) {
            HorizontalPager(
                state = pagerState,
                key = { postChildren[it].getPostId() },
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val child = postChildren[page]
                Box(modifier = Modifier.fillMaxSize()) {
                    AmityPostImageView(
                        modifier = Modifier
                            .semantics {
                                role = Role.Image
                                contentDescription = if (isVideoPost) {
                                    "Video ${page + 1} of ${postChildren.size}"
                                } else {
                                    "Photo ${page + 1} of ${postChildren.size}: ${getAltText(child)}"
                                }
                            },
                        post = child,
                        onClick = { onClick(child) }
                    )
                    if (isVideoPost) {
                        AmityPostMediaPlayButton(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    AmityProductTagBadge(
                        count = productTagCounts.getOrElse(page) { 0 },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 12.dp),
                        onClick = { onProductTagClick(child) }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .background(
                        color = amityColorBlack.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${postChildren.size}",
                    style = AmityTheme.typography.body.copy(color = amityColorWhite)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AmityCarouselPaginationIndicator(
            pageCount = postChildren.size,
            currentPage = pagerState.currentPage,
        )
    }
}

private enum class AmityCarouselDotState { ACTIVE, INACTIVE, EDGE }

/**
 * Row of at most six elements below the carousel. Up to six attachments every page gets a full-size
 * dot. Beyond that a sliding window of full dots tracks the current page and small edge dots are
 * appended outside it to signal more content that way, so the strip is always six wide.
 */
@Composable
private fun AmityCarouselPaginationIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
) {
    if (pageCount <= 1) return

    val window = carouselIndicatorWindow(pageCount = pageCount, currentPage = currentPage)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Edge dots always hold their slot so the strip's width never jumps; they fade and slide in
        // from their own side instead of popping.
        AmityCarouselEdgeDot(visible = window.showLeft, fromLeft = true)
        for (page in window.start..window.end) {
            AmityCarouselDot(
                if (page == currentPage) {
                    AmityCarouselDotState.ACTIVE
                } else {
                    AmityCarouselDotState.INACTIVE
                }
            )
        }
        AmityCarouselEdgeDot(visible = window.showRight, fromLeft = false)
    }
}

@Composable
private fun AmityCarouselEdgeDot(visible: Boolean, fromLeft: Boolean) {
    val offsetFrom = if (fromLeft) -CAROUSEL_EDGE_DOT_SLIDE else CAROUSEL_EDGE_DOT_SLIDE
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(CAROUSEL_EDGE_DOT_ANIM_MS),
        label = "carouselEdgeDotAlpha",
    )
    val offsetX by animateDpAsState(
        targetValue = if (visible) 0.dp else offsetFrom,
        animationSpec = tween(CAROUSEL_EDGE_DOT_ANIM_MS),
        label = "carouselEdgeDotOffset",
    )
    Box(
        modifier = Modifier
            .size(3.dp)
            .offset(x = offsetX)
            .alpha(alpha)
            .clip(CircleShape)
            .background(AmityTheme.colors.baseShade4)
    )
}

@Composable
private fun AmityCarouselDot(state: AmityCarouselDotState) {
    val size = if (state == AmityCarouselDotState.EDGE) 3.dp else 6.dp
    val color = when (state) {
        AmityCarouselDotState.ACTIVE -> AmityTheme.colors.base
        AmityCarouselDotState.INACTIVE, AmityCarouselDotState.EDGE -> AmityTheme.colors.baseShade4
    }
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

private const val CAROUSEL_EDGE_DOT_ANIM_MS = 300
private val CAROUSEL_EDGE_DOT_SLIDE = 4.dp

private data class AmityCarouselWindow(
    val start: Int,
    val end: Int,
    val showLeft: Boolean,
    val showRight: Boolean,
)

/**
 * The window of pages that get a full-size dot. Near either end it holds five, in the middle four —
 * so together with the edge dots the strip is always six. In the middle the window starts one page
 * back, which puts the active dot second of the four.
 */
private fun carouselIndicatorWindow(pageCount: Int, currentPage: Int): AmityCarouselWindow {
    if (pageCount <= 6) {
        return AmityCarouselWindow(
            start = 0,
            end = pageCount - 1,
            showLeft = false,
            showRight = false,
        )
    }

    val start: Int
    val end: Int
    when {
        currentPage <= 1 -> {
            start = 0
            end = 4
        }

        currentPage >= pageCount - 3 -> {
            start = pageCount - 5
            end = pageCount - 1
        }

        else -> {
            start = currentPage - 1
            end = currentPage + 2
        }
    }

    return AmityCarouselWindow(
        start = start,
        end = end,
        showLeft = start > 0,
        showRight = end < pageCount - 1,
    )
}

/**
 * Dimensions the post already carries, with no fetch. Images have them; a video's poster does not,
 * which is the whole reason [rememberFrameAspectRatio] exists.
 */
private fun syncDimensions(child: AmityPost?): Pair<Int, Int>? = when (val data = child?.getData()) {
    is AmityPost.Data.IMAGE -> data.getImage()?.let { it.getWidth() to it.getHeight() }
    is AmityPost.Data.VIDEO -> data.getThumbnailImage()?.let { it.getWidth() to it.getHeight() }
    is AmityPost.Data.CLIP -> data.getThumbnailImage()?.let { it.getWidth() to it.getHeight() }
    else -> null
}?.takeIf { it.first > 0 && it.second > 0 }

/**
 * Ratio is classified once from the first attachment's own dimensions and locked for every frame,
 * so the carousel's height is known before any image loads and never jumps between slides.
 *
 * Dimensions come from the media's own record. For video that record is only reachable
 * asynchronously; images resolve on the first pass and never reach the fetch.
 *
 * That record states the stored frame plus the turn to apply to it, so a rotated video only has its
 * real shape once AmityMediaRatio.dimensionsOf has applied the turn -- which is why nothing here
 * reads a video's width and height directly.
 */
@Composable
private fun rememberFrameAspectRatio(firstChild: AmityPost?): Float {
    val postId = firstChild?.getPostId()
    val known = remember(postId) { syncDimensions(firstChild) }
    val videoData = firstChild?.getData() as? AmityPost.Data.VIDEO

    val dimensions by produceState(known, postId, known, videoData) {
        if (known != null || videoData == null) return@produceState
        // getVideo() answers a source that never emits when the file is absent from the local store,
        // so the timeout is what turns "will never answer" into an observable event.
        val subscription = videoData.getVideo()
            .subscribeOn(Schedulers.io())
            .timeout(VIDEO_LOOKUP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .subscribe(
                { video ->
                    val resolved = AmityMediaRatio.dimensionsOf(video)
                    if (resolved == null) {
                        Log.w(TAG, "video record for $postId carries no dimensions — frame stays 1:1")
                    }
                    value = resolved
                },
                { error -> Log.w(TAG, "could not read the video record for $postId — frame stays 1:1", error) },
            )
        awaitDispose { subscription.dispose() }
    }

    return AmityMediaRatio.classify(dimensions?.first ?: 0, dimensions?.second ?: 0)
}

private const val VIDEO_LOOKUP_TIMEOUT_SECONDS = 10L

private const val TAG = "AmityMediaRatio"

fun getAltText(post: AmityPost): String {
    return (post.getData() as? AmityPost.Data.IMAGE)?.getImage()?.getAltText()
        ?: DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_no_description_available")
}

fun getProductTagCount(childPost: AmityPost): Int {
    return childPost.getProductTags()
        .filterIsInstance<AmityProductTag.Media>()
        .size
}

fun getProductsFromTags(childPost: AmityPost): List<AmityProduct> {
    return childPost.getProductTags()
        .filterIsInstance<AmityProductTag.Media>()
        .mapNotNull { it.product }
}

@Composable
fun AmityProductTagBadge(
    count: Int,
    modifier: Modifier = Modifier,
    showWhenEmpty: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    if (count <= 0 && !showWhenEmpty) return

    Row(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .background(
                color = amityColorBlack.copy(alpha = 0.5f),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = if (count > 0) 6.dp else 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (count > 0) {
                    CommonR.drawable.amity_ic_product_tag_filled
                } else {
                    CommonR.drawable.amity_ic_product_tag
                }
            ),
            contentDescription = "Product tags",
            tint = amityColorWhite,
            modifier = Modifier.size(16.dp)
        )
        if (count > 0) {
            Text(
                text = count.toString(),
                style = AmityTheme.typography.caption.copy(
                    color = amityColorWhite
                )
            )
        }
    }
}

@Composable
fun AmityPostMediaPlayButton(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = CommonComposeR.drawable.amity_ic_play_v4),
        contentDescription = null,
        modifier = modifier.size(40.dp)
    )
}
