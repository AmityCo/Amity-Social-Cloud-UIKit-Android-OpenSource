package com.amity.socialcloud.uikit.community.compose.community.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCategoryListElement
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCoverView
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityInfoView
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileActionView
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileTitleView
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentType

@Composable
fun AmityCommunityHeaderComponent(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	community: AmityCommunity,
) {
	AmityBaseComponent(
		pageScope = pageScope,
		componentId = "community_header"
	) {
		val categories = community.getCategories().map { it.getName() }.filter { it.isNotEmpty() }
		Column(modifier = modifier.fillMaxWidth()) {
			AmityCommunityCoverView(community = community)
			Column(modifier = Modifier.padding(top = 16.dp)) {
				AmityCommunityProfileTitleView(community = community)
				if (categories.isNotEmpty()) { AmityCommunityCategoryListElement(categories = categories) }
				if (community.getDescription().isNotBlank()) {
					Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
						Text(
							text = community.getDescription(),
							style = TextStyle(
								fontSize = 15.sp,
								lineHeight = 20.sp,
								fontWeight = FontWeight(400),
								color = AmityTheme.colors.base,
							)
						)
					}
				}
				AmityCommunityInfoView(community = community)
				if (community.isJoined()) {
					Row(modifier = Modifier.padding(bottom = 12.dp, start = 16.dp, end = 16.dp).fillMaxWidth().align(Alignment.Start)) {
						AmityStoryTabComponent(
							type = AmityStoryTabComponentType.CommunityFeed(
								communityId = community.getCommunityId(),
							)
						)
					}
				}
				AmityCommunityProfileActionView(pageScope = pageScope, componentScope = getComponentScope(), community = community)
			}
		}
	}
}