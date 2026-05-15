package com.amity.socialcloud.uikit.community.compose.post.composer.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityMediaCameraSelectionSheet(
    modifier: Modifier = Modifier,
    onSelect: (AmityPostMedia.Type?) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onSelect(null) },
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = modifier,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            AmityBottomSheetActionItem(
                icon = null,
                text = amitySocialString("amity_social_button_image"),
            ) {
                onSelect(AmityPostMedia.Type.IMAGE)
            }

            AmityBottomSheetActionItem(
                icon = null,
                text = amitySocialString("amity_social_button_post_composer_video_button"),
            ) {
                onSelect(AmityPostMedia.Type.VIDEO)
            }

            Box(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityMediaCameraSelectionSheetPreview() {
    AmityMediaCameraSelectionSheet()
}