package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.MediaSource
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.engine.analytics.AnalyticsEventSourceType
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityAddProductBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductTaggingBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.RenderModeEnum
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostVideoPlayerHelper
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import com.amity.socialcloud.uikit.community.compose.utils.sharePost
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import java.util.Locale

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AmityVideoPlayerPage(
    modifier: Modifier = Modifier,
    childPosts: List<AmityPost> = emptyList(),
    selectedFileId: String,
    showMenuButton: Boolean = false,
    recordedUrls: List<String> = emptyList(),
    onDismiss: () -> Unit,
    onProductsUpdated: ((List<AmityProduct>) -> Unit)? = null,
    onViewOriginalPost: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner provided for AmityVideoPlayerPage"
    }
    val viewModel = viewModel<AmityVideoPlayerViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AmityVideoPlayerViewModel() as T
            }
        }
    )

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(15_000)
            .setSeekForwardIncrementMs(15_000)
            .setPauseAtEndOfMediaItems(true)
            .build()
    }

    var isAudioMuted by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var playerState by remember { mutableIntStateOf(ExoPlayer.STATE_IDLE) }

    var verticalDragAmount by remember { mutableFloatStateOf(0f) }

    // Product tag state
    var showProductTagSheet by remember { mutableStateOf(false) }
    var showAddProductBottomSheet by remember { mutableStateOf(false) }
    var selectedProducts by remember { mutableStateOf<List<AmityProduct>>(emptyList()) }
    var selectedProduct by remember { mutableStateOf<AmityProduct?>(null) }
    val disposables = remember { CompositeDisposable() }

    // Sync selectedProducts from ViewModel after add/remove operations
    val taggedProductsFromVM by viewModel.taggedProducts.collectAsState()
    val isProductCatalogueEnabled by viewModel.isProductCatalogueEnabled.collectAsState()

    // Update selectedProducts when ViewModel emits new value (after add/remove)
    LaunchedEffect(taggedProductsFromVM) {
        taggedProductsFromVM?.let {
            selectedProducts = it
            onProductsUpdated?.invoke(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProductCatalogueSettings()
    }

    // Menu bottom sheet state
    var showMenuSheet by remember { mutableStateOf(false) }

    // Check if this is a recorded room post (recordedUrls is provided)
    val isRecordedRoomPost = remember(recordedUrls) { recordedUrls.isNotEmpty() }

    val videoPosts = remember(childPosts) {
        childPosts.filter { it.getData() is AmityPost.Data.VIDEO }
    }

    // For recorded room posts, get the ROOM child post to read its products
    val roomChildPost = remember(childPosts, isRecordedRoomPost) {
        if (isRecordedRoomPost) childPosts.find { it.getData() is AmityPost.Data.ROOM } else null
    }

    val isHost = AmityCoreClient.getUserId() == roomChildPost?.getCreatorId()

    val pagerState = rememberPagerState {
        if (isRecordedRoomPost) 1 else videoPosts.size
    }

    val videos by remember(videoPosts.size, isRecordedRoomPost) {
        if (isRecordedRoomPost) {
            Single.just(emptyList<AmityVideo>())
        } else {
            prepareVideoUrl(videoPosts.map { it.getData() })
        }
    }.subscribeAsState(initial = emptyList())

    // Scroll to selected video (only for video posts)
    LaunchedEffect(selectedFileId, isRecordedRoomPost) {
        if (!isRecordedRoomPost) {
            val index = videoPosts.indexOfFirst { it.getPostId() == selectedFileId }
            if (index >= 0) {
                pagerState.scrollToPage(index)
            }
        }
    }

    // Setup player and helper
    LaunchedEffect(exoPlayer) {
        AmityPostVideoPlayerHelper.setup(exoPlayer)
    }

    // Add videos to player helper when available
    LaunchedEffect(videos, recordedUrls, isRecordedRoomPost) {
        if (isRecordedRoomPost && recordedUrls.isNotEmpty()) {
            // Use HlsMediaSource with authentication for recorded room posts
            val mediaSource = getRecordedMediaSource(recordedUrls)
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        } else if (videos.isNotEmpty()) {
            AmityPostVideoPlayerHelper.add(videos)
        }
    }

    // Play video when page changes (only for video posts)
    LaunchedEffect(pagerState.currentPage, videos, isRecordedRoomPost) {
        if (!isRecordedRoomPost && videos.isNotEmpty()) {
            delay(100)
            AmityPostVideoPlayerHelper.playMediaItem(pagerState.currentPage)
            exoPlayer.play()
        }
    }

    // Initialize selectedProducts from room post products
    LaunchedEffect(roomChildPost) {
        if (isRecordedRoomPost) {
            selectedProducts = roomChildPost?.getProducts().orEmpty()
        }
    }

    // Setup player listener
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                playerState = playbackState
                isPlaying = exoPlayer.isPlaying
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
            AmityPostVideoPlayerHelper.clear()
            disposables.clear()
        }
    }

    // Product tag click handler
    val onProductTagClick: (AmityPost) -> Unit = { childPost ->
        val mediaTags = childPost.getProductTags().filterIsInstance<AmityProductTag.Media>()
        val productIds = mediaTags.map { it.productId }

        if (productIds.isNotEmpty()) {
            val disposable = io.reactivex.rxjava3.core.Observable.fromIterable(productIds)
                .flatMapSingle { productId ->
                    AmityCoreClient.newProductRepository()
                        .getProduct(productId)
                        .firstOrError()
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        AmityBaseComponent(
            componentId = "video_player_page",
            needScaffold = true,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (isRecordedRoomPost) 0.dp else 100.dp)
                    .background(Color.Black)
            ) {
                HorizontalPager(
                    state = pagerState,
                    key = { it },
                    modifier = modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    if (verticalDragAmount > 100) {
                                        onDismiss()
                                    }
                                    verticalDragAmount = 0f
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                verticalDragAmount += dragAmount
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures {
                                if (isPlaying) {
                                    exoPlayer.pause()
                                } else {
                                    exoPlayer.play()
                                }
                            }
                        }
                ) { index ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AmityStoryVideoPlayer(
                            exoPlayer = exoPlayer,
                            isVisible = pagerState.currentPage == index,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Play/Pause button overlay
                if (!isPlaying && playerState == ExoPlayer.STATE_READY) {
                    Image(
                        painter = painterResource(R.drawable.amity_ic_play_v4),
                        contentDescription = "Play",
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.Center)
                            .clickableWithoutRipple {
                                exoPlayer.play()
                            }
                    )
                }

                // Toolbar (top)
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .zIndex(Float.MAX_VALUE)
                ) {
                    val (closeBtn, muteBtn, menuBtn) = createRefs()

                    // Close button
                    AmityMenuButton(
                        size = 32.dp,
                        iconPadding = 8.dp,
                        modifier = Modifier.constrainAs(closeBtn) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                        background = if (isRecordedRoomPost) Color.Transparent
                        else Color.Black.copy(alpha = 0.5f)
                    ) {
                        onDismiss()
                    }

                    // Mute/Unmute button
                    Image(
                        painter = painterResource(
                            id = if (isAudioMuted) R.drawable.amity_ic_audio_mute_outline
                            else R.drawable.amity_ic_audio_unmute_outline
                        ),
                        contentDescription = "Audio Toggle",
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(muteBtn) {
                                top.linkTo(parent.top)
                                end.linkTo(if (showMenuButton && (isProductCatalogueEnabled || isHost)) menuBtn.start else parent.end, margin = 8.dp)
                            }
                            .clickableWithoutRipple {
                                isAudioMuted = !isAudioMuted
                                exoPlayer.volume = if (isAudioMuted) 0f else 1f
                            },
                    )

                    // Menu button (3 dots)
                    if (showMenuButton && (isProductCatalogueEnabled || isHost)) {
                        AmityMenuButton(
                            icon = if (isRecordedRoomPost) R.drawable.amity_ic_more_vertical else R.drawable.amity_ic_more_horiz,
                            size = 32.dp,
                            iconPadding = 2.dp,
                            modifier = Modifier.constrainAs(menuBtn) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            },
                        ) {
                            showMenuSheet = true
                        }
                    }
                }

                // Bottom section: Product tag + SeekBar
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp)
                ) {
                    // Product Tag Badge at bottom-right
                    val currentProductTagCount by remember {
                        derivedStateOf {
                            if (isRecordedRoomPost) {
                                selectedProducts.size
                            } else {
                                videoPosts.getOrNull(pagerState.currentPage)?.let { getProductTagCount(it) } ?: 0
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        AmityProductTagBadge(
                            count = currentProductTagCount,
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = {
                                if (isRecordedRoomPost) {
                                    if (selectedProducts.isNotEmpty()) {
                                        showProductTagSheet = true
                                    }
                                } else {
                                    videoPosts.getOrNull(pagerState.currentPage)?.let { currentPost ->
                                        onProductTagClick(currentPost)
                                    }
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Seek bar
                    VideoSeekBar(
                        exoPlayer = exoPlayer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            // Product tag bottom sheet
            if (showProductTagSheet) {
                if (isRecordedRoomPost) {
                    AmityProductTaggingBottomSheet(
                        pageScope = getPageScope(),
                        onDismiss = {
                            showProductTagSheet = false
                        },
                        onRemoveProduct = {
                            viewModel.removeTaggedProduct(
                                postId = roomChildPost?.getPostId() ?: "",
                                productId = it,
                                currentProducts = selectedProducts,
                            )
                        },
                        onAddProducts = {
                            showAddProductBottomSheet = true
                        },
                        onProductClick = { product, location ->
                            selectedProduct = product
                            roomChildPost?.let {
                                product.analytics()
                                    .markAsClicked(
                                        sourceType = AnalyticsEventSourceType.ROOM,
                                        sourceId = getRoomPostData(it)?.getRoomId() ?: "",
                                        location = location
                                    )
                            }
                        },
                        onProductViewed = { product, location ->
                            roomChildPost?.let {
                                product.analytics()
                                    .markAsViewed(
                                        sourceType = AnalyticsEventSourceType.ROOM,
                                        sourceId = getRoomPostData(it)?.getRoomId() ?: "",
                                        location = location
                                    )
                            }
                        },
                        canManageProducts = isHost && isProductCatalogueEnabled,
                        taggedProducts = selectedProducts,
                        pinnedProductId = null,
                        isPostLive = true,
                        skipPartiallyExpanded = isHost,
                        isHost = isHost
                    )
                } else if (selectedProducts.isNotEmpty()) {
                    AmityProductTagListComponent(
                        productTags = selectedProducts,
                        renderMode = RenderModeEnum.VIDEO,
                        onDismiss = { showProductTagSheet = false },
                        onProductClick = { product -> selectedProduct = product }
                    )
                }
            }

            if (showAddProductBottomSheet) {
                AmityAddProductBottomSheet(
                    pageScope = getPageScope(),
                    onDismiss = {
                        showAddProductBottomSheet = false
                    },
                    onDone = {
                        showAddProductBottomSheet = false
                        val postId = roomChildPost?.getPostId() ?: ""
                        viewModel.addTaggedProducts(
                            postId = postId,
                            currentProducts = selectedProducts,
                            newProducts = it,
                        )
                    },
                    taggedProduct = selectedProducts.orEmpty(),
                    requestFocus = true
                )
            }

            selectedProduct?.let { product ->
                AmityProductWebViewBottomSheet(
                    product = product,
                    onDismiss = { selectedProduct = null }
                )
            }

            // Menu bottom sheet
            if (showMenuSheet) {
                VideoPlayerMenuBottomSheet(
                    post = if (isRecordedRoomPost) roomChildPost else videoPosts.getOrNull(pagerState.currentPage),
                    onDismiss = { showMenuSheet = false },
                    onViewOriginalPost = onViewOriginalPost,
                    onTagProductsClick = if (isRecordedRoomPost) {
                        {
                            showProductTagSheet = true
                        }
                    } else null,
                    tagProductsCount = if (isRecordedRoomPost) selectedProducts.size else 0,
                    context = context
                )
            }

            BackHandler {
                onDismiss()
            }
        }
    }
}

@Composable
fun VideoSeekBar(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
) {
    var duration by remember { mutableLongStateOf(0L) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var isDragging by remember { mutableStateOf(false) }

    // Update progress periodically
    LaunchedEffect(exoPlayer) {
        while (true) {
            if (!isDragging) {
                duration = exoPlayer.duration.takeIf { it > 0 } ?: 0L
                currentPosition = exoPlayer.currentPosition
            }
            delay(100)
        }
    }

    val progress = if (duration > 0) {
        (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Column(modifier = modifier) {
        // Duration display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatVideoDuration(currentPosition),
                color = Color.White,
                style = AmityTheme.typography.body
            )
            Text(
                text = formatVideoDuration(duration),
                color = Color.White,
                style = AmityTheme.typography.body
            )
        }

        // Seek bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                            exoPlayer.play()
                        },
                        onDrag = { change, _ ->
                            if (duration > 0) {
                                val seekRatio = (change.position.x / size.width).coerceIn(0f, 1f)
                                val seekPosition = (seekRatio * duration).toLong()
                                exoPlayer.seekTo(seekPosition)
                                currentPosition = seekPosition
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (duration > 0) {
                            val seekPosition = (offset.x / size.width * duration).toLong()
                            exoPlayer.seekTo(seekPosition)
                        }
                    }
                }
        ) {
            // Background track
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp)
                    )
            ) {
                // Progress track
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(4.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

            // Thumb indicator - centered on progress position
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceAtLeast(0.01f))
                    .align(Alignment.CenterStart)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(12.dp)
                        .background(Color.White, RoundedCornerShape(6.dp))
                )
            }
        }
    }
}

private fun formatVideoDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60)) % 24
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoPlayerMenuBottomSheet(
    post: AmityPost?,
    onDismiss: () -> Unit,
    onViewOriginalPost: (() -> Unit)? = null,
    onTagProductsClick: (() -> Unit)? = null,
    tagProductsCount: Int = 0,
    context: android.content.Context,
) {
    if (post == null) {
        onDismiss()
        return
    }

    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val postLink = com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController.getPostLink(post)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (onTagProductsClick != null) Color(0xFF191919) else AmityTheme.colors.background,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
        ) {
            // View original post option (for video feed)
            if (onViewOriginalPost != null) {
                com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_view_post,
                    text = "View original post",
                ) {
                    onDismiss()
                    onViewOriginalPost()
                }
            }

            // Tag products option (for recorded room posts)
            if (onTagProductsClick != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableWithoutRipple {
                            onDismiss()
                            onTagProductsClick()
                        }
                        .padding(horizontal = 4.dp, vertical = 16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.amity_ic_room_product_tags),
                        contentDescription = "open tagged products bottomsheet button",
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("product_tagging_button_element")
                    )
                    Text(
                        text = "Tag products",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFEBECEF)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = tagProductsCount.toString(),
                            style = AmityTheme.typography.body,
                            color = AmityTheme.colors.baseShade3
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_chevron_right),
                            contentDescription = null,
                            tint = AmityTheme.colors.baseShade3,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (onViewOriginalPost == null && onTagProductsClick == null) {
                if (postLink.isNotEmpty()) {
                    // Copy post link
                    com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem(
                        icon = R.drawable.amity_v4_link_icon,
                        text = "Copy post link",
                    ) {
                        onDismiss()
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(postLink))
                        com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar.publishSnackbarMessage(
                            "Link copied"
                        )
                    }

                    // Share to
                    com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem(
                        icon = R.drawable.amity_v4_share_icon,
                        text = "Share to",
                    ) {
                        onDismiss()
                        sharePost(context, postLink)
                    }
                }
            }
        }
    }
}

@UnstableApi
private fun getRecordedMediaSource(urls: List<String>): ConcatenatingMediaSource {
    val concatenatedSource = ConcatenatingMediaSource()
    urls.forEach { url ->
        val mediaItem = MediaItem.fromUri(url.toUri())
        val videoSource: MediaSource =
            HlsMediaSource.Factory(getAuthenticatedDataSource())
                .createMediaSource(mediaItem)
        concatenatedSource.addMediaSource(videoSource)
    }
    return concatenatedSource
}

@UnstableApi
private fun getAuthenticatedDataSource(): DefaultHttpDataSource.Factory {
    return DefaultHttpDataSource.Factory()
        .setAllowCrossProtocolRedirects(true)
        .let { factory ->
            AmityCoreClient.getAccessToken()
                ?.let { token -> mapOf("Authorization" to "Bearer $token") }
                ?.let(factory::setDefaultRequestProperties)
                ?: factory
        }
}