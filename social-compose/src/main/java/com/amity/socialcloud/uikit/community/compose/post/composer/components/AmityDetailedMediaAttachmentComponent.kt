package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.isUIKitInDarkTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityMediaAttachmentViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentAllowedPickerType
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentPickerEvent
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityPostAttachmentButton
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialConfigString
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@Composable
fun AmityDetailedMediaAttachmentComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityMediaAttachmentViewModel,
    productTagCount: Int = 0,
    onProductTagClick: () -> Unit = {},
) {
    val allowedPickerType by viewModel.postAttachmentAllowedPickerType.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "detailed_media_attachment"
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            when (allowedPickerType) {
                AmityPostAttachmentAllowedPickerType.All -> {
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImageOrVideoSelectionSheet)
                    }
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "image_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImagePicker)
                    }
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "video_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenVideoPicker)
                    }
                    /*
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "file_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenFilePicker)
                    }
                     */
                }

                is AmityPostAttachmentAllowedPickerType.Image -> {
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImageCamera)
                    }
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "image_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImagePicker)
                    }
                }

                is AmityPostAttachmentAllowedPickerType.Video -> {
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                        isEnabled = allowedPickerType.isEnabled
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenVideoCamera)
                    }
                    AmityDetailedMediaAttachmentElement(
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
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "file_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenFilePicker)
                    }
                     */
                }
            }

            // Only this row carries a trailing count and chevron; the media rows are icon + label.
            if (productTagCount > 0) {
                AmityTagProductsMenuRow(
                    count = productTagCount,
                    onClick = onProductTagClick,
                )
            }
        }
    }
}

@Composable
private fun AmityTagProductsMenuRow(
    modifier: Modifier = Modifier,
    count: Int,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            // 14 rather than the media rows' 12: this row is 60 high, they are 56.
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .clickableWithoutRipple { onClick() },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
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
            Text(
                text = amitySocialString("amity_social_button_tag_products"),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.base,
                ),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = count.toString(),
                style = AmityTheme.typography.bodyLegacy.copy(color = AmityTheme.colors.baseShade1),
            )
            Icon(
                painter = painterResource(id = CommonComposeR.drawable.amity_ic_chevron_right),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade1,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
fun AmityDetailedMediaAttachmentElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    elementId: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    AmityBaseElement(
        pageScope = pageScope,
        componentScope = componentScope,
        elementId = elementId,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clickableWithoutRipple {
                    if(isEnabled) onClick()
                }
        ) {
            AmityPostAttachmentButton(
                pageScope = pageScope,
                componentScope = componentScope,
                elementId = elementId,
                onClick = onClick,
                isEnabled = isEnabled
            )
            Text(
                text = amitySocialConfigString(
                    when (elementId) {
                        "camera_button" -> "amity_social_button_post_composer_camera_button"
                        "image_button" -> "amity_social_button_post_composer_image_button"
                        "video_button" -> "amity_social_button_post_composer_video_button"
                        "file_button" -> "amity_social_button_post_composer_file_button"
                        else -> elementId
                    }
                ),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if(isEnabled) AmityTheme.colors.base else AmityTheme.colors.baseShade3
                )
            )
        }
    }
}