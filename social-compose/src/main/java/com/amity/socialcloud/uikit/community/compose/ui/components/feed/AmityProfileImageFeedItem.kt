package com.amity.socialcloud.uikit.community.compose.ui.components.feed

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.asAmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.image.rememberZoomState
import com.amity.socialcloud.uikit.common.ui.image.zoomable
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.RenderAltTextConfigSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityProfileImageFeedItem(
    modifier: Modifier = Modifier,
    data: AmityPost.Data.IMAGE?,
    postId: String? = null,
    parentPostId: String? = null,
    isPostCreator: Boolean = false,
    onPostClick: ((String) -> Unit)? = null,
    openDialog: ((AmityImage?, String?, onBottomSheetRequest: (() -> Unit)?) -> Unit)? = null,
) {
    val thumbnailUrl = remember(data) {
        data?.getImage()?.getUrl(AmityImage.Size.MEDIUM)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val imageMap = remember { mutableMapOf<String, AmityImage>() }
    data?.getImage()?.let { image ->
        if (imageMap[image.getFileId()] == null) {
            imageMap[image.getFileId()] = image
        }
    }

    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(thumbnailUrl)
            .crossfade(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = "Image Post",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clickableWithoutRipple {
                openDialog?.invoke(data?.getImage(), postId) {
                    showBottomSheet = true
                }
            }
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(AmityTheme.colors.baseShade4)
    )

    if (showBottomSheet && parentPostId != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
            ) {

                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_view_post,
                    text = "View post",
                    modifier = Modifier,
                    onClick = {
                        showBottomSheet = false
                        parentPostId.let { onPostClick?.invoke(it) }
                    }
                )

                if (isPostCreator) {
                    AmityBottomSheetActionItem(
                        icon = R.drawable.amity_ic_edit_profile,
                        text = context.getString(R.string.amity_image_edit_alt_text_title),
                        modifier = Modifier,
                    ) {
                        val image = data?.getImage()
                        image?.getFileId()?.let { fileId ->
                            imageMap[fileId]?.let {
                                viewModel.setAltTextMedia(AltTextMedia.Image(it))
                                viewModel.showAltTextConfigSheet()
                            }
                        }
                        showBottomSheet = false
                    }
                }

            }
        }
    }

    // Add the Alt Text Config Sheet
    RenderAltTextConfigSheet(
        forcedEditMode = true,
        onSuccess = { image ->
            imageMap[image.getFileId()] = image
            AmityUIKitSnackbar.publishSnackbarMessage(
                message = context.getString(R.string.amity_image_alt_text_updated_message),
            )
        },
        onDismiss = {
            // Do nothing extra when dismissed
        }
    )
}

@Composable
fun AmityProfileImageFeedItemPreviewDialog(
    modifier: Modifier = Modifier,
    data: AmityImage?,
    postId: String? = null,
    onPostClick: ((String) -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(data?.getUrl(AmityImage.Size.LARGE))
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    val painterState by painter.state.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Image(
                painter = painter,
                contentDescription = "Image Post",
                contentScale = ContentScale.Fit,
                modifier = modifier.fillMaxSize()
                    .zoomable(rememberZoomState()),
            )

            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color.Black.copy(alpha = 0.5f)),
            ) {
                val (closeBtn, menuBtn) = createRefs()

                AmityMenuButton(
                    icon = R.drawable.amity_ic_close2,
                    size = 32.dp,
                    iconPadding = 8.dp,
                    modifier = Modifier
                        .zIndex(Float.MAX_VALUE).constrainAs(closeBtn) {
                            top.linkTo(parent.top, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        },
                    onClick = {
                        onDismiss()
                    }
                )

                AmityMenuButton(
                    icon = R.drawable.amity_ic_more_horiz,
                    size = 32.dp,
                    iconPadding = 2.dp,
                    modifier = Modifier.constrainAs(menuBtn) {
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                    onClick = {
                        if (postId != null && onPostClick != null) {
                            onPostClick(postId)
                        }
                    }
                )
            }

            if (painterState !is AsyncImagePainter.State.Success) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .aspectRatio(0.75f)
                        .background(AmityTheme.colors.baseShade4)
                        .align(Alignment.Center)
                )
            }
        }
        Box(
            Modifier
                .height(32.dp)
                .offset(y = (-32).dp)
                .fillMaxWidth()
                .background(Color.Black)
        )
    }
}