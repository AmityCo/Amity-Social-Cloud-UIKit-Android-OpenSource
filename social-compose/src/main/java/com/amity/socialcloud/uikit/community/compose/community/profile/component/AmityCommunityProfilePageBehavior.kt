package com.amity.socialcloud.uikit.community.compose.community.profile.component

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory

open class AmityCommunityProfilePageBehavior {

    open fun goToPostDetailPage(
        context: Context,
        postId: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = postId,
            category = category,
            hideTarget = true,
        )
        context.startActivity(intent)
    }
    
    fun goToPostComposerPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        community: AmityCommunity,
    ) {
        AmitySocialBehaviorHelper
            .postTargetSelectionPageBehavior
            .goToPostComposerPage(
                context = context,
                launcher = launcher,
                targetId = community.getCommunityId(),
                targetType = AmityPostTargetType.COMMUNITY,
                community = community,
            )
    }
    
    fun goToCreateStoryPage (
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        community: AmityCommunity,
    ) {
        AmitySocialBehaviorHelper
            .storyTargetSelectionPageBehavior
            .goToStoryCreationPage(
                context = context,
                launcher = launcher,
                targetId = community.getCommunityId(),
                targetType = AmityStory.TargetType.COMMUNITY,
            )
    }
    
    open fun goToCommunitySettingPage(
        context: Context,
        communityId: String,
    ) {
    
    }
    
    open fun goToPendingPostPage(
        context: Context,
        communityId: String,
    ) {
    
    }
    open fun goToMemberListPage(
        context: Context,
        community: AmityCommunity,
    ) {
    
    }
}