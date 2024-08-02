package com.amity.socialcloud.uikit

import android.content.Context
import android.content.Intent
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.members.AmityCommunityMemberSettingsActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityCommunityReviewingFeedActivity
import com.amity.socialcloud.uikit.community.setting.AmityCommunitySettingsActivity
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID

class AmityCustomCommunityProfilePageBehavior : AmityCommunityProfilePageBehavior() {
	override fun goToCommunitySettingPage(
		context: Context,
		communityId: String,
	) {
		AmityCommunitySettingsActivity.newIntent(
			context = context,
			id = communityId,
		).also {
			context.startActivity(it)
		}
	}
	
	override fun goToPendingPostPage(context: Context, communityId: String) {
		val intent = Intent(context, AmityCommunityReviewingFeedActivity::class.java)
		intent.putExtra(EXTRA_PARAM_POST_ID, communityId)
		context.startActivity(intent)
	}
	
	override fun goToMemberListPage(context: Context, community: AmityCommunity) {
		val intent = AmityCommunityMemberSettingsActivity.newIntent(
			context,
			id = community.getCommunityId(),
			isMember = community.isJoined(),
		)
		context.startActivity(intent)
	}
}
