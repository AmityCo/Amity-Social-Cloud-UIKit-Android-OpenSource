@file:OptIn(com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi::class)

package com.amity.socialcloud.uikit.community.compose

import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior
import com.amity.socialcloud.uikit.community.compose.clip.create.AmityCreateClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.clip.draft.AmityDraftClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentBehavior
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityPendingPostContentComponentBehavior
import com.amity.socialcloud.uikit.community.compose.community.pending.elements.AmityPendingRequestComponentBehavior
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.community.setting.AmityCommunitySettingPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.setting.notifications.AmityCommunityNotificationSettingPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageBehavior
import com.amity.socialcloud.uikit.community.compose.livestream.create.AmityCreateLivestreamPageBehavior
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.AmityCreateRoomPageBehavior
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
import com.amity.socialcloud.uikit.community.compose.discoverywidget.AmityDiscoveryWidgetComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityGlobalFeedComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmitySocialHomeTopNavigationComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.event.AmityEventTargetSelectionPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.eventpost.AmityEventPostTargetSelectionPageBehavior
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
import com.amity.socialcloud.uikit.common.config.AmityUIKitDataGate
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature

object AmitySocialBehaviorHelper {

    // Nine feeds ask for these types. A request that names dataTypes[]=clip is
    // asking the backend for clips, so with clip switched off the list has to
    // drop it - otherwise every one of those feeds makes a clip request, and an
    // error response about clips reaches a UI that cannot show clips at all.
    // Computed on each read, not stored: overrides arrive after this object is
    // first touched.
    val supportedPostTypes: List<AmityPost.DataType>
        get() = listOfNotNull(
            AmityPost.DataType.TEXT,
            AmityPost.DataType.IMAGE,
            AmityPost.DataType.VIDEO,
            AmityPost.DataType.POLL.takeIf { AmityUIKitDataGate.isOn(AmityUIKitFeature.POLL) },
            AmityPost.DataType.LIVE_STREAM.takeIf { AmityUIKitDataGate.isOn(AmityUIKitFeature.LIVE) },
            AmityPost.DataType.CLIP.takeIf { AmityUIKitDataGate.isClipOn() },
            AmityPost.DataType.ROOM.takeIf { AmityUIKitDataGate.isOn(AmityUIKitFeature.LIVE) },
            AmityPost.DataType.EVENT.takeIf { AmityUIKitDataGate.isOn(AmityUIKitFeature.EVENTS) },
        )

    val supportedStructureTypes = listOf(
        AmityPost.StructureType.TEXT,
        AmityPost.StructureType.IMAGE,
        AmityPost.StructureType.VIDEO,
        AmityPost.StructureType.POLL,
        AmityPost.StructureType.LIVESTREAM,
        AmityPost.StructureType.CLIP,
        AmityPost.StructureType.ROOM,
        AmityPost.StructureType.EVENT,
    )

    var showPollResultInDetailFirst = false

    var globalBehavior: AmityGlobalBehavior =
        AmityGlobalBehavior()

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

    var eventTargetSelectionPageBehavior: AmityEventTargetSelectionPageBehavior =
        AmityEventTargetSelectionPageBehavior()

    var eventPostTargetSelectionPageBehavior: AmityEventPostTargetSelectionPageBehavior =
        AmityEventPostTargetSelectionPageBehavior()

    var eventDetailPageBehavior: AmityEventDetailPageBehavior =
        AmityEventDetailPageBehavior()

    var createLivestreamPageBehavior: AmityCreateLivestreamPageBehavior =
        AmityCreateLivestreamPageBehavior()

    var createRoomPageBehavior: AmityCreateRoomPageBehavior =
        AmityCreateRoomPageBehavior()

    var createClipPageBehavior: AmityCreateClipPageBehavior =
        AmityCreateClipPageBehavior()

    var clipDraftPageBehavior: AmityDraftClipPageBehavior = AmityDraftClipPageBehavior()

    var viewClipPageBehavior: AmityClipPageBehavior =
        AmityClipPageBehavior()

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

    var discoveryWidgetComponentBehavior: AmityDiscoveryWidgetComponentBehavior =
        AmityDiscoveryWidgetComponentBehavior()

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

    var pendingRequestComponentBehavior: AmityPendingRequestComponentBehavior =
        AmityPendingRequestComponentBehavior()

    var exploreComponentBehavior: AmityExploreComponentBehavior = AmityExploreComponentBehavior()

    var commentTrayComponentBehavior: AmityCommentTrayComponentBehavior =
        AmityCommentTrayComponentBehavior()
}