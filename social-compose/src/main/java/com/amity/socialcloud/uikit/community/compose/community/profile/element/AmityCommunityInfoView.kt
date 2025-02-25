package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil.getNumberAbbreveation
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior

@Composable
fun AmityCommunityInfoView(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	community: AmityCommunity? = null,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "community_info"
	) {
		val behavior = remember {
			AmitySocialBehaviorHelper.communityProfilePageBehavior
		}
		val context = LocalContext.current
		Row(
			modifier = Modifier
				.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				text = getNumberAbbreveation(community?.getPostCount() ?: 0),
				style = TextStyle(
					fontSize = 15.sp,
					lineHeight = 20.sp,
					fontWeight = FontWeight(600),
					color = AmityTheme.colors.base,
				)
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(
				text = "posts",
				style = TextStyle(
					fontSize = 13.sp,
					lineHeight = 18.sp,
					fontWeight = FontWeight(400),
					color = AmityTheme.colors.baseShade2,
				)
			)
			Box(modifier = Modifier
				.padding(horizontal = 16.dp)
				.width(1.dp)
				.height(20.dp)
				.background(color = AmityTheme.colors.baseShade4)
            ) {}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.clickable {
				community?.let{
                    behavior.goToMemberListPage(
						AmityCommunityProfilePageBehavior.Context(
							pageContext = context,
                            community = it
                        )
                    )
				}
			}) {
				Text(
					text = getNumberAbbreveation(community?.getMemberCount() ?: 0),
					style = TextStyle(
						fontSize = 15.sp,
						lineHeight = 20.sp,
						fontWeight = FontWeight(600),
						color = AmityTheme.colors.base,
					)
				)
				Spacer(modifier = Modifier.width(4.dp))
				Text(
					text = "members",
					style = TextStyle(
						fontSize = 13.sp,
						lineHeight = 18.sp,
						fontWeight = FontWeight(400),
						color = AmityTheme.colors.baseShade2,
					)
				)
			}
		}
	}
}