package com.amity.socialcloud.uikit.community.compose.story.draft

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryImageDisplayMode
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.story.create.elements.AmityStoryCameraRelatedButtonElement
import com.amity.socialcloud.uikit.community.compose.story.hyperlink.AmityStoryHyperlinkComponent
import com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements.AmityStoryHyperlinkView
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryVideoPlayer
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.community.compose.utils.asBoolean
import com.amity.socialcloud.uikit.community.compose.utils.asDrawableRes
import com.amity.socialcloud.uikit.community.compose.utils.getBackgroundColor
import com.amity.socialcloud.uikit.community.compose.utils.getValue
import com.amity.socialcloud.uikit.community.compose.utils.showToast
import com.amity.socialcloud.uikit.community.compose.utils.toComposeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityDraftStoryPage(
    modifier: Modifier = Modifier,
    targetId: String,
    targetType: AmityStory.TargetType,
    isImage: Boolean,
    fileUri: Uri,
    exoPlayer: ExoPlayer? = null,
    onCreateSuccess: () -> Unit = {},
    onCloseClicked: () -> Unit = {},
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

    val communityAvatarUrl by remember {
        derivedStateOf {
            when (storyTarget?.getTargetType()) {
                AmityStory.TargetType.COMMUNITY -> {
                    (storyTarget as? AmityStoryTarget.COMMUNITY)?.getCommunity()?.getAvatar()
                        ?.getUrl()
                }

                else -> ""
            }
        }
    }

    val hyperlinkUrlText by viewModel.hyperlinkUrl.collectAsState(initial = "")
    val hyperlinkCustomText by viewModel.hyperlinkText.collectAsState(initial = "")

    var isImageContentScaleFit by remember { mutableStateOf(true) }
    val openAlertDialog = remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val communityAvatarPainter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalContext.current)
            .data(data = communityAvatarUrl)
            .build()
    )

    var palette by remember { mutableStateOf<Palette?>(null) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(if (isImage) fileUri else "")
            .allowHardware(false)
            .build()
    )

    LaunchedEffect(painter) {
        val bitmap = painter.imageLoader.execute(painter.request).drawable?.toBitmap()

        if (bitmap != null) {
            launch(Dispatchers.Default) {
                palette = Palette.Builder(bitmap).generate()
            }
        }
    }

    if (openAlertDialog.value) {
        AmityAlertDialog(
            dialogTitle = "Discard this story?",
            dialogText = "The story will be permanently deleted. It cannot be undone.",
            confirmText = "Discard",
            dismissText = "Cancel",
            onConfirmation = { onCloseClicked() },
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
                    if (isImage) {
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
                                .background(Color.LightGray)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = if (isImageContentScaleFit) ContentScale.Fit else ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    } else {
                        AmityStoryVideoPlayer(
                            exoPlayer = exoPlayer,
                            isVisible = true,
                            modifier = modifier
                                .aspectRatio(9f / 16f)
                                .align(Alignment.TopCenter),
                        )

                        DisposableEffect(Unit) {
                            onDispose {
                                exoPlayer?.release()
                            }
                        }
                    }
                }
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "back_button"
                ) {
                    AmityStoryCameraRelatedButtonElement(
                        icon = getConfig().getValue("back_icon").asDrawableRes(),
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(closeBtn) {
                                top.linkTo(parent.top, 16.dp)
                                start.linkTo(parent.start, 16.dp)
                            },
                        onClick = {
                            openAlertDialog.value = true
                        }
                    )
                }

                if (isImage) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "aspect_ratio_button"
                    ) {
                        AmityStoryCameraRelatedButtonElement(
                            icon = getConfig().getValue("aspect_ratio_icon").asDrawableRes(),
                            modifier = Modifier
                                .size(32.dp)
                                .constrainAs(aspectRatioBtn) {
                                    top.linkTo(parent.top, 16.dp)
                                    end.linkTo(hyperlinkBtn.start, 8.dp)
                                },
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
                    AmityStoryCameraRelatedButtonElement(
                        icon = getConfig().getValue("hyperlink_button_icon").asDrawableRes(),
                        modifier = Modifier
                            .size(32.dp)
                            .constrainAs(hyperlinkBtn) {
                                top.linkTo(parent.top, 16.dp)
                                end.linkTo(parent.end, 16.dp)
                            },
                        background = getConfig().getBackgroundColor(),
                        onClick = {
                            if (hyperlinkUrlText.isEmpty()) {
                                showBottomSheet = true
                            } else {
                                context.showToast("Can't add more than one link to your story.")
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

            AmityBaseElement(
                pageScope = getPageScope(),
                elementId = "share_story_button"
            ) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.Black)
                        .padding(end = 16.dp, top = 16.dp)
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
                            .clickable {
                                if (isImage) {
                                    viewModel.createImageStory(
                                        targetId = targetId,
                                        targetType = targetType,
                                        fileUri = fileUri,
                                        imageDisplayMode =
                                        if (isImageContentScaleFit) AmityStoryImageDisplayMode.FIT
                                        else AmityStoryImageDisplayMode.FILL,
                                        onSuccess = {
                                            context.showToast("Successfully shared story")
                                        },
                                        onError = { message ->
                                            context.showToast(message)
                                        }
                                    )
                                    onCreateSuccess()
                                } else {
                                    viewModel.createVideoStory(
                                        targetId = targetId,
                                        targetType = targetType,
                                        fileUri = fileUri,
                                        onSuccess = {
                                            context.showToast("Successfully shared story")
                                        },
                                        onError = { message ->
                                            context.showToast(message)
                                        }
                                    )
                                    onCreateSuccess()
                                }
                            }
                    ) {
                        if (getConfig().getValue("hide_avatar").asBoolean()) {
                            Spacer(modifier = Modifier.width(8.dp))
                        } else {
                            Image(
                                painter = communityAvatarPainter,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
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
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    containerColor = Color.White
                ) {
                    AmityStoryHyperlinkComponent(
                        defaultUrlText = hyperlinkUrlText,
                        defaultCustomText = hyperlinkCustomText,
                        onClose = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    )
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
        isImage = true,
        fileUri = Uri.parse("")
    )
}