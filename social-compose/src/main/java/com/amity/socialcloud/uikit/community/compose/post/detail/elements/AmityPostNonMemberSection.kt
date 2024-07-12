package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityPostNonMemberSection(
	modifier: Modifier = Modifier,
	componentScope: AmityComposeComponentScope? = null,
) {
	AmityBaseElement(elementId = "non_member_section") {
		Column(modifier = modifier.padding(horizontal = 16.dp)) {
			HorizontalDivider(
				color = AmityTheme.colors.divider,
			)
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 12.dp)
			) {
				Text(
					text = "Join community to interact with all posts",
					style = TextStyle(
						fontSize = 15.sp,
						lineHeight = 20.sp,
						fontWeight = FontWeight(400),
						color = AmityTheme.colors.baseShade2
					)
				)
			}
		}
	}
}