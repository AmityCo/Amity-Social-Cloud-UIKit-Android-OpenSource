package com.amity.socialcloud.uikit.community.compose.story.draft

import android.app.Activity
import android.net.Uri
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryImageDisplayMode
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.asBoolean
import com.amity.socialcloud.uikit.common.utils.asDrawableRes
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getBackgroundColor
import com.amity.socialcloud.uikit.common.utils.getValue
import com.amity.socialcloud.uikit.common.utils.toComposeColor
import com.amity.socialcloud.uikit.community.compose.story.hyperlink.AmityStoryHyperlinkComponent
import com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements.AmityStoryHyperlinkView
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityDraftStoryPage(
    modifier: Modifier = Modifier,
    targetId: String,
    targetType: AmityStory.TargetType,
    mediaType: AmityStoryMediaType,
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityDraftStoryViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val storyTarget by remember(viewModel, targetId, targetType) {
        viewModel.getTarget(targetId, targetType)
    }.subscribeAsState(initial = null)

    val community by remember {
        derivedStateOf {
            when (storyTarget?.getTargetType()) {
                AmityStory.TargetType.COMMUNITY -> {
                    (storyTarget as? AmityStoryTarget.COMMUNITY)?.getCommunity()
                }

                else -> null
            }
        }
    }

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(mediaType) {
        if (mediaType is AmityStoryMediaType.Video && mediaType.uri != Uri.EMPTY) {
            exoPlayer.apply {
                addMediaItem(MediaItem.fromUri(mediaType.uri))
                prepare()
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                playWhenReady = true
            }
        }
    }


    var hyperlinkUrlText by remember { mutableStateOf("") }
    var hyperlinkCustomText by remember { mutableStateOf("") }

    var isImageContentScaleFit by remember { mutableStateOf(true) }
    val openAlertDialog = remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var palette by remember { mutableStateOf<Palette?>(null) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(if (mediaType is AmityStoryMediaType.Image) mediaType.uri else "")
            .allowHardware(false)
            .build()
    )
    val painterState by painter.state.collectAsState()

    LaunchedEffect(painter, painterState) {
        if (painterState is AsyncImagePainter.State.Success) {
            val bitmap = (painterState as AsyncImagePainter.State.Success).result.image.toBitmap()
            launch(Dispatchers.Default) {
                palette = Palette.Builder(bitmap).generate()
            }
        }
    }

    if (openAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = "Discard story?",
            dialogText = "The story will be permanently discarded. It cannot be undone.",
            confirmText = "Discard",
            dismissText = "Cancel",
            onConfirmation = { context.closePage() },
            onDismissRequest = { openAlertDialog.value = false }
        )
    }
    BackHandler {
        openAlertDialog.value = true
    }

    AmityBasePage(pageId = "create_story_page") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            ConstraintLayout(
                modifier = modifier
            ) {
                val (contentBox, closeBtn, aspectRatioBtn, hyperlinkBtn, hyperlinkView) = createRefs()

                Box(
                    modifier = modifier
                        .background(Color.Black)
                        .clip(RoundedCornerShape(12.dp))
                        .constrainAs(contentBox) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    if (mediaType is AmityStoryMediaType.Image) {
                        Box(
                            modifier = Modifier
                                .aspectRatio(9f / 16f)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            (palette?.mutedSwatch?.toComposeColor() ?: Color.Black),
                                            (palette?.darkMutedSwatch?.toComposeColor()
                                                ?: Color.Black),
                                        )
                                    )
                                )
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = if (isImageContentScaleFit) ContentScale.Fit else ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("image_view")
                            )
                        }

                    } else {
                        AmityStoryVideoPlayer(
                            exoPlayer = exoPlayer,
                            isVisible = true,
                            modifier = modifier
                                .aspectRatio(9f / 16f)
                                .align(Alignment.TopCenter)
                                .testTag("video_view"),
                        )

                        DisposableEffect(Unit) {
                            onDispose {
                                exoPlayer.release()
                            }
                        }
                    }
                }
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "back_button"
                ) {
                    AmityMenuButton(
                        icon = getConfig().getValue("back_icon").asDrawableRes(),
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(closeBtn) {
                                top.linkTo(parent.top, 16.dp)
                                start.linkTo(parent.start, 16.dp)
                            }
                            .testTag(getAccessibilityId()),
                        onClick = {
                            openAlertDialog.value = true
                        }
                    )
                }

                if (mediaType is AmityStoryMediaType.Image) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "aspect_ratio_button"
                    ) {
                        AmityMenuButton(
                            icon = getConfig().getValue("aspect_ratio_icon").asDrawableRes(),
                            modifier = Modifier
                                .size(32.dp)
                                .constrainAs(aspectRatioBtn) {
                                    top.linkTo(parent.top, 16.dp)
                                    end.linkTo(hyperlinkBtn.start, 8.dp)
                                }
                                .testTag(getAccessibilityId()),
                            background = getConfig().getBackgroundColor(),
                            onClick = {
                                isImageContentScaleFit = !isImageContentScaleFit
                            }
                        )
                    }
                }

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "story_hyperlink_button"
                ) {
                    AmityMenuButton(
                        icon = getConfig().getValue("hyperlink_button_icon").asDrawableRes(),
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(hyperlinkBtn) {
                                top.linkTo(parent.top, 16.dp)
                                end.linkTo(parent.end, 16.dp)
                            }
                            .testTag(getAccessibilityId()),
                        background = getConfig().getBackgroundColor(),
                        onClick = {
                            if (hyperlinkUrlText.isEmpty()) {
                                showBottomSheet = true
                            } else {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage("Can't add more than one link to your story.")
                            }
                        }
                    )
                }

                if (hyperlinkUrlText.isNotEmpty()) {
                    var displayText = hyperlinkCustomText.takeIf { it.isNotEmpty() }
                    if (displayText == null) {
                        val matcher = Patterns.DOMAIN_NAME.matcher(hyperlinkUrlText)
                        displayText = if (matcher.find()) {
                            matcher.group()
                        } else {
                            hyperlinkUrlText
                        }
                    }

                    AmityStoryHyperlinkView(
                        text = displayText ?: "",
                        modifier = Modifier
                            .constrainAs(hyperlinkView) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom, 32.dp)
                            },
                        onClick = {
                            showBottomSheet = true
                        }
                    )
                }
            }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Black)
                    .padding(end = 16.dp, top = 16.dp)
            ) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "share_story_button"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .clip(CircleShape)
                            .background(getConfig().getBackgroundColor())
                            .padding(start = 4.dp, end = 12.dp)
                            .align(Alignment.CenterEnd)
                            .testTag(getAccessibilityId())
                            .clickable {
                                when (mediaType) {
                                    is AmityStoryMediaType.Image -> {
                                        viewModel.createImageStory(
                                            targetId = targetId,
                                            targetType = targetType,
                                            fileUri = mediaType.uri,
                                            imageDisplayMode =
                                            if (isImageContentScaleFit) AmityStoryImageDisplayMode.FIT
                                            else AmityStoryImageDisplayMode.FILL,
                                            hyperlinkUrlText = hyperlinkUrlText,
                                            hyperlinkCustomText = hyperlinkCustomText,
                                            onSuccess = {
                                                AmityUIKitSnackbar.publishSnackbarMessage(
                                                    "Successfully shared story"
                                                )
                                            },
                                            onError = { message ->
                                                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                                    message
                                                )
                                            }
                                        )
                                        context.closePageWithResult(Activity.RESULT_OK)
                                    }

                                    is AmityStoryMediaType.Video -> {
                                        viewModel.createVideoStory(
                                            targetId = targetId,
                                            targetType = targetType,
                                            fileUri = mediaType.uri,
                                            hyperlinkUrlText = hyperlinkUrlText,
                                            hyperlinkCustomText = hyperlinkCustomText,
                                            onSuccess = {
                                                AmityUIKitSnackbar.publishSnackbarMessage(
                                                    "Successfully shared story"
                                                )
                                            },
                                            onError = { message ->
                                                AmityUIKitSnackbar.publishSnackbarMessage(
                                                    message
                                                )
                                            }
                                        )
                                        context.closePageWithResult(Activity.RESULT_OK)
                                    }
                                }
                            }
                    ) {
                        if (getConfig().getValue("hide_avatar").asBoolean()) {
                            Spacer(modifier = Modifier.width(8.dp))
                        } else {
                            AmityCommunityAvatarView(
                                community = community,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                                    .testTag(getAccessibilityId("image_view"))
                            )
                        }

                        Text(
                            text = "Share story",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Icon(
                            painter = painterResource(
                                id = getConfig().getValue("share_icon").asDrawableRes()
                            ),
                            contentDescription = null,
                            tint = AmityTheme.colors.base,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (showBottomSheet) {
                AmityComposeTheme {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState,
                        containerColor = AmityTheme.colors.background,
                        contentWindowInsets = { WindowInsets.waterfall },
                    ) {
                        AmityStoryHyperlinkComponent(
                            defaultUrlText = hyperlinkUrlText,
                            defaultCustomText = hyperlinkCustomText,
                            onClose = { urlText, customText ->
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }

                                hyperlinkUrlText = urlText
                                hyperlinkCustomText = customText
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AmityDraftStoryPagePreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityDraftStoryPage(
        targetId = "",
        targetType = AmityStory.TargetType.COMMUNITY,
        mediaType = AmityStoryMediaType.Image(Uri.EMPTY),
    )
}