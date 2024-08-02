package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityCommunityCategoryListElement(
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	categories: List<String>
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "community_categories"
	) {
		Box {
			LazyRow(
				horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
				verticalAlignment = Alignment.CenterVertically,
			) {
				item { Spacer(modifier = Modifier.width(8.dp)) }
				items(categories.size) { index ->
					CategoryItem(categories[index])
				}
			}
		}
	}
}

@Composable
fun CategoryItem(title: String) {
	Box(modifier = Modifier
		.wrapContentWidth()
		.wrapContentHeight()
		.background(
			color = AmityTheme.colors.baseShade4,
			shape = RoundedCornerShape(size = 20.dp))
		.padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 2.dp)) {
		Text(
			text = title,
			style = TextStyle(
				fontSize = 13.sp,
				lineHeight = 18.sp,
				fontWeight = FontWeight(400),
				color = AmityTheme.colors.base,
				textAlign = TextAlign.Center,
			)
		)
	}
}