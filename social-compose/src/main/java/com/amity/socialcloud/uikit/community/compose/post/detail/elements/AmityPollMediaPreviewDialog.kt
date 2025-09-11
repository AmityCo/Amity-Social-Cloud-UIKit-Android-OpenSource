package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.image.rememberZoomState
import com.amity.socialcloud.uikit.common.ui.image.zoomable
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.RenderAltTextConfigSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityPollMediaPreviewDialog(
    image: AmityImage,
    isPostCreator: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var localImage by remember(image) { mutableStateOf(image) }

    val imageRequest = ImageRequest
        .Builder(LocalContext.current)
        .data(localImage.getUrl(AmityImage.Size.LARGE))
        .crossfade(true)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    var openMenu by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        AmityBaseComponent(
            componentId = "post_media_preview",
            needScaffold = true,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (imageRequest.error() == null) {
                    AsyncImage(
                        model = imageRequest,
                        contentDescription = "Image Post",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .zoomable(rememberZoomState()),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(480.dp)
                            .background(AmityTheme.colors.baseShade4),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.amity_v4_image_preview_close),
                        contentDescription = "Close",
                        modifier = Modifier
                            .statusBarsPadding()
                            .size(24.dp)
                            .clickableWithoutRipple { onDismiss() },
                    )

                    if (isPostCreator) {
                        Icon(
                            painter = painterResource(R.drawable.amity_ic_more_horiz),
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier
                                .statusBarsPadding()
                                .size(28.dp)
                                .clickableWithoutRipple {  openMenu = true },
                        )
                    }
                }

            }
            if (openMenu) {
                ModalBottomSheet(
                    onDismissRequest = {
                        openMenu = false
                    },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true,
                    ),
                    containerColor = AmityTheme.colors.background,
                    contentWindowInsets = { WindowInsets.waterfall },
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                    ) {
                        AmityBottomSheetActionItem(
                            icon = R.drawable.amity_ic_edit_profile,
                            text = "Edit alt text",
                            modifier = Modifier.testTag("bottom_sheet_edit_alt_text_button"),
                        ) {
                            viewModel.showAltTextConfigSheet()
                            viewModel.setAltTextMedia(AltTextMedia.Image(localImage))
                        }
                    }
                }
            }

            RenderAltTextConfigSheet(
                forcedEditMode = true,
                onSuccess = { image ->
                    localImage = image
                    AmityUIKitSnackbar.publishSnackbarMessage(
                        message = context.getString(R.string.amity_image_alt_text_updated_message),
                    )
                },
                onDismiss = {
                    openMenu = false
                })
        }
    }
}