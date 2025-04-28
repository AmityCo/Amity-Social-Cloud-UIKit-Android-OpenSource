package com.amity.socialcloud.uikit.community.compose

import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentBehavior
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityPendingPostContentComponentBehavior
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageBehavior
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageBehavior
import com.amity.socialcloud.uikit.community.compose.notificationtray.AmityNotificationTrayPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.community.AmityMyCommunitiesSearchPageBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityPostSearchResultComponentBehaviour
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.global.AmitySocialGlobalSearchPageBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCreatePostMenuComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityExploreComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityGlobalFeedComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmitySocialHomeTopNavigationComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.livestream.AmityLivestreamPostTargetSelectionPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.poll.AmityPollTargetSelectionPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPageBehavior
import com.amity.socialcloud.uikit.community.compose.user.blocked.AmityBlockedUsersPageBehavior
import com.amity.socialcloud.uikit.community.compose.user.pending.AmityUserPendingFollowRequestsPageBehavior
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserFeedComponentBehavior
import com.amity.socialcloud.uikit.community.compose.user.profile.components.AmityUserProfileHeaderComponentBehavior
import com.amity.socialcloud.uikit.community.compose.user.relationship.AmityUserRelationshipPageBehavior

object AmitySocialBehaviorHelper {

    var showPollResultInDetailFirst = false

    var createStoryPageBehavior: AmityCreateStoryPageBehavior =
        AmityCreateStoryPageBehavior()

    var storyTabComponentBehavior: AmityStoryTabComponentBehavior =
        AmityStoryTabComponentBehavior()

    var viewStoryPageBehavior: AmityViewStoryPageBehavior =
        AmityViewStoryPageBehavior()

    var socialHomePageBehavior: AmitySocialHomePageBehavior =
        AmitySocialHomePageBehavior()

    var postContentComponentBehavior: AmityPostContentComponentBehavior =
        AmityPostContentComponentBehavior()

    var postTargetSelectionPageBehavior: AmityPostTargetSelectionPageBehavior =
        AmityPostTargetSelectionPageBehavior()

    var storyTargetSelectionPageBehavior: AmityStoryTargetSelectionPageBehavior =
        AmityStoryTargetSelectionPageBehavior()

    var pollTargetSelectionPageBehavior: AmityPollTargetSelectionPageBehavior =
        AmityPollTargetSelectionPageBehavior()

    var createLivestreamPageBehavior: AmityCreateLivestreamPageBehavior =
        AmityCreateLivestreamPageBehavior()

    var notificationTrayPageBehavior: AmityNotificationTrayPageBehavior =
        AmityNotificationTrayPageBehavior()

    var livestreamTargetSelectionPageBehavior: AmityLivestreamPostTargetSelectionPageBehavior =
        AmityLivestreamPostTargetSelectionPageBehavior()

    var createPostMenuComponentBehavior: AmityCreatePostMenuComponentBehavior =
        AmityCreatePostMenuComponentBehavior()

    var socialHomeTopNavigationComponentBehavior: AmitySocialHomeTopNavigationComponentBehavior =
        AmitySocialHomeTopNavigationComponentBehavior()

    var globalFeedComponentBehavior: AmityGlobalFeedComponentBehavior =
        AmityGlobalFeedComponentBehavior()

    var communitySearchResultComponentBehavior: AmityCommunitySearchResultComponentBehavior =
        AmityCommunitySearchResultComponentBehavior()

    var userSearchResultComponentBehavior: AmityUserSearchResultComponentBehavior =
        AmityUserSearchResultComponentBehavior()

    var postSearchResultComponentBehavior: AmityPostSearchResultComponentBehaviour =
        AmityPostSearchResultComponentBehaviour()

    var myCommunitiesComponentBehavior: AmityMyCommunitiesComponentBehavior =
        AmityMyCommunitiesComponentBehavior()

    var postDetailPageBehavior: AmityPostDetailPageBehavior =
        AmityPostDetailPageBehavior()

    var socialGlobalSearchPageBehavior: AmitySocialGlobalSearchPageBehavior =
        AmitySocialGlobalSearchPageBehavior()

    var myCommunitiesSearchPageBehavior: AmityMyCommunitiesSearchPageBehavior =
        AmityMyCommunitiesSearchPageBehavior()

    var postComposerPageBehavior: AmityPostComposerPageBehavior =
        AmityPostComposerPageBehavior()

    var communityProfilePageBehavior: AmityCommunityProfilePageBehavior =
        AmityCommunityProfilePageBehavior()

    var communitySetupPageBehavior: AmityCommunitySetupPageBehavior =
        AmityCommunitySetupPageBehavior()

    var communitySettingPageBehavior: AmityCommunitySettingPageBehavior =
        AmityCommunitySettingPageBehavior()

    var communityNotificationSettingPageBehavior: AmityCommunityNotificationSettingPageBehavior =
        AmityCommunityNotificationSettingPageBehavior()

    var communityMembershipPageBehavior: AmityCommunityMembershipPageBehavior =
        AmityCommunityMembershipPageBehavior()

    var userProfilePageBehavior: AmityUserProfilePageBehavior = AmityUserProfilePageBehavior()

    var userProfileHeaderComponentBehavior: AmityUserProfileHeaderComponentBehavior =
        AmityUserProfileHeaderComponentBehavior()

    var userRelationshipPageBehavior: AmityUserRelationshipPageBehavior =
        AmityUserRelationshipPageBehavior()

    var userPendingFollowRequestsPageBehavior: AmityUserPendingFollowRequestsPageBehavior =
        AmityUserPendingFollowRequestsPageBehavior()

    var blockedUsersPageBehavior: AmityBlockedUsersPageBehavior =
        AmityBlockedUsersPageBehavior()

    var userFeedComponentBehavior: AmityUserFeedComponentBehavior = AmityUserFeedComponentBehavior()

    var pendingPostContentComponentBehavior: AmityPendingPostContentComponentBehavior =
        AmityPendingPostContentComponentBehavior()

    var exploreComponentBehavior: AmityExploreComponentBehavior = AmityExploreComponentBehavior()

    var commentTrayComponentBehavior: AmityCommentTrayComponentBehavior =
        AmityCommentTrayComponentBehavior()
}