package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.reaction.AmityReactionList
import com.amity.socialcloud.uikit.common.reaction.preview.AmityReactionPreview
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityPostNonMemberSection(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	post: AmityPost,
	onJoinButtonClick: (community: AmityCommunity) -> Unit = {},
	onShareButtonClick: (postId: String) -> Unit = {}
) {
	val context = LocalContext.current
	val behavior by lazy {
		AmitySocialBehaviorHelper.postContentComponentBehavior
	}
	var showReactionListSheet by remember { mutableStateOf(false) }

	AmityBaseElement(elementId = "non_member_section") {
		val postTarget = post.getTarget()
		Column(
			modifier = Modifier
				.padding(
					start = 16.dp,
					end = 16.dp,
				),
		) {
			Row(
				modifier = modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically,
			) {
				if (post.getReactionCount() > 0) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.clickableWithoutRipple {
								showReactionListSheet = true
							}
					) {
						AmityReactionPreview(
							pageScope = pageScope,
							componentScope = componentScope,
							myReaction = post.getMyReactions().firstOrNull(),
							reactions = post.getReactionMap(),
							reactionCount = post.getReactionCount(),
						)
					}
				}
				val commentCount = post.getCommentCount()
				if (commentCount > 0) {
					Text(
						text = pluralStringResource(
							id = R.plurals.amity_feed_comment_count,
							count = commentCount,
							commentCount.readableNumber()
						),
						style = AmityTheme.typography.captionLegacy.copy(
							fontWeight = FontWeight.Normal,
							color = AmityTheme.colors.baseShade2
						),
					)
				}
			}
			HorizontalDivider(
				color = AmityTheme.colors.divider,
			)
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 16.dp, bottom = 12.dp)
					.clickableWithoutRipple{
						if (postTarget is AmityPost.Target.COMMUNITY) {
							val community =
								(post.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()
							community?.let {
								onJoinButtonClick.invoke(it)
							}
						}
					}
			) {
				Text(
					text = "Join community to interact with all posts",
					style = AmityTheme.typography.body.copy(
						color = AmityTheme.colors.baseShade2
					),
				)
				Spacer(modifier = Modifier.weight(1f))
				val postLink = AmityUIKitConfigController.getPostLink(post)
				if (postLink.isNotEmptyOrBlank()) {
					val shouldShowIcon = if (postTarget is AmityPost.Target.COMMUNITY) {
						val community = (post.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()
						community?.isPublic() == true
					} else {
						true
					}

					if (shouldShowIcon) {
						Icon(
							imageVector = ImageVector.vectorResource(id = R.drawable.amity_v4_share_icon),
							contentDescription = "Share button",
							tint = Color.Unspecified,
							modifier = Modifier
								.size(20.dp)
								.clickableWithoutRipple {
									onShareButtonClick(post.getPostId())
								}
						)
					}

				}
			}
		}


		if (showReactionListSheet) {
			AmityReactionList(
				modifier = modifier,
				referenceType = AmityReactionReferenceType.POST,
				referenceId = post.getPostId(),
				onClose = {
					showReactionListSheet = false
				},
				onUserClick = {
					behavior.goToUserProfilePage(
						context = context,
						userId = it,
					)
				}
			)
		}
	}
}