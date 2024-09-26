package com.amity.socialcloud.uikit.community.compose.socialhome.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityCommunityCategoryView(
    modifier: Modifier = Modifier,
    categories: List<AmityCommunityCategory>,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        for (index in categories.indices) {
            if (index >= 3) break
            AmityCommunityCategoryItemView(
                categoryName = categories[index].getName(),
            )
        }

        if (categories.size >= 4) {
            AmityCommunityCategoryOverflowCount(
                remainingCount = categories.size - 3
            )
        }
    }
}

@Composable
fun AmityCommunityCategoryItemView(
    modifier: Modifier = Modifier,
    categoryName: String,
) {
    Box(
        modifier = modifier
            .widthIn(max = 84.dp)
            .height(18.dp)
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = categoryName,
            style = AmityTheme.typography.caption.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun AmityCommunityCategoryOverflowCount(
    modifier: Modifier = Modifier,
    remainingCount: Int,
) {
    Box(
        modifier = modifier
            .widthIn(max = 84.dp)
            .height(18.dp)
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "+$remainingCount",
            style = AmityTheme.typography.caption.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            modifier = modifier.align(Alignment.Center),
        )
    }
}