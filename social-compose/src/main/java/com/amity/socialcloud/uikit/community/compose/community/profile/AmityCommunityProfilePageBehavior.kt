package com.amity.socialcloud.uikit.community.compose.community.profile

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehaviorContext
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageActivity
import com.amity.socialcloud.uikit.community.compose.community.pending.AmityPendingPostsPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageActivity

open class AmityCommunityProfilePageBehavior : AmityBaseBehavior() {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val community: AmityCommunity? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher) {
        /*
        data class Community(
            val context: android.content.Context,
            val launcher: ActivityResultLauncher<Intent>? = null,
            val community: AmityCommunity,
        ) : Context(context, launcher)

        data class Post(
            val context: android.content.Context,
            val launcher: ActivityResultLauncher<Intent>? = null,
            val postId: String,
            val postCategory: AmityPostCategory = AmityPostCategory.GENERAL,
        ) : Context(context, launcher)
         */
    }

    open fun goToPostDetailPage(
        context: Context,
        postId: String,
        category: AmityPostCategory,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context.pageContext,
            id = postId,
            category = category,
            hideTarget = true,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToPostComposerPage(
        context: Context,
    ) {
        val intent = AmityPostComposerPageActivity.newIntent(
            context = context.pageContext,
            options = AmityPostComposerOptions.AmityPostComposerCreateOptions(
                targetId = context.community?.getCommunityId(),
                targetType = AmityPostTargetType.COMMUNITY,
                community = context.community,
            )
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToCreateStoryPage(
        context: Context,
    ) {
        val intent = AmityCreateStoryPageActivity.newIntent(
            context = context.pageContext,
            targetId = context.community?.getCommunityId()!!,
            targetType = AmityStory.TargetType.COMMUNITY,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToCreatePollPage(
        context: Context,
    ) {
        val intent = AmityPollPostComposerPageActivity.newIntent(
            context = context.pageContext,
            targetId = context.community?.getCommunityId()!!,
            targetType = AmityPost.TargetType.COMMUNITY,
            community = context.community,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToCreateLivestreamPage(
        context: Context,
    ) {
        val intent = AmityCreateLivestreamPageActivity.newIntent(
            context = context.pageContext,
            targetId = context.community?.getCommunityId()!!,
            targetType = AmityPost.TargetType.COMMUNITY,
            community = context.community,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToCommunitySettingPage(
        context: Context,
    ) {
        val intent = AmityCommunitySettingPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToPendingPostPage(
        context: Context,
    ) {
        val intent = AmityPendingPostsPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToMemberListPage(
        context: Context,
    ) {
        val intent = AmityCommunityMembershipPageActivity.newIntent(
            context = context.pageContext,
            community = context.community!!,
        )
        context.pageContext.startActivity(intent)
    }
}