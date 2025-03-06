package com.amity.socialcloud.uikit.community.compose.community.setup.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.isUIKitInDarkTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityMediaImageSelectionSheet(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    onSelect: (AmityMediaImageSelectionType?) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onSelect(null) },
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            AmityBaseElement(
                pageScope = pageScope,
                elementId = "camera_button"
            ) {
                AmityBottomSheetActionItem(
                    modifier = modifier.testTag(getAccessibilityId()),
                    icon = {
                        Box(
                            modifier = modifier
                                .clip(CircleShape)
                                .background(
                                    color = if (isUIKitInDarkTheme()) {
                                        AmityTheme.colors.baseShade3
                                    } else {
                                        AmityTheme.colors.baseShade4
                                    },
                                )
                                .size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = getConfig().getIcon()),
                                contentDescription = null,
                                tint = AmityTheme.colors.base,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center),
                            )
                        }
                    },
                    text = getConfig().getText(),
                ) {
                    onSelect(AmityMediaImageSelectionType.CAMERA)
                }
            }

            AmityBaseElement(
                pageScope = pageScope,
                elementId = "image_button"
            ) {
                AmityBottomSheetActionItem(
                    modifier = modifier.testTag(getAccessibilityId()),
                    icon = {
                        Box(
                            modifier = modifier
                                .clip(CircleShape)
                                .background(
                                    color = if (isUIKitInDarkTheme()) {
                                        AmityTheme.colors.baseShade3
                                    } else {
                                        AmityTheme.colors.baseShade4
                                    },
                                )
                                .size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = getConfig().getIcon()),
                                contentDescription = null,
                                tint = AmityTheme.colors.base,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center),
                            )
                        }
                    },
                    text = getConfig().getText(),
                ) {
                    onSelect(AmityMediaImageSelectionType.IMAGE)
                }
            }

            Box(modifier = Modifier.height(16.dp))
        }
    }
}

enum class AmityMediaImageSelectionType(val key: String) {
    CAMERA("camera"),
    IMAGE("image");
}

@Preview(showBackground = true)
@Composable
fun AmityMediaImageSelectionSheetPreview() {
    AmityMediaImageSelectionSheet()
}