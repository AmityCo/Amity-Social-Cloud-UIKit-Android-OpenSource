package com.amity.socialcloud.uikit.community.compose.event.detail

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehavior
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehaviorContext
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.event.attendees.AmityEventAttendeesPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerOptions
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostComposerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity

open class AmityEventDetailPageBehavior : AmityBaseBehavior() {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val community: AmityCommunity? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher)

    open fun goToUserProfilePage(
        context: Context,
        userId: String,
    ) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context.pageContext,
            userId = userId,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToCommunityProfilePage(
        context: Context,
        communityId: String,
    ) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context.pageContext,
            communityId = communityId,
        )
        context.pageContext.startActivity(intent)
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
            eventHostId = null,
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

    open fun goToCreatePollPage(
        context: Context,
        pollType: String,
    ) {
        val intent = AmityPollPostComposerPageActivity.newIntent(
            context = context.pageContext,
            targetId = context.community?.getCommunityId()!!,
            targetType = AmityPost.TargetType.COMMUNITY,
            community = context.community,
            pollType = pollType
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToEditEventPage(
        context: Context,
        eventId: String,
    ) {
        val intent = com.amity.socialcloud.uikit.community.compose.event.setup.AmityEventSetupPageActivity.newIntent(
            context = context.pageContext,
            mode = com.amity.socialcloud.uikit.community.compose.event.setup.AmityEventSetupPageMode.Edit(eventId = eventId),
        )
        context.activityLauncher?.launch(intent) ?: context.pageContext.startActivity(intent)
    }

    open fun goToLivestreamPostComposerPage(
        context: Context,
    ) {
        val intent = AmityCreateLivestreamPageActivity.newIntent(
            context = context.pageContext,
            targetId = context.community?.getCommunityId() ?: "",
            targetType = AmityPost.TargetType.COMMUNITY,
        )
        context.pageContext.startActivity(intent)
    }

    open fun goToEventAttendeesPage(
        context: Context,
        eventId: String,
    ) {
        val intent = AmityEventAttendeesPageActivity.newIntent(
            context = context.pageContext,
            eventId = eventId
        )
        context.pageContext.startActivity(intent)
    }

    open fun handleVisitorUserAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleVisitorUserAction()
    }

    open fun handleNonMemberAction() {
        AmitySocialBehaviorHelper.globalBehavior.handleNonMemberAction()
    }
}
