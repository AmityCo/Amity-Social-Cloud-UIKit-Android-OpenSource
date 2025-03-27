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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
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
    maxPreview: Int = 3,
) {
    val textWidths = remember { mutableStateListOf<Int>() }

    // Initialize the list to store text widths
    if (textWidths.size != maxPreview + 1) {
        textWidths.clear()
        repeat(maxPreview + 1) { textWidths.add(0) }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        for (index in categories.indices) {
            if (index >= maxPreview) break
            AmityCommunityCategoryItemView(
                modifier = Modifier
                    .onSizeChanged {
                        textWidths[index] = it.width
                    }
                    .weight(calculateDynamicWeight(index, textWidths), fill = false),
                categoryName = categories[index].getName(),
            )
        }

        if (categories.size > maxPreview) {
            AmityCommunityCategoryOverflowCount(
                modifier = Modifier
                    .onSizeChanged {
                        textWidths[maxPreview] = it.width
                    }
                    .weight(calculateDynamicWeight(maxPreview, textWidths), fill = false),
                remainingCount = categories.size - maxPreview
            )
        }
    }
}

fun calculateDynamicWeight(index: Int, textWidths: List<Int>): Float {
    val totalWidth = textWidths.sum()
    return if (totalWidth == 0) {
        1f // Default weight if all text widths are 0 (initial state)
    } else {
        val calculatedWeight = textWidths[index].toFloat() / totalWidth // Relative weight based on text width
        if(calculatedWeight == 0.0f) 0.01f else calculatedWeight
    }
}

@Composable
fun AmityCommunityCategoryItemView(
    modifier: Modifier = Modifier,
    categoryName: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(18.dp)
            .background(
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(size = 20.dp)
            )
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = categoryName,
            style = AmityTheme.typography.captionLegacy.copy(
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
        contentAlignment = Alignment.Center,
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
            style = AmityTheme.typography.captionLegacy.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            modifier = modifier.align(Alignment.Center),
        )
    }
}