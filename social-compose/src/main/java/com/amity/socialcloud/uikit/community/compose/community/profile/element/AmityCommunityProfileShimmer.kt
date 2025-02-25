package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.shimmerBackground

@Composable
fun AmityCommunityProfileShimmer(modifier: Modifier = Modifier) {
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
			.fillMaxWidth()
			.background(AmityTheme.colors.background)
	) {
		Box(
			modifier = modifier
				.fillMaxWidth()
				.height(188.dp)
				.shimmerBackground(
					color = AmityTheme.colors.baseShade4,
				)
		)
		Column(modifier = Modifier.padding(16.dp)) {
			Box(modifier = Modifier
				.width(200.dp)
				.height(12.dp)
				.shimmerBackground(
					color = AmityTheme.colors.baseShade4,
					shape = RoundedCornerShape(size = 12.dp)
				)
			)
			Spacer(modifier = Modifier.height(16.dp))
			Row {
				Box(
					modifier = Modifier
						.width(54.dp)
						.height(12.dp)
						.shimmerBackground(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 12.dp)
						)
				)
				Spacer(modifier = Modifier.width(12.dp))
				Box(
					modifier = Modifier
						.width(54.dp)
						.height(12.dp)
						.shimmerBackground(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 12.dp)
						)
				)
				Spacer(modifier = Modifier.width(12.dp))
				Box(
					modifier = Modifier
						.width(54.dp)
						.height(12.dp)
						.shimmerBackground(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 12.dp)
						)
				)
			}
			Spacer(modifier = Modifier.height(21.dp))
			Box(modifier = Modifier
				.width(240.dp)
				.height(12.dp)
				.shimmerBackground(
					color = AmityTheme.colors.baseShade4,
					shape = RoundedCornerShape(size = 12.dp)
				)
			)
			Spacer(modifier = Modifier.height(16.dp))
			Box(modifier = Modifier
				.width(297.dp)
				.height(12.dp)
				.shimmerBackground(
					color = AmityTheme.colors.baseShade4,
					shape = RoundedCornerShape(size = 12.dp)
				)
			)
			Spacer(modifier = Modifier.height(16.dp))
			Row {
				Box(
					modifier = Modifier
						.width(54.dp)
						.height(12.dp)
						.shimmerBackground(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 12.dp)
						)
				)
				Spacer(modifier = Modifier.width(12.dp))
				Box(
					modifier = Modifier
						.width(54.dp)
						.height(12.dp)
						.shimmerBackground(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 12.dp)
						)
				)
			}

			Spacer(modifier = Modifier.height(32.dp))
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = modifier.width(64.dp)
			) {
				Box(
					modifier = modifier
						.size(40.dp)
						.shimmerBackground(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(40.dp)
						)
				)
				Spacer(modifier = Modifier.height(12.dp))
				Box(
					modifier = Modifier
						.width(60.dp)
						.height(12.dp)
						.shimmerBackground(
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 12.dp)
						)
				)
			}
		}
	}
}