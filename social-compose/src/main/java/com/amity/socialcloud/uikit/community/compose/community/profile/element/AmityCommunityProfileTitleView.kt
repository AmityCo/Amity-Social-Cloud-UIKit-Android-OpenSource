package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon

@Composable
fun AmityCommunityProfileTitleView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	community: AmityCommunity,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "community_title",
	) {
		Row(modifier = modifier
			.padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
			.wrapContentWidth()
		) {
			if (!community.isPublic()) {
				Box(modifier = Modifier
					.size(24.dp)
					.padding(top = 4.dp, end = 8.dp)
				) {
					AmityBaseElement(
						elementId = "community_private_badge"
					) {
						Image(
							painter = painterResource(id = getConfig().getIcon()),
							contentDescription = "Private community icon",
							modifier = Modifier
								.width(20.dp)
								.height(16.dp)
						)
					}
				}
			}
			Box(modifier = Modifier.weight(1f, fill = false)) {
				AmityBaseElement(
					pageScope = pageScope,
					componentScope = componentScope,
					elementId = "community_name"
				) {
					Text(
						text = community.getDisplayName(),
						style = TextStyle(
							fontSize = 20.sp,
							lineHeight = 24.sp,
							fontWeight = FontWeight(700),
							color = AmityTheme.colors.base,
						),
						maxLines = 2,
						overflow = TextOverflow.Ellipsis,
					)
				}
			}
			if (community.isOfficial()) {
				
				Box(modifier = Modifier
					.size(24.dp)
					.padding(top = 2.dp, start = 6.dp)
				) {
					AmityBaseElement(
						pageScope = pageScope,
						componentScope = componentScope,
						elementId = "community_verify_badge"
					) {
						Image(
							painter = painterResource(id = getConfig().getIcon()),
							contentDescription = "Verified community icon",
							modifier = Modifier
								.size(20.dp)
						)
					}
				}
			}
		}
	}
}