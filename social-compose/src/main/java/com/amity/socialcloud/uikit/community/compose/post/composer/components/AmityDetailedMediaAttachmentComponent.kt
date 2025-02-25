package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityMediaAttachmentViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentAllowedPickerType
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentPickerEvent
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityPostAttachmentButton

@Composable
fun AmityDetailedMediaAttachmentComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityMediaAttachmentViewModel,
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
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImageOrVideoSelectionSheet)
                    }
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "image_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImagePicker)
                    }
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "video_button",
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

                AmityPostAttachmentAllowedPickerType.Image -> {
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImageCamera)
                    }
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "image_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenImagePicker)
                    }
                }

                AmityPostAttachmentAllowedPickerType.Video -> {
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "camera_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenVideoCamera)
                    }
                    AmityDetailedMediaAttachmentElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "video_button",
                    ) {
                        viewModel.setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.OpenVideoPicker)
                    }
                }

                AmityPostAttachmentAllowedPickerType.File -> {
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
        }
    }
}

@Composable
fun AmityDetailedMediaAttachmentElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    elementId: String,
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
                    onClick()
                }
        ) {
            AmityPostAttachmentButton(
                pageScope = pageScope,
                componentScope = componentScope,
                elementId = elementId,
                onClick = onClick,
            )
            Text(
                text = getConfig().getText(),
                style = AmityTheme.typography.body.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}