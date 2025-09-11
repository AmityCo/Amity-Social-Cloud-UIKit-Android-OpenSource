package com.amity.socialcloud.uikit.community.compose.clip.view.element

import android.content.res.Resources
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.common.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.reaction.picker.AmityReactionPicker
import com.amity.socialcloud.uikit.common.reaction.picker.getReactionIndexByX
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityConstants.POST_REACTION
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageViewModel
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.calculateWeight
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import com.google.gson.JsonObject
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.math.roundToInt

@OptIn(UnstableApi::class)
@Composable
fun ClipItem(
    modifier: Modifier = Modifier,
    post: AmityPost,
    community: AmityCommunity? = null,
    behavior: AmityClipPageBehavior,
    isCurrentlyPlaying: Boolean,
    playerState: Int = ExoPlayer.STATE_IDLE,
    exoPlayer: ExoPlayer,
    pageIndex: Int = 0,
    viewModel: AmityClipFeedPageViewModel,
    type: AmityClipFeedPageType,
    reloadClick: () -> Unit = { }
) {
    val context = LocalContext.current

    val clipUrl by viewModel.clipUrl.collectAsState()
    val parentPost by viewModel.parentPost.collectAsState()
    val isCommunityModerator by viewModel.isCommunityModerator.collectAsState()
    var isSeekBarDragging by remember { mutableStateOf(false) }
    val sheetUIState by viewModel.sheetUIState.collectAsState()

    val myReactionState = remember {
        val firstReaction = post.getMyReactions().firstOrNull()
        mutableStateOf(firstReaction ?: "")
    }
    val myReaction by remember(post.getMyReactions()) { myReactionState }
    val reactingState = remember {
        mutableStateOf(Pair("",0))
    }
    val reacting by remember { reactingState }
    var localReactionCount by remember(post.getReactionCount()) {
        mutableIntStateOf(parentPost?.getReactionCount() ?: post.getReactionCount())
    }

    var isReacted = remember(myReaction, reacting) { myReaction.isNotEmpty() || reacting.first.isNotEmpty() }

    var reactionExpanded by remember { mutableStateOf(false) }

    var initialPointerId by remember { mutableStateOf<PointerId?>(null) }
    var initialDownPos by remember { mutableStateOf<Offset?>(null) }
    val density = LocalDensity.current
    val screenWidthPx = with(density) { Resources.getSystem().displayMetrics.widthPixels }
    var anchorInWindow by remember { mutableStateOf<Rect?>(null) }
    var popupWidthPx by remember { mutableStateOf(0) }
    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    var lastHapticIndex by remember { mutableStateOf<Int?>(null) }
    val haptics = LocalHapticFeedback.current
    val reactions = remember { AmitySocialReactions.getList() }
    val pickerPositionState = remember { mutableStateOf<Rect?>(null) }
    val pickerPosition by remember { pickerPositionState }

    LaunchedEffect(sheetUIState) {
        if (sheetUIState !is AmityClipModalSheetUIState.CloseSheet) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }

    val commentCount by remember(
        post.getCommentCount(),
        parentPost?.getCommentCount()
    ) {
        mutableIntStateOf(parentPost?.getCommentCount() ?: post.getCommentCount())
    }

    var isMuted by remember(Unit) {
        val clip = (parentPost?.getChildren()?.firstOrNull()?.getData() as? AmityPost.Data.CLIP?)
            ?: (post.getData() as? AmityPost.Data.CLIP?)
        mutableStateOf(clip?.getIsMuted() ?: false)
    }

    var isFilled by remember {
        mutableStateOf(false)
    }

    val isCommunityJoined = remember(viewModel, community) {
        community?.isJoined() == true
    }

    val isCommentCreateAllowed = remember(viewModel, community) {
        community?.getStorySettings()?.allowComment == true
    }

    // Only get clip URL when this item is currently playing
    LaunchedEffect(pageIndex, isCurrentlyPlaying) {
        if (isCurrentlyPlaying) {
            val currentClip = if (type !is AmityClipFeedPageType.GlobalFeed) {
                (post.getData() as? AmityPost.Data.CLIP)
            } else {
                (post.getChildren()
                    .firstOrNull()?.getData() as? AmityPost.Data.CLIP)
            }
            if (type !is AmityClipFeedPageType.GlobalFeed) {
                viewModel.getParentPost(post.getParentPostId() ?: "")
            }
            isFilled = currentClip?.getDisplayMode() == AmityClip.DisplayMode.FILL

            currentClip?.let {
                viewModel.getClipUrl(currentClip)
            }

            val currentVideo = (post.getData() as? AmityPost.Data.VIDEO)

            currentVideo?.let {
                viewModel.getVideoUrl(currentVideo)
            }

            viewModel.isCommunityModerator(post)
        }
    }

    // Only set media item when URL is available AND this is the current playing item
    LaunchedEffect(clipUrl, isCurrentlyPlaying) {
        if (clipUrl.isNotBlank() && isCurrentlyPlaying) {
            exoPlayer.clearMediaItems()
            exoPlayer.setMediaItem(MediaItem.fromUri(clipUrl))
            exoPlayer.prepare()
            if (isMuted) {
                exoPlayer.volume = 0f // Mute the player if isMuted is true
            } else {
                exoPlayer.volume = 1f // Unmute the player
            }
            exoPlayer.playWhenReady = true
        }
    }

    // Track lifecycle events to properly handle playback
    DisposableEffectWithLifeCycle(
        onResume = {
            if (isCurrentlyPlaying) {
                exoPlayer.play()
            }
        },
        onPause = {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
            }
        },
        onStop = {
            exoPlayer.pause()
        },
        onDestroy = {
            exoPlayer.stop()
            exoPlayer.release()
        }
    )

    var firstTextWidth by remember { mutableIntStateOf(0) }
    var secondTextWidth by remember { mutableIntStateOf(0) }

    // Sample texts
    var firstText = ""

    val mentionGetter =
        AmityMentionMetadataGetter(parentPost?.getMetadata() ?: post.getMetadata() ?: JsonObject())
    val postDescription = (parentPost?.getData() as? AmityPost.Data.TEXT)?.getText()
        ?: (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Video Player component
        Box(modifier = Modifier.fillMaxSize()) {
            if (isCurrentlyPlaying && (playerState == ExoPlayer.STATE_READY)) {
                AmityStoryVideoPlayer(
                    exoPlayer = exoPlayer,
                    modifier = Modifier.fillMaxSize(),
                    isVisible = true, // Always show player when current
                    scaleMode = if (isFilled) AspectRatioFrameLayout.RESIZE_MODE_ZOOM else AspectRatioFrameLayout.RESIZE_MODE_FIT
                )
            } else if (exoPlayer.playerError != null) {
                AmityClipLoadStateError(
                    onReloadClick = {
                        reloadClick()
                    }
                )
            }
        }

        // Top Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(102.dp) // adjust as needed
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )

        // Bottom Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(168.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                    )
                )
        )

        // Interaction UI
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            if (!isSeekBarDragging) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // User Profile and clip details (bottom left)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 16.dp)
                            .padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AmityUserAvatarView(
                                user = parentPost?.getCreator() ?: post.getCreator(),
                                modifier = modifier.clickableWithoutRipple {
                                    post.getCreator()?.let {
                                        behavior.goToUserProfilePage(
                                            context = context,
                                            userId = it.getUserId(),
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                // Creator name and post time
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    firstText = post.getCreator()?.getDisplayName()?.trim() ?: ""
                                    Text(
                                        text = firstText,
                                        style = AmityTheme.typography.bodyBold,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        onTextLayout = {
                                            firstTextWidth = it.size.width
                                        },
                                        modifier = modifier
                                            .weight(
                                                calculateWeight(firstTextWidth, secondTextWidth),
                                                fill = false
                                            )
                                            .clickableWithoutRipple {
                                                post.getCreator()?.let {
                                                    behavior.goToUserProfilePage(
                                                        context = context,
                                                        userId = it.getUserId(),
                                                    )
                                                }
                                            }
                                    )

                                    if (post.getCreator()?.isBrand() == true) {
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Image(
                                            painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(18.dp)
                                        )
                                    }

                                    Text(
                                        text = " • ",
                                        style = AmityTheme.typography.captionLegacy.copy(
                                            fontWeight = FontWeight.Normal,
                                            color = Color.White
                                        )
                                    )

                                    Text(
                                        text = (parentPost?.getCreatedAt()?.readableSocialTimeDiff()
                                            ?: post.getCreatedAt()?.readableSocialTimeDiff() ?: "")
                                                + if (post.isEdited()) " (edited)" else "",
                                        style = AmityTheme.typography.caption,
                                        color = Color.White,
                                        modifier = Modifier,
                                    )
                                }

                                // Moderator badge if applicable
                                if (isCommunityModerator) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                                        modifier = modifier
                                            .background(
                                                color = AmityTheme.colors.primaryShade3,
                                                shape = RoundedCornerShape(size = 20.dp)
                                            )
                                            .padding(start = 4.dp, end = 6.dp)
                                            .height(18.dp),
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.amity_ic_moderator_social),
                                            contentDescription = null,
                                            tint = AmityTheme.colors.primary,
                                        )
                                        Text(
                                            text = "Moderator",
                                            style = AmityTheme.typography.captionSmall,
                                            color = AmityTheme.colors.primary,
                                        )
                                    }
                                }
                            }
                        }

                        if (postDescription.isNotBlank()) {
                            AmityClipExpandableText(
                                modifier = Modifier,
                                text = postDescription,
                                mentionGetter = mentionGetter,
                                mentionees = parentPost?.getMentionees() ?: post.getMentionees(),
                                onClick = {

                                },
                                onMentionedUserClick = {
                                    behavior.goToUserProfilePage(
                                        context = context,
                                        userId = it,
                                    )
                                },
                                seeMoreClick = {
                                    behavior.goToPostDetailPage(
                                        context = context,
                                        postId = post.getPostId(),
                                    )
                                },
                                previewLines = 3
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Interaction buttons (right side)
                    Column(
                        modifier = Modifier
                            .padding(bottom = 56.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Like button with reaction picker
                        Box(
                            modifier = Modifier
                                .onGloballyPositioned { coords ->
                                    anchorInWindow = coords.boundsInWindow()
                                }
                                .pointerInput(Unit) {
                                    awaitEachGesture {
                                        // Record the initial pointer ID for future drag tracking
                                        val down = awaitFirstDown()
                                        down.consume()
                                        initialPointerId = down.id

                                        // Check if it's a long press (to show reactions) or a tap (to like/unlike)
                                        var longPressDetected = false

                                        // Try to detect long press
                                        try {
                                            withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                                // Wait a bit to see if this is a long press
                                                awaitLongPressOrCancellation(down.id)?.let { longPress ->
                                                    // Long press detected, show reaction picker
                                                    longPressDetected = true
                                                    val anchor = anchorInWindow ?: return@let
                                                    initialDownPos = Offset(
                                                        anchor.left.toFloat() + longPress.position.x,
                                                        anchor.top.toFloat() + longPress.position.y
                                                    )

                                                    if (community != null && !isCommunityJoined) {
                                                        AmityUIKitSnackbar.publishSnackbarErrorMessage(message = "Join community to interact with this clip.")
                                                        return@let
                                                    }

                                                    reactionExpanded = true
                                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                                                    // Start drag tracking for the reaction selection
                                                    var lastIdx: Int? = null

                                                    drag(longPress.id) { change ->
                                                        change.consume()
                                                        if (!reactionExpanded) return@drag

                                                        // Convert position to be relative to the reaction picker popup
                                                        val pickerRect = pickerPosition
                                                        if (pickerRect != null) {
                                                            // Calculate position relative to the picker
                                                            val fromAnchorToEdgePx = screenWidthPx - anchor.left
                                                            val approxPopupWidthPx = ((40*reactions.size)+8).dp.roundToPx()
                                                            val relativeX = change.position.x + screenWidthPx - approxPopupWidthPx
                                                            val idx = getReactionIndexByX(relativeX, approxPopupWidthPx, reactions.size)
                                                            if (idx != highlightedIndex) {
                                                                highlightedIndex = idx
                                                                if (idx != null && lastHapticIndex != idx) {
                                                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                    lastHapticIndex = idx
                                                                }
                                                            }
                                                            lastIdx = idx
                                                        }
                                                    }

                                                    // On drag end
                                                    lastIdx?.let { chosen ->
                                                        val chosenReaction = reactions[chosen]
                                                        if (myReaction == chosenReaction.name) {
                                                            localReactionCount -= 1
                                                            myReactionState.value = ""
                                                            viewModel.changeReaction(
                                                                postId = parentPost?.getPostId() ?: post.getPostId(),
                                                                isReacted = false,
                                                                reactionName = chosenReaction.name
                                                            )
                                                        } else {
                                                            // If user already reacted with different reaction
                                                            if (myReaction.isNotEmpty() && myReaction != chosenReaction.name) {
                                                                // Switch reaction
                                                                val previousReaction = myReaction
                                                                myReactionState.value = chosenReaction.name
                                                                isReacted = true
                                                                viewModel.switchReaction(
                                                                    postId = parentPost?.getPostId() ?: post.getPostId(),
                                                                    reaction = chosenReaction.name,
                                                                    previousReaction = previousReaction
                                                                )
                                                            } else {
                                                                localReactionCount += 1
                                                                myReactionState.value = chosenReaction.name
                                                                isReacted = true
                                                                viewModel.changeReaction(
                                                                    postId = parentPost?.getPostId() ?: post.getPostId(),
                                                                    isReacted = true,
                                                                    reactionName = chosenReaction.name
                                                                )
                                                            }
                                                        }
                                                    }

                                                    // Reset states
                                                    highlightedIndex = null
                                                    lastHapticIndex = null
                                                    reactionExpanded = false
                                                    initialPointerId = null
                                                    initialDownPos = null
                                                }
                                            }
                                        } catch (e: TimeoutCancellationException) {
                                            // Timeout for long press detection exceeded
                                        }

                                        // If no long press was detected, handle as a normal tap
                                        if (!longPressDetected) {
                                            if (reacting.first.isEmpty()) {
                                                // This was a tap, handle the like/unlike action
                                                val previousReaction = myReaction
                                                myReactionState.value = if (myReaction.isEmpty()) {
                                                    POST_REACTION
                                                } else {
                                                    ""
                                                }
                                                if (myReactionState.value.isEmpty()) {
                                                    localReactionCount -= 1
                                                } else {
                                                    localReactionCount += 1
                                                }
                                                viewModel.changeReaction(
                                                    postId = post.getPostId(),
                                                    reactionName = if (myReactionState.value.isNotEmpty()) POST_REACTION else previousReaction,
                                                    isReacted = myReactionState.value.isNotEmpty()
                                                )
                                            }
                                        }
                                    }
                                }
                        ) {
                            // The actual button UI
                            InteractionButton(
                                icon = if (isReacted) {
                                    myReaction.ifEmpty { "like" }
                                        .let(AmitySocialReactions::toReaction)
                                        .icon
                                } else {
                                    R.drawable.amity_v4_clip_like
                                },
                                count = localReactionCount.readableNumber(),
                            )
                        }

                        // Comment button
                        InteractionButton(
                            icon = R.drawable.amity_v4_ic_clip_comment,
                            count = commentCount.readableNumber(),
                            onClick = {
                                viewModel.updateSheetUIState(
                                    AmityClipModalSheetUIState.OpenCommentTraySheet(
                                        postId = parentPost?.getPostId() ?: post.getPostId(),
                                        community = community,
                                        shouldAllowInteraction = if (community != null) isCommunityJoined else true,
                                        shouldAllowComment = if (community != null) isCommentCreateAllowed else true
                                    )
                                )
                            }
                        )

                        // Mute and Unmute button
                        InteractionButton(
                            icon = if (isMuted) R.drawable.amity_v4_clip_mute_state else R.drawable.amity_v4_clip_unmute_state,
                            count = null,
                            onClick = {
                                isMuted = !isMuted

                                if (isMuted) {
                                    exoPlayer.volume = 0f
                                } else {
                                    exoPlayer.volume = 1f
                                }
                            }
                        )

                        // option button
                        InteractionButton(
                            icon = R.drawable.amity_v4_ic_clip_option,
                            count = null,
                            onClick = {
                                viewModel.updateSheetUIState(
                                    AmityClipModalSheetUIState.OpenClipMenuSheet(
                                        post = parentPost ?: post,
                                    )
                                )
                            }
                        )
                    }
                }
            }

            // SeekBar at the bottom
            ClipSeekBar(
                exoPlayer = exoPlayer,
                onDragStateChanged = { isDragging ->
                    isSeekBarDragging = isDragging
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        }

    }

    // Add reaction picker popup
    if (reactionExpanded && anchorInWindow != null) {
        val anchor = anchorInWindow!!
        Popup(
            properties = PopupProperties(
                focusable = true,
                clippingEnabled = false
            ),
            popupPositionProvider = object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    // Save popup width for picker
                    popupWidthPx = popupContentSize.width
                    // Position the picker centered with 8dp gap above the button
                    val spacingPx = with(density) { 8.dp.roundToPx() }
                    val x = (anchor.center.x - popupContentSize.width / 2f).roundToInt()
                    val y = (anchor.top - popupContentSize.height - spacingPx).roundToInt()

                    return IntOffset(
                        x.coerceIn(0, windowSize.width - popupContentSize.width),
                        y.coerceIn(0, windowSize.height - popupContentSize.height)
                    )
                }
            },
            onDismissRequest = {
                reactionExpanded = false
                highlightedIndex = null
                lastHapticIndex = null
            }
        ) {
            AmityReactionPicker(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .onGloballyPositioned { coords ->
                        // Store the popup's position in window coordinates for accurate touch position calculations
                        pickerPositionState.value = coords.boundsInWindow()
                    },
                pageScope = null,
                componentScope = null,
                selectedReaction = myReaction,
                highlightedIndex = highlightedIndex,
                onAddReaction = { reaction ->
                    // If the user has already reacted with a different reaction, remove that reaction first
                    if (myReaction.isNotEmpty() && myReaction != reaction) {
                        // Switch reaction
                        val previousReaction = myReaction
                        myReactionState.value = reaction
                        isReacted = true
                        viewModel.switchReaction(
                            postId = parentPost?.getPostId() ?: post.getPostId(),
                            reaction = reaction,
                            previousReaction = previousReaction
                        )
                    } else {
                        // If the user is adding a reaction for the first time
                        localReactionCount += 1
                        myReactionState.value = reaction
                        isReacted = true
                        viewModel.changeReaction(
                            postId = parentPost?.getPostId() ?: post.getPostId(),
                            isReacted = true,
                            reactionName = reaction
                        )
                    }
                },
                onRemoveReaction = { reaction ->
                    localReactionCount -= 1
                    myReactionState.value = ""
                    isReacted = false
                    viewModel.changeReaction(
                        postId = parentPost?.getPostId() ?: post.getPostId(),
                        isReacted = false,
                        reactionName = reaction
                    )
                },
                onDismiss = {
                    reactionExpanded = false
                    initialPointerId = null
                    initialDownPos = null
                    highlightedIndex = null
                    lastHapticIndex = null
                },
            )
        }
    }
}

