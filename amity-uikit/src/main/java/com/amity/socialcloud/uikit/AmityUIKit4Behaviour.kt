package com.amity.socialcloud.uikit

import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior

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

    var postContentComponentBehavior: AmityPostContentComponentBehavior =
        AmitySocialBehaviorHelper.postContentComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.postContentComponentBehavior = value
        }
}