package com.amity.socialcloud.uikit.community.compose.community.category.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.category.AmityCommunityAddCategoryPageViewModel


@Composable
fun AmityCommunityCategoryItem(
    modifier: Modifier = Modifier,
    viewModel: AmityCommunityAddCategoryPageViewModel,
    category: AmityCommunityCategory,
    isEnabled: Boolean = true,
    onSelect: (AmityCommunityCategory) -> Unit,
    onRemove: (AmityCommunityCategory) -> Unit,
) {
    val selectedItemStates by viewModel.selectedItemStates.collectAsState()
    val isSelected by remember(category.getCategoryId()) {
        derivedStateOf {
            selectedItemStates.find {
                it.categoryId == category.getCategoryId()
            }?.isSelected ?: false
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickableWithoutRipple {
                if (!isEnabled && !isSelected) return@clickableWithoutRipple

                if (isSelected) {
                    onRemove(category)
                } else {
                    onSelect(category)
                }

                viewModel.updateSelectedItemState(
                    categoryId = category.getCategoryId(),
                    isSelected = !isSelected,
                )
            }
    ) {
        AmityAvatarView(
            image = category.getAvatar(),
            iconPadding = 8.dp,
            placeholder = R.drawable.amity_ic_default_category_avatar,
            modifier = modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier.width(12.dp))
        Text(
            text = category.getName(),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            modifier = modifier.weight(1f)
        )
        Spacer(modifier.width(8.dp))
        Image(
            painter = painterResource(
                if (isSelected) R.drawable.amity_ic_category_selected
                else R.drawable.amity_ic_category_not_selected
            ),
            contentDescription = "Selection",
            modifier = modifier.size(24.dp)
        )
    }
}