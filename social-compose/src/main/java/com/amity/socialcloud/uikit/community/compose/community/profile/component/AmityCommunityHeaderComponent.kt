package com.amity.socialcloud.uikit.community.compose.community.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCategoryListElement
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityCoverView
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityInfoView
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityJoinButton
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityPendingPost
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileTitleView
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentType

@Composable
fun AmityCommunityHeaderComponent(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	community: AmityCommunity,
	style: AmityCommunityHeaderStyle = AmityCommunityHeaderStyle.EXPANDED,
) {
	AmityBaseComponent(
		pageScope = pageScope,
		componentId = "community_header"
	) {
		val categories = community.getCategories().map { it.getName() }.filter { it.isNotEmpty() }
		if (style == AmityCommunityHeaderStyle.EXPANDED) {
			Column(modifier = modifier
                .fillMaxWidth()
                .background(AmityTheme.colors.background)
			) {
				AmityCommunityCoverView(
					pageScope = pageScope,
					componentScope = getComponentScope(),
					community = community,
					style = AmityCommunityHeaderStyle.EXPANDED
				)
				Column(modifier = Modifier.padding(top = 16.dp)) {
					AmityCommunityProfileTitleView(
						pageScope = pageScope,
						componentScope = getComponentScope(),
						community = community,
					)
					if (categories.isNotEmpty()) {
						AmityCommunityCategoryListElement(categories = categories)
					}

                    if (community.getDescription().isNotEmptyOrBlank()) {
						Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
							AmityExpandableText(
								text = community.getDescription(),
								previewLines = 4,
								modifier = modifier.padding(vertical = 8.dp)
							)
						}
                    }

					AmityCommunityInfoView(
						pageScope = pageScope,
						componentScope = getComponentScope(),
						community = community
					)

                    AmityCommunityJoinButton(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        community = community
                    )
					Row(
						modifier = Modifier
                            .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                            .align(Alignment.Start)
					) {
						AmityStoryTabComponent(
							type = AmityStoryTabComponentType.CommunityFeed(
								communityId = community.getCommunityId(),
							)
						)
					}
                    AmityCommunityPendingPost(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        community = community
                    )
				}
			}
		} else {
			Column {
				AmityCommunityCoverView(
					pageScope = pageScope,
					componentScope = getComponentScope(),
					community = community,
					style = AmityCommunityHeaderStyle.COLLAPSE
				)
				community.let {
					if (!community.isJoined()) {
						AmityCommunityJoinButton(
							modifier = Modifier.background(AmityTheme.colors.background),
							pageScope = pageScope,
							componentScope = getComponentScope(),
							community = community,
						)
					}
				}
			}
		}
	}
}

enum class AmityCommunityHeaderStyle {
	EXPANDED, COLLAPSE
}