package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageViewModel
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.elements.calculateWeight
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import com.google.gson.JsonObject

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

    LaunchedEffect(sheetUIState) {
        if (sheetUIState !is AmityClipModalSheetUIState.CloseSheet) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }

    var isReacted by remember(
        post.getUpdatedAt(),
        post.getMyReactions(),
        parentPost?.getUpdatedAt(),
        parentPost?.getMyReactions()
    ) {
        mutableStateOf(
            parentPost?.getMyReactions()?.isNotEmpty() ?: post.getMyReactions().isNotEmpty()
        )
    }
    var localReactionCount by remember(
        post.getUpdatedAt(),
        post.getReactionCount(),
        parentPost?.getUpdatedAt(),
        parentPost?.getMyReactions()
    ) {
        mutableIntStateOf(parentPost?.getReactionCount() ?: post.getReactionCount())
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

            viewModel.observeReactionChange()

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
                        // Like button
                        InteractionButton(
                            icon = if (isReacted) R.drawable.amity_v4_clip_liked_button else R.drawable.amity_v4_clip_like,
                            count = localReactionCount.readableNumber(),
                            onClick = {
                                if (community != null && !isCommunityJoined) {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(message = "Join community to interact with this clip.")
                                    return@InteractionButton
                                } else {
                                    isReacted = !isReacted
                                    if (isReacted) {
                                        localReactionCount += 1
                                    } else {
                                        localReactionCount -= 1
                                    }

                                    viewModel.changeReaction(
                                        postId = parentPost?.getPostId() ?: post.getPostId(),
                                        isReacted = isReacted
                                    )
                                }
                            }
                        )

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
                                        postId = parentPost?.getPostId() ?: post.getPostId(),
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
}