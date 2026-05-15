package com.amity.socialcloud.uikit.community.compose.livestream.create.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialConfigString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityEditThumbnailSheet(
    modifier: Modifier = Modifier,
    onSelect: (Boolean?) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()

    AmityBaseComponent(componentId = "thumbnail_action") {
        ModalBottomSheet(
            onDismissRequest = { onSelect(null) },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            //windowInsets = WindowInsets(top = 54.dp), not compat with sdk v7
            modifier = modifier,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "change_thumbnail_button"
                ) {
                    AmityBottomSheetActionItem(
                        icon = getConfig().getIcon(),
                        text = amitySocialConfigString("amity_social_button_change_thumbnail"),
                    ) {
                        onSelect(true)
                    }
                }
                AmityBaseElement(
                    componentScope = getComponentScope(),
                    elementId = "delete_thumbnail_button"
                ) {
                    AmityBottomSheetActionItem(
                        icon = getConfig().getIcon(),
                        text = amitySocialConfigString("amity_social_button_delete_thumbnail"),
                        color = AmityTheme.colors.alert
                    ) {
                        onSelect(false)
                    }
                }
                Box(modifier = Modifier.height(32.dp))
            }
        }
    }
}