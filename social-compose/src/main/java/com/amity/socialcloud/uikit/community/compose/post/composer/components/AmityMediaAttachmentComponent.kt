package com.amity.socialcloud.uikit.community.compose.post.composer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityMediaAttachmentViewModel
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentAllowedPickerType
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostAttachmentPickerEvent
import com.amity.socialcloud.uikit.community.compose.post.composer.elements.AmityPostAttachmentButton

@Composable
fun AmityMediaAttachmentComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    viewModel: AmityMediaAttachmentViewModel,
) {
    val allowedPickerType by viewModel.postAttachmentAllowedPickerType.collectAsState()
    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "media_attachment"
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
        }
    }
}