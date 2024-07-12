package com.amity.socialcloud.uikit.community.compose.socialhome.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

// TODO Remove this when AmityCreatePostMenuComponent support community profile page
@Composable
fun AmityCommunityCreatePostMenuComponent(
	modifier: Modifier = Modifier,
	community: AmityCommunity,
	expanded: Boolean = false,
	onDismiss: () -> Unit = {}
) {
	val targetType = CommunityCreatePostTargetType.COMMUNITY
	val context = LocalContext.current
	
	val behavior by lazy {
		AmitySocialBehaviorHelper.createPostMenuComponentBehavior
	}
	val targetPostBehavior by lazy {
		AmitySocialBehaviorHelper.postTargetSelectionPageBehavior
	}
	val targetStoryBehavior by lazy {
		AmitySocialBehaviorHelper.storyTargetSelectionPageBehavior
	}
	
	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartActivityForResult()
	) {
	}
	
	DropdownMenu(
		offset = DpOffset(x = (-4).dp, y = 8.dp),
		expanded = expanded,
		onDismissRequest = onDismiss,
		modifier = Modifier
			.width(180.dp)
			.background(AmityTheme.colors.background)
			.clip(RoundedCornerShape(12.dp))
	) {
		DropdownMenuItem(
			text = {
				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically,
					modifier = modifier
						.padding(horizontal = 8.dp),
				) {
					Icon(
						painter = painterResource(id = R.drawable.amity_ic_post_create),
						contentDescription = "Create Post",
						tint = AmityTheme.colors.base,
						modifier = modifier.size(20.dp)
					)
					Text(
						text = "Post",
						style = AmityTheme.typography.body.copy(
							fontWeight = FontWeight.SemiBold
						)
					)
				}
			},
			onClick = {
				onDismiss()
				targetPostBehavior.goToPostComposerPage(
					context = context,
					launcher = launcher,
					targetId = community.getCommunityId(),
					targetType = AmityPostTargetType.COMMUNITY,
					community = community,
				)
			},
		)
		DropdownMenuItem(
			text = {
				
				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically,
					modifier = modifier
						.padding(horizontal = 8.dp),
				) {
					Icon(
						painter = painterResource(id = R.drawable.amity_ic_story_create),
						contentDescription = "Create Story",
						tint = AmityTheme.colors.base,
						modifier = modifier.size(20.dp)
					)
					Text(
						text = "Story",
						style = AmityTheme.typography.body.copy(
							fontWeight = FontWeight.SemiBold
						)
					)
				}
			},
			onClick = {
				onDismiss()
				targetStoryBehavior.goToStoryCreationPage(
					context = context,
					launcher = launcher,
					targetId = community.getCommunityId(),
					targetType = AmityStory.TargetType.COMMUNITY,
				)
			}
		)
	}
	
}

enum class CommunityCreatePostTargetType {
	COMMUNITY,
	USER,
}