package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.NoRippleInteractionSource
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityFeedFilterBottomSheet(
    modifier: Modifier = Modifier,
    feedFilters: List<String>,
    selectedFilterIndex: Int,
    onFilterSelected: (Int) -> Unit,
    onCloseSheet: () -> Unit,
) {
    var currentSelectedIndex by remember(selectedFilterIndex) { mutableIntStateOf(selectedFilterIndex) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onCloseSheet,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = modifier
            .semantics {
                testTagsAsResourceId = true
            },
    ) {
        Column(
            modifier = modifier
                .padding(bottom = 64.dp)
        ) {
            // Filter options
            AmityFeedFilterSelection(
                feedFilters = feedFilters,
                selectedFilterIndex = currentSelectedIndex,
                onFilterSelected = { index ->
                    currentSelectedIndex = index
                    onFilterSelected(index)
                    onCloseSheet()
                }
            )
        }
    }
}

@Composable
fun AmityFeedFilterSelection(
    modifier: Modifier = Modifier,
    feedFilters: List<String>,
    selectedFilterIndex: Int,
    onFilterSelected: (Int) -> Unit,
) {
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(modifier.selectableGroup()) {
        feedFilters.forEachIndexed { index, filterText ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .selectable(
                        selected = (index == selectedFilterIndex),
                        interactionSource = NoRippleInteractionSource(),
                        indication = null,
                        onClick = { onFilterSelected(index) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = filterText,
                    style = AmityTheme.typography.bodyBold,
                    modifier = Modifier,
                    color = AmityTheme.colors.base
                )

                RadioButton(
                    selected = (index == selectedFilterIndex),
                    onClick = null, // null recommended for accessibility with screen readers
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AmityTheme.colors.primary,
                        unselectedColor = AmityTheme.colors.baseShade3
                    )
                )
            }
        }
    }
}
