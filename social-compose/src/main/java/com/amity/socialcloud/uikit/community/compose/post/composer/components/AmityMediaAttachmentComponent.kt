package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.isUIKitInDarkTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityMediaAttachmentViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentAllowedPickerType
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentPickerEvent
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityPostAttachmentButton

@Composable
fun AmityMediaAttachmentComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityMediaAttachmentViewModel,
    productTagCount: Int = 0,
    onProductTagClick: () -> Unit = {},
) {
    val allowedPickerType by viewModel.postAttachmentAllowedPickerType.collectAsState()
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "media_attachment"
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(56.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            when (allowedPickerType) {
                AmityPostAttachmentAllowedPickerType.All -> {
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImageOrVideoSelectionSheet)
                    }
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "image_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImagePicker)
                    }
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "video_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenVideoPicker)
                    }
                    /*
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "file_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenFilePicker)
                    }
                     */
                }

                is AmityPostAttachmentAllowedPickerType.Image -> {
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImageCamera)
                    }
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "image_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImagePicker)
                    }
                }

                is AmityPostAttachmentAllowedPickerType.Video -> {
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenVideoCamera)
                    }
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "video_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenVideoPicker)
                    }
                }

                is AmityPostAttachmentAllowedPickerType.File -> {
                    /*
                    AmityPostAttachmentButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "file_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenFilePicker)
                    }
                     */
                }
            }

            AmityProductTagAttachmentButton(
                count = productTagCount,
                onClick = onProductTagClick,
            )
        }
    }
}

@Composable
private fun AmityProductTagAttachmentButton(
    count: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clickableWithoutRipple { onClick() },
    ) {
        // Same circular ground as the camera/gallery buttons — the design gives all four
        // icons in the bar the identical treatment.
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    color = if (isUIKitInDarkTheme()) {
                        AmityTheme.colors.baseShade3
                    } else {
                        AmityTheme.colors.backgroundShade1
                    },
                )
                .size(32.dp),
        ) {
            Icon(
                painter = painterResource(id = CommonR.drawable.amity_ic_product_tag),
                contentDescription = null,
                tint = AmityTheme.colors.base,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
            )
        }
        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(22.dp)
                    .background(color = AmityTheme.colors.base, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = count.toString(),
                    style = AmityTheme.typography.captionBold.copy(color = AmityTheme.colors.background),
                )
            }
        }
    }
}