package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import com.amity.socialcloud.uikit.common.utils.getActivity
import com.amity.socialcloud.uikit.community.compose.livestream.room.util.AmityRoomPipController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityAddProductBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductTaggingBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewPageActivity
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
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.uikit.community.compose.livestream.room.util.AmityPipSessionRegistry

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AmityVideoPlayerPage(
    modifier: Modifier = Modifier,
    childPosts: List<AmityPost> = emptyList(),
    selectedFileId: String,
    showMenuButton: Boolean = false,
    recordedUrls: List<String> = emptyList(),
    // When false the page renders full-screen without the Dialog wrapper, so a host
    // Activity can own it and enter Picture-in-Picture (recorded livestream). Video-post
    // usages keep the default Dialog and are excluded from PiP.
    asDialog: Boolean = true,
    isInPipMode: Boolean = false,
    // Identifies the floating window this page owns, so leaving a product page can restore it.
    // Supplied by the host Activity, which registers the same id when it enters PiP.
    pipOwnerPostId: String? = null,
    post: AmityPost? = null,
    onDismiss: () -> Unit,
    onPageChanged: (String) -> Unit = {},
    onProductsUpdated: ((List<AmityProduct>) -> Unit)? = null,
    onViewOriginalPost: (() -> Unit)? = null
) {
    val context = LocalContext.current
    // Present only when hosted by a PiP-capable Activity (asDialog == false).
    val pipController = remember(context) { context.getActivity() as? AmityRoomPipController }
    // A room whose recording is still processing arrives here with no playable url, so there
    // is nothing to float — PiP must stay off rather than open a window onto a dead player.
    val hasPlayableMedia = recordedUrls.any { it.isNotBlank() }
    LaunchedEffect(pipController, asDialog, hasPlayableMedia) {
        if (!asDialog) {
            pipController?.setPipAllowed(hasPlayableMedia)
        }
    }

    // Recorded playback is VOD, so pausing holds position and resuming continues from it —
    // no live-edge seek. Mute silences audio while video keeps playing.
    var isMuted by remember { mutableStateOf(false) }
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
            .setSeekBackIncrementMs(10_000)
            .setSeekForwardIncrementMs(10_000)
            .setPauseAtEndOfMediaItems(true)
            .build()
    }

    // As a Dialog this is an ordinary video post, hosted by a page that is not a PiP host — so
    // nothing else would stand a floating livestream down and the viewer would hear both. As the
    // recorded livestream host (asDialog = false) this page IS the session, and its Activity has
    // already reconciled against the registry, so it must not end its own window here.
    if (asDialog) {
        LaunchedEffect(Unit) {
            AmityPipSessionRegistry.endSession()
        }
    }

    val connection by viewModel
        .getNetworkConnectionStateFlow()
        .collectAsState(initial = NetworkConnectionEvent.Connected)

    // Recovering from a data stall, mirroring the live room player. A recording that loses its
    // connection stops on the last frame and ExoPlayer does not retry; in a floating window there
    // is no resume to hang a reload on either. Both the connection dropping and the player
    // erroring mark it, and the retry runs once the connection is back — the position is still
    // valid for a recording, so it resumes where it stopped rather than restarting.
    var needsStallRecovery by remember { mutableStateOf(false) }
    LaunchedEffect(connection) {
        if (connection == NetworkConnectionEvent.Disconnected) needsStallRecovery = true
    }
    LaunchedEffect(connection, needsStallRecovery) {
        if (connection == NetworkConnectionEvent.Disconnected) return@LaunchedEffect
        if (!needsStallRecovery) return@LaunchedEffect
        needsStallRecovery = false
        if (exoPlayer.playbackState == Player.STATE_IDLE) {
            exoPlayer.prepare()
        }
    }

    var isAudioMuted by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var playerState by remember { mutableIntStateOf(ExoPlayer.STATE_IDLE) }
    // Media controls are revealed by tapping the video and auto-hide while playing.
    var showControls by remember { mutableStateOf(false) }
    // Bumped on every control interaction to restart the auto-hide countdown.
    var controlsInteraction by remember { mutableIntStateOf(0) }

    var verticalDragAmount by remember { mutableFloatStateOf(0f) }

    // Product tag state
    var showProductTagSheet by remember { mutableStateOf(false) }
    var showAddProductBottomSheet by remember { mutableStateOf(false) }
    var selectedProducts by remember { mutableStateOf<List<AmityProduct>>(emptyList()) }
    var selectedProduct by remember { mutableStateOf<AmityProduct?>(null) }
    /**
     * Opens a product and leaves the recording floating over it.
     *
     * When this page is Activity-hosted (the recorded livestream), the product opens as a page
     * of its own so the Activity backgrounds and the OS floats the recording in a real PiP
     * window — the same treatment every other navigation gets. As a Dialog (regular video
     * posts) there is no PiP to preserve, so the modal sheet below still serves.
     */
    val globalBehavior = remember { AmitySocialBehaviorHelper.globalBehavior }

    /**
     * Handles a product tap.
     *
     * As a Dialog (regular video posts) this is an in-place sheet — not a livestream, so the
     * livestream override does not apply and there is no playback to float.
     *
     * Activity-hosted (the recorded livestream) the integrator's override gets first refusal.
     * When it claims the tap it navigates somewhere of its own and this page never learns
     * where, so PiP is requested right here, in the same call stack: startActivity is
     * asynchronous, so the claim returning leaves us still resumed — the only state the OS
     * accepts the request from. Waiting for onPause is too late. Unclaimed, the built-in
     * product page opens and floats the recording the same way.
     */
    val openProduct: (AmityProduct) -> Unit = { product ->
        if (asDialog) {
            selectedProduct = product
        } else {
            val claimed = globalBehavior.onLivestreamProductTagClick(
                AmityGlobalBehavior.Context(
                    pageContext = context,
                    product = product,
                    communityId = (post?.getTarget() as? AmityPost.Target.COMMUNITY)
                        ?.getCommunityId(),
                    pipNavigator = { intent ->
                        pipController?.enterPipAndStart(intent) ?: context.startActivity(intent)
                    },
                )
            )
            if (!claimed) {
                val intent = AmityProductWebViewPageActivity.newIntent(
                    context = context,
                    product = product,
                    ownerPostId = pipOwnerPostId,
                )
                pipController?.enterPipAndStart(intent) ?: context.startActivity(intent)
            }
        }
    }
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

    // Reports the page the member is on as it changes, so the caller can return to it on dismiss.
    // Recorded room posts are a single fixed page with no per-page child post, so skip them.
    LaunchedEffect(pagerState.currentPage, isRecordedRoomPost) {
        if (!isRecordedRoomPost) {
            videoPosts.getOrNull(pagerState.currentPage)?.getPostId()?.let(onPageChanged)
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

    // Auto-hide the controls after 1s of no interaction, but only while playing —
    // when paused the controls (and play button) stay visible. Each control
    // interaction bumps controlsInteraction, which restarts this countdown.
    LaunchedEffect(showControls, isPlaying, controlsInteraction) {
        if (showControls && isPlaying) {
            delay(1_000)
            showControls = false
        }
    }

    // The floating window's controls are OS-drawn, so taps come back through the Activity
    // rather than as clicks here. Hand it closures that drive this player, and report state
    // so the icons stay truthful.
    DisposableEffect(pipController, exoPlayer, asDialog) {
        if (!asDialog) {
            pipController?.setPipControlHandlers(
                onTogglePlay = {
                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                },
                onSkipBack = { exoPlayer.seekBack() },
                onSkipForward = { exoPlayer.seekForward() },
            )
        }
        onDispose { pipController?.setPipControlHandlers(null, null, null) }
    }

    // This host only ever plays recorded content, so the skips are always offered.
    LaunchedEffect(pipController, asDialog, isPlaying) {
        if (!asDialog) {
            pipController?.setPipPlaybackState(isPlaying = isPlaying, canSeek = true)
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

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                // A dropped connection lands here and leaves the player idle; ExoPlayer never
                // retries on its own, and a floating window never resumes, so nothing else would
                // recover it. Flag it for the retry below.
                needsStallRecovery = true
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

    OptionalDialog(asDialog = asDialog) {
        AmityBaseComponent(
            componentId = "video_player_page",
            needScaffold = true,
            // See AmityBasePage.showSnackbar — the floating window shows video only.
            showSnackbar = !isInPipMode,
            // The video is full-bleed and the overlays inset themselves, so the scaffold must
            // not consume the system-bar insets — otherwise the close/menu buttons, the mini
            // player and the product page all draw under the status bar.
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(amityMediaSurface)
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
                                showControls = !showControls
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


                // Dim scrim behind the controls for readability. Not drawn while floating:
                // the PiP window shows video only.
                if (showControls && !isInPipMode) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(amityColorBlack.copy(alpha = 0.4f))
                    )
                }

                // Center transport controls: rewind 10s, play/pause, forward 10s.
                // Not gated on playback state, so seeking (which briefly buffers)
                // doesn't make the controls flicker away and back.
                if (showControls && !isInPipMode) {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.amity_ic_exo_rew_10),
                            contentDescription = "Rewind 10 seconds",
                            modifier = Modifier
                                .size(40.dp)
                                .clickableWithoutRipple {
                                    exoPlayer.seekBack()
                                    controlsInteraction++
                                }
                        )

                        Box(
                            modifier = Modifier.size(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Show a spinner while (re)buffering — e.g. the brief buffer
                            // after a skip — instead of the play/pause glyph. The box keeps
                            // a fixed size so the skip buttons don't shift.
                            if (playerState == ExoPlayer.STATE_BUFFERING) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(28.dp),
                                    color = amityColorWhite,
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Image(
                                    painter = painterResource(
                                        if (isPlaying) CommonComposeR.drawable.amity_ic_pause
                                        else CommonComposeR.drawable.amity_ic_play_v4
                                    ),
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    modifier = Modifier.fillMaxSize().clickableWithoutRipple {
                                        if (isPlaying) exoPlayer.pause() else exoPlayer.play()
                                        controlsInteraction++
                                    },
                                )
                            }
                        }

                        Image(
                            painter = painterResource(R.drawable.amity_ic_exo_ffwd_10),
                            contentDescription = "Forward 10 seconds",
                            modifier = Modifier
                                .size(40.dp)
                                .clickableWithoutRipple {
                                    exoPlayer.seekForward()
                                    controlsInteraction++
                                }
                        )
                    }
                }

                // Toolbar (top) — shown together with the media controls.
                if (showControls && !isInPipMode) ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .zIndex(Float.MAX_VALUE)
                ) {
                    val (closeBtn, muteBtn, menuBtn, pageCounter) = createRefs()

                    // Which frame of how many. Same style and placement as the image previewer's, so
                    // the two viewers state position identically. A recorded room is one continuous
                    // video, so there is no position to state.
                    if (!isRecordedRoomPost && videoPosts.size > 1) {
                        Text(
                            text = "${pagerState.currentPage + 1} / ${videoPosts.size}",
                            style = AmityTheme.typography.titleLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = amityColorWhite,
                            ),
                            modifier = Modifier
                                .constrainAs(pageCounter) {
                                    top.linkTo(closeBtn.top)
                                    bottom.linkTo(closeBtn.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .semantics {
                                    contentDescription =
                                        "Video ${pagerState.currentPage + 1} of ${videoPosts.size}"
                                },
                        )
                    }

                    // Close button
                    AmityMenuButton(
                        size = 32.dp,
                        iconPadding = 8.dp,
                        modifier = Modifier.constrainAs(closeBtn) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                        background = if (isRecordedRoomPost) Color.Transparent
                        else amityColorBlack.copy(alpha = 0.5f)
                    ) {
                        onDismiss()
                    }

                    // Mute/Unmute button
                    Image(
                        painter = painterResource(
                            id = if (isAudioMuted) R.drawable.amity_ic_audio_mute_filled
                            else R.drawable.amity_ic_audio_unmute_filled
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
                                controlsInteraction++
                            },
                        colorFilter = ColorFilter.tint(amityColorWhite)
                    )

                    // Menu button (3 dots)
                    if (showMenuButton && (isProductCatalogueEnabled || isHost)) {
                        AmityMenuButton(
                            icon = if (isRecordedRoomPost) CommonR.drawable.amity_ic_more_vertical else CommonR.drawable.amity_ic_more_horiz,
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

                // Bottom section: Product tag + SeekBar — shown with the media controls.
                if (showControls && !isInPipMode) Column(
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
                            // Close the list first, or it stays stacked over the product page.
                            showProductTagSheet = false
                            openProduct(product)
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
                        onProductClick = { product ->
                            showProductTagSheet = false
                            openProduct(product)
                        },
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

            // Only reachable as a Dialog (regular video posts). The Activity-hosted recorded
            // livestream opens the product as its own page instead — see openProduct.
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

/**
 * Wraps [content] in a full-screen [Dialog] when [asDialog] is true (the default, used for
 * video posts), or renders it directly when false so a host Activity owns the window and can
 * enter Picture-in-Picture (recorded livestream).
 */
@Composable
private fun OptionalDialog(asDialog: Boolean, content: @Composable () -> Unit) {
    if (asDialog) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            content()
        }
    } else {
        content()
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
                color = amityColorWhite,
                style = AmityTheme.typography.body
            )
            Text(
                text = formatVideoDuration(duration),
                color = amityColorWhite,
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
                        color = amityColorWhite.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp)
                    )
            ) {
                // Progress track
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(4.dp)
                        .background(
                            color = amityColorWhite,
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
                        .background(amityColorWhite, RoundedCornerShape(6.dp))
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
                    icon = CommonR.drawable.amity_ic_view_post,
                    text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_view_post"),
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
                        painter = painterResource(CommonR.drawable.amity_ic_room_product_tags),
                        contentDescription = "open tagged products bottomsheet button",
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("product_tagging_button_element")
                    )
                    Text(
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_tag_products"),
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
                            painter = painterResource(id = CommonComposeR.drawable.amity_ic_chevron_right),
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
                        icon = CommonR.drawable.amity_v4_link_icon,
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_label_copy_post_link"),
                    ) {
                        onDismiss()
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(postLink))
                        com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar.publishSnackbarMessage(
                            DefaultAmitySocialStringProvider.getInstance().getString("amity_social_toast_snackbar_link_copied")
                        )
                    }

                    // Share to
                    com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem(
                        icon = CommonR.drawable.amity_v4_share_icon,
                        text = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_button_share_to"),
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