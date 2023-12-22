package com.amity.socialcloud.uikit.community.compose.story.create.elements

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryCameraHelper

@Composable
fun AmityStoryCameraPreviewElement(
    modifier: Modifier = Modifier,
) {
    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = {
                PreviewView(it).apply {
                    AmityStoryCameraHelper.init(this)
                }
            }
        )
    ) {
        onDispose {
            AmityStoryCameraHelper.stop()
        }
    }
}


@Preview
@Composable
fun AmityStoryCameraPreviewElementPreview() {
    AmityStoryCameraPreviewElement()
}