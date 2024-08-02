package com.amity.socialcloud.uikit

import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostComposerPageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.community.AmityMyCommunitiesSearchPageBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.global.AmitySocialGlobalSearchPageBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCreatePostMenuComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityGlobalFeedComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPageBehavior

class AmityUIKit4Behavior {

    var createStoryPageBehavior: AmityCreateStoryPageBehavior =
        AmitySocialBehaviorHelper.createStoryPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.createStoryPageBehavior = value
        }

    var storyTabComponentBehavior: AmityStoryTabComponentBehavior =
        AmitySocialBehaviorHelper.storyTabComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.storyTabComponentBehavior = value
        }

    var viewStoryPageBehavior: AmityViewStoryPageBehavior =
        AmitySocialBehaviorHelper.viewStoryPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.viewStoryPageBehavior = value
        }

    var socialHomePageBehavior: AmitySocialHomePageBehavior =
        AmitySocialBehaviorHelper.socialHomePageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.socialHomePageBehavior = value
        }

    var postContentComponentBehavior: AmityPostContentComponentBehavior =
        AmitySocialBehaviorHelper.postContentComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.postContentComponentBehavior = value
        }

    var postTargetSelectionPageBehavior: AmityPostTargetSelectionPageBehavior =
        AmitySocialBehaviorHelper.postTargetSelectionPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.postTargetSelectionPageBehavior = value
        }

    var storyTargetSelectionPageBehavior: AmityStoryTargetSelectionPageBehavior =
        AmitySocialBehaviorHelper.storyTargetSelectionPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.storyTargetSelectionPageBehavior = value
        }

    var socialGlobalSearchPageBehavior: AmitySocialGlobalSearchPageBehavior =
        AmitySocialBehaviorHelper.socialGlobalSearchPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.socialGlobalSearchPageBehavior = value
        }

    var createPostMenuComponentBehavior: AmityCreatePostMenuComponentBehavior =
        AmitySocialBehaviorHelper.createPostMenuComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.createPostMenuComponentBehavior = value
        }

    var postDetailPageBehavior: AmityPostDetailPageBehavior =
        AmitySocialBehaviorHelper.postDetailPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.postDetailPageBehavior = value
        }

    var globalFeedComponentBehavior: AmityGlobalFeedComponentBehavior =
        AmitySocialBehaviorHelper.globalFeedComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.globalFeedComponentBehavior = value
        }

    var communitySearchResultComponentBehavior: AmityCommunitySearchResultComponentBehavior =
        AmitySocialBehaviorHelper.communitySearchResultComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.communitySearchResultComponentBehavior = value
        }

    var userSearchResultComponentBehavior: AmityUserSearchResultComponentBehavior =
        AmitySocialBehaviorHelper.userSearchResultComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.userSearchResultComponentBehavior = value
        }

    var myCommunitiesComponentBehavior: AmityMyCommunitiesComponentBehavior =
        AmitySocialBehaviorHelper.myCommunitiesComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.myCommunitiesComponentBehavior = value
        }

    var myCommunitiesSearchPageBehavior: AmityMyCommunitiesSearchPageBehavior =
        AmitySocialBehaviorHelper.myCommunitiesSearchPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.myCommunitiesSearchPageBehavior = value
        }

    var postComposerPageBehavior: AmityPostComposerPageBehavior =
        AmitySocialBehaviorHelper.postComposerPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.postComposerPageBehavior = value
        }
    
    var communityProfilePageBehavior: AmityCommunityProfilePageBehavior =
        AmitySocialBehaviorHelper.communityProfilePageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.communityProfilePageBehavior = value
        }
}