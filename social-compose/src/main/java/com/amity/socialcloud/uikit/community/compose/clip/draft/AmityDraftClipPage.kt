package com.amity.socialcloud.uikit.community.compose.clip.draft

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asDrawableRes
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getValue
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityDraftClipPage(
    targetId: String,
    targetType: AmityPostTargetType,
    clipUrl: Uri,
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    val behavior = remember {
        AmitySocialBehaviorHelper.clipDraftPageBehavior
    }

    val postComposerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // If post was created successfully, close this page too
        if (result.resultCode == Activity.RESULT_OK) {
            context.closePageWithResult(Activity.RESULT_OK) // Pass success result back to CreateClipPage
        }
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityDraftClipViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    if (targetType == AmityPostTargetType.COMMUNITY) {
        viewModel.getCommunity(targetId)
    }

    val community by viewModel.community.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uploadClip(uri = clipUrl)
    }

    val clipUploadState by viewModel.clipUploadState.collectAsState()

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var isMute by remember { mutableStateOf(false) }
    var isVideoScaleFit by remember { mutableStateOf(false) }

    LaunchedEffect(clipUrl, isMute, isVideoScaleFit, clipUploadState) {
        if (clipUrl != Uri.EMPTY) {

            exoPlayer.apply {
                addMediaItem(MediaItem.fromUri(clipUrl))
                prepare()
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                playWhenReady = clipUploadState is AmityDraftClipViewModel.ClipUploadState.Success
                volume = if (isMute) 0f else 1f
            }
        }
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    if (openAlertDialog) {
        AmityAlertDialog(
            dialogTitle = "Discard this clip?",
            dialogText = "The clip will be permanently discarded. It cannot be undone.",
            confirmText = "Discard",
            dismissText = "Keep editing",
            onConfirmation = { context.closePage() },
            onDismissRequest = { openAlertDialog = false },
            confirmTextColor = AmityTheme.colors.alert,
            dismissTextColor = AmityTheme.colors.highlight,
        )
    }

    DisposableEffectWithLifeCycle(
        onStop = {
            // Stop the player when the page is stopped
            if (exoPlayer.isPlaying) {
                exoPlayer.stop()
            }
        },
    )

    BackHandler {
        openAlertDialog = true
    }

    AmityBasePage(pageId = "draft_clip_page") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Top bar and exo player view
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Content Box with video view
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .clip(RoundedCornerShape(12.dp))
                        .align(Alignment.TopCenter)
                ) {
                    // Video player without blur
                    AmityStoryVideoPlayer(
                        exoPlayer = exoPlayer,
                        isVisible = true,
                        modifier = Modifier
                            .aspectRatio(9f / 16f)
                            .align(Alignment.TopCenter),
                        scaleMode = if (isVideoScaleFit) AspectRatioFrameLayout.RESIZE_MODE_FIT else AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    )

                    // Overlay Box that appears only during uploading state
                    if (clipUploadState is AmityDraftClipViewModel.ClipUploadState.Uploading) {
                        Box(
                            modifier = Modifier
                                .aspectRatio(9f / 16f)
                                .align(Alignment.TopCenter)
                                .background(Color.White.copy(alpha = 0.3f)) // Semi-transparent white overlay
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(40.dp),
                                color = Color.White,
                                strokeWidth = 2.dp,
                                trackColor = Color.White.copy(0.2f),
                                strokeCap = StrokeCap.Round,
                            )
                        }
                    }

                    DisposableEffect(Unit) {
                        onDispose {
                            exoPlayer.release()
                        }
                    }
                }

                // Close button - positioned at top left
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "back_button"
                ) {
                    AmityMenuButton(
                        icon = getConfig().getIcon(),
                        size = 20.dp,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp)
                            .size(32.dp)
                            .align(Alignment.TopStart),
                        onClick = {
                            openAlertDialog = true
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "mute_button"
                    ) {
                        AmityMenuButton(
                            icon = getConfig().getValue(if (isMute) "mute_button_icon" else "unmute_button_icon")
                                .asDrawableRes(),
                            size = 20.dp,
                            modifier = Modifier
                                .size(32.dp),
                            background = getConfig().getBackgroundColor(),
                            onClick = {
                                isMute = !isMute
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "aspect_ratio_button"
                    ) {
                        AmityMenuButton(
                            icon = getConfig().getIcon(),
                            size = 16.dp,
                            modifier = Modifier
                                .size(32.dp),
                            background = getConfig().getBackgroundColor(),
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                isVideoScaleFit = !isVideoScaleFit
                            }
                        )
                    }

                }

            }

            // Bottom bar with Next button
            if (clipUploadState is AmityDraftClipViewModel.ClipUploadState.Success) {
                val clip = (clipUploadState as AmityDraftClipViewModel.ClipUploadState.Success).clip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Black)
                        .padding(horizontal = 16.dp)
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "next_button"
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .height(40.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                                .padding(start = 16.dp, end = 8.dp)
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    behavior.goToPostComposerPage(
                                        context = context,
                                        clip = clip,
                                        uri = clipUrl,
                                        launcher = postComposerLauncher,
                                        community = community,
                                        targetId = targetId,
                                        targetType = targetType,
                                        isMute = isMute,
                                        aspectRatio = if (isVideoScaleFit) AmityClip.DisplayMode.FIT else AmityClip.DisplayMode.FILL
                                    )
                                }
                        ) {
                            Text(
                                text = "Next",
                                style = AmityTheme.typography.bodyBold
                            )

                            Icon(
                                painter = painterResource(
                                    id = getConfig().getIcon()
                                ),
                                contentDescription = null,
                                tint = AmityTheme.colors.base,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            if (clipUploadState is AmityDraftClipViewModel.ClipUploadState.Error) {
                AmityAlertDialog(
                    dialogTitle = "Failed to upload",
                    dialogText = "Please check your connection or choose a different video to upload",
                    dismissText = "OK",
                    onDismissRequest = { context.closePage() }
                )
            }

        }
    }
}

@Preview
@Composable
fun AmityDraftClipPagePreview() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(40.dp),
        color = Color.White,
        strokeWidth = 2.dp,
        trackColor = Color.White.copy(0.2f),
        strokeCap = StrokeCap.Round,
    )
}