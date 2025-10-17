package com.amity.socialcloud.uikit.community.compose.post.composer.poll

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.uikit.common.ui.elements.AmityBasicTextField
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.model.ImagePollUiState
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState

@Composable
fun AmityPollTypeImageView(
) {
    var sampleItems by remember {
        mutableStateOf(
            listOf(
                ImagePollUiState(uploadState = AmityFileUploadState.UPLOADING),
                ImagePollUiState(uploadState = AmityFileUploadState.FAILED),
                ImagePollUiState()
            )
        )
    }
    var cardHeight by remember { mutableStateOf(0.dp) }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
    ) {
        itemsIndexed(sampleItems) { index, item ->
            AmityPollImageTypeItemView(
                uiState = item,
                placeHolderText = "Option ${index + 1}",
                onTextChange = { newValue ->
                    sampleItems = sampleItems.map {
                        if (it == item) it.copy(answer = newValue) else it
                    }
                },
                onSelectImageClick = {
                    // Handle image selection
                },
                onRemoveAnswerClick = {
                    sampleItems = sampleItems.filterNot { it == item }
                },
                maxChar = 80,
                onCardHeight = { height ->
                    cardHeight = height
                }
            )
        }

        if (sampleItems.size < 10) {
            item {
                AmityPollImageTypeAddItemView(
                    modifier = Modifier.height(cardHeight),
                    onAddClick = {
                        sampleItems = sampleItems + ImagePollUiState()
                    }
                )
            }
        }
    }
}

@Composable
fun AmityPollImageTypeItemView(
    uiState: ImagePollUiState,
    placeHolderText: String = "Option",
    onTextChange: (TextFieldValue) -> Unit = {},
    maxChar: Int = Int.MAX_VALUE,
    onSelectImageClick: () -> Unit = {},
    onRemoveAnswerClick: () -> Unit = {},
    onErrorUploadImageClick: () -> Unit = {},
    onAltTextClick: (altText: AltTextMedia) -> Unit = {},
    onCardHeight: (Dp) -> Unit = {},
    isShowDeleteIcon: Boolean = true,
) {
    val density = LocalDensity.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(uiState.imageUri.toString())
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    var isFocused by remember { mutableStateOf(false) }
    val isNotEmpty = uiState.answer.text.trim().isNotEmpty()

    Box(
        modifier = Modifier
            .background(Color.White)
            .onGloballyPositioned { coordinates ->
                onCardHeight(with(density) { coordinates.size.height.toDp() })
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            border = BorderStroke(1.dp, AmityTheme.colors.baseShade4)
        ) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .aspectRatio(5f / 4f)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickableWithoutRipple {
                            onSelectImageClick()
                        },
                    painter = if (uiState.imageUri != null) painter else painterResource(R.drawable.amity_v4_poll_image_placeholder),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

                when (uiState.uploadState) {
                    AmityFileUploadState.UPLOADING -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = {
                                    uiState.uploadProgress.toFloat() / 100f
                                },
                                color = AmityTheme.colors.primary,
                                trackColor = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(28.dp),
                                strokeCap = StrokeCap.Round,
                            )
                        }
                    }

                    AmityFileUploadState.FAILED -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickableWithoutRipple {
                                    onErrorUploadImageClick()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(R.drawable.amity_ic_snack_bar_warning),
                                contentDescription = "Upload Failed",
                                tint = Color.White
                            )
                        }
                    }

                    AmityFileUploadState.COMPLETE -> {
                        uiState.image?.let { amityImage ->
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                                    .height(24.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(size = 9999.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .clickableWithoutRipple {
                                        onAltTextClick(
                                            AltTextMedia.Image(amityImage)
                                        )
                                    },
                            ) {
                                Text(
                                    text = "ALT",
                                    style = AmityTheme.typography.captionBold.copy(
                                        color = Color.White,
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .wrapContentSize()
                                )
                                amityImage.getAltText()?.let { altText ->
                                    // If alt text is set, show the check icon
                                    if (altText.isNotEmpty()) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            painter = painterResource(id = R.drawable.amity_ic_alt_check),
                                            contentDescription = "Check Icon",
                                            tint = Color.White,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        // No overlay needed
                    }
                }
            }
            AmityBasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                    }
                    .height(40.dp)
                    .padding(horizontal = 12.dp)
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = if (uiState.answer.text.trim().length > 60) AmityTheme.colors.alert else AmityTheme.colors.baseShade4
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(8.dp)
                    ),
                value = uiState.answer,
                maxChar = maxChar,
                onValueChange = { newValue: TextFieldValue ->
                    onTextChange(newValue)
                },
                textStyle = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.colors.base
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                placeholder = {
                    Text(
                        text = placeHolderText,
                        style = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.baseShade2
                        )
                    )
                },
                //readOnly = isCreating,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                minHeight = 40.dp,
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (true) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .clickableWithoutRipple {
                        onRemoveAnswerClick()
                    },
                painter = painterResource(R.drawable.amity_close_circle_buttons),
                tint = Color.Unspecified,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun AmityPollImageTypeAddItemView(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickableWithoutRipple {
                onAddClick()
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, AmityTheme.colors.baseShade4)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.amity_ic_add),
                contentDescription = "",
                modifier = Modifier
                    .size(28.dp),
                tint = AmityTheme.colors.baseShade1
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Add Option",
                style = AmityTheme.typography.captionBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AmityPollTypeImageViewPreview() {
    AmityPollTypeImageView()
}