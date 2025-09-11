package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.asAmityImage
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityAltTextConfigComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    mode: AltTextConfigMode,
    result: (image: AmityImage) -> Unit,
    onDismiss: () -> Unit,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostComposerPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val altText = remember {
        mutableStateOf(
            if (mode is AltTextConfigMode.Edit) mode.altText else null
        )
    }
    val isInEditMode = mode is AltTextConfigMode.Edit
    val shouldAllowToSave by remember(altText) {
        derivedStateOf {
            (isInEditMode && altText.value != (mode as? AltTextConfigMode.Edit)?.altText)
                    || (!isInEditMode && altText.value?.trim()?.isNotEmpty() == true)
        }
    }

    // Get up-to-date alt text from cache if in edit mode
    LaunchedEffect(mode) {
        if (mode is AltTextConfigMode.Edit) {
            (mode.media as? AltTextMedia.Image)?.image?.getFileId()?.let { fileId ->
                AmityCoreClient.newFileRepository()
                    .getFile(fileId)
                    .doOnSuccess {
                        altText.value = it.asAmityImage()?.getAltText()
                    }
                    .subscribe()
            }

        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.navigationBars },
        modifier = modifier
            .padding(0.dp)
            .statusBarsPadding()
            .semantics {
                testTagsAsResourceId = true
            },
    ) {
        AmityBaseComponent(
            needScaffold = true,
            pageScope = pageScope,
            componentId = "alt_text_config_component",
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(AmityTheme.colors.background)
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier
                            .fillMaxWidth()
                            .background(AmityTheme.colors.background)
                            .padding(top = 8.dp, bottom = 12.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        AmityBaseElement(
                            pageScope = pageScope,
                            elementId = "close_button"
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(com.amity.socialcloud.uikit.common.R.drawable.amity_ic_close),
                                    contentDescription = "Close button",
                                    tint = AmityTheme.colors.base,
                                    modifier = modifier
                                        .clickableWithoutRipple {
                                            onDismiss()
                                        }
                                        .testTag(getAccessibilityId()),
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AmityBaseElement(
                                pageScope = pageScope,
                                elementId = if (isInEditMode) "edit_alt_text_title" else "add_alt_text_title"
                            ) {
                                Text(
                                    text = if (isInEditMode) context.getString(R.string.amity_image_edit_alt_text_title) else context.getString(R.string.amity_image_add_alt_text_title),
                                    style = AmityTheme.typography.titleBold.copy(color = AmityTheme.colors.base),
                                    modifier = modifier.testTag(getAccessibilityId()),
                                )
                            }
                            Text(
                                text = "${altText.value?.length ?: 0}/180",
                                style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade2)
                            )
                        }
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            elementId = if (isInEditMode) "edit_alt_text_button" else "add_alt_text_button"
                        ) {
                            Text(
                                text = if (isInEditMode) context.getString(R.string.amity_v4_dialog_save_button) else context.getString(R.string.amity_v4_dialog_done_button),
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    color = if (shouldAllowToSave) AmityTheme.colors.primary else AmityTheme.colors.primaryShade2
                                ),
                                modifier = modifier
                                    .clickableWithoutRipple(enabled = shouldAllowToSave) {
                                        (mode.media as? AltTextMedia.Image)?.image?.let { image ->
                                            viewModel.updateAltText(
                                                image.getFileId(),
                                                altText.value?.trim() ?: "",
                                                onSuccess = { refreshImage ->
                                                    result(refreshImage)
                                                    onDismiss()
                                                },
                                                onError = { exception ->
                                                    val message =
                                                        AmityError.from(exception).let { error ->
                                                            when (error) {
                                                                AmityError.LINK_NOT_ALLOWED -> {
                                                                    R.string.amity_image_alt_text_ban_url_error_message
                                                                }
                                                                AmityError.CONNECTION_ERROR -> {
                                                                    R.string.amity_no_internet_error_message
                                                                }
                                                                AmityError.BAN_WORD_FOUND -> {
                                                                    R.string.amity_image_alt_text_ban_word_error_message
                                                                }
                                                                else -> {
                                                                    if (isInEditMode)
                                                                        R.string.amity_image_edit_alt_text_generic_error_message
                                                                    else
                                                                        R.string.amity_image_add_alt_text_generic_error_message
                                                                }
                                                            }
                                                        }
                                                    AmityUIKitSnackbar.publishSnackbarErrorMessage(
                                                        message = context.getString(message),
                                                    )
                                                },
                                            )
                                        }
                                    }
                                    .testTag(getAccessibilityId()),
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(AmityTheme.colors.baseShade4)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp)
                                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                        ) {
                            val image = (mode.media as? AltTextMedia.Image)?.image
                            val imageUrl = image?.getUrl(AmityImage.Size.MEDIUM) ?: ""
                            AsyncImage(
                                model = ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .networkCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .build(),
                                contentDescription = image?.getAltText() ?: "Image",
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .fillMaxSize()
                                    .background(AmityTheme.colors.baseShade4)
                            )
                        }
                    }
                    if (isInEditMode) {
                        if (altText.value != null) {
                            AmityTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 500.dp)
                                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                                text = altText.value ?: "",
                                hint = context.getString(R.string.amity_image_alt_text_hint_message),
                                maxCharacters = 180,
                                maxLines = 30,
                                textStyle = AmityTheme.typography.body.copy(
                                    color = AmityTheme.colors.base,
                                    fontSize = 15.sp  // Match original post composer font size
                                ),
                                onValueChange = {
                                    altText.value = it
                                },
                            )
                        }
                    } else {
                        AmityTextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 500.dp)
                                .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                            text = altText.value ?: "",
                            hint = context.getString(R.string.amity_image_alt_text_hint_message),
                            maxCharacters = 180,
                            maxLines = 30,
                            textStyle = AmityTheme.typography.body.copy(
                                color = AmityTheme.colors.base,
                                fontSize = 15.sp  // Match original post composer font size
                            ),
                            onValueChange = {
                                altText.value = it
                            },
                        )
                    }
                }
            }
        }
    }
}

sealed class AltTextMedia {
    data class Image(val image: AmityImage) : AltTextMedia()
}

sealed class AltTextConfigMode(open val media: AltTextMedia) {
    data class Create(override val media: AltTextMedia) : AltTextConfigMode(media)
    data class Edit(val altText: String, override val media: AltTextMedia) :
        AltTextConfigMode(media)
}