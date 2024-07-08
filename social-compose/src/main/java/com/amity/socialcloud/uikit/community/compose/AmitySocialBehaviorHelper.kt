package com.amity.socialcloud.uikit.community.compose

import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityCreatePostMenuComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityGlobalFeedComponentBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityMyCommunitiesComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.post.AmityPostTargetSelectionPageBehavior
import com.amity.socialcloud.uikit.community.compose.target.story.AmityStoryTargetSelectionPageBehavior

object AmitySocialBehaviorHelper {

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

    var createPostMenuComponentBehavior: AmityCreatePostMenuComponentBehavior =
        AmityCreatePostMenuComponentBehavior()

    var globalFeedComponentBehavior: AmityGlobalFeedComponentBehavior =
        AmityGlobalFeedComponentBehavior()

    var communitySearchResultComponentBehavior: AmityCommunitySearchResultComponentBehavior =
        AmityCommunitySearchResultComponentBehavior()

    var userSearchResultComponentBehavior: AmityUserSearchResultComponentBehavior =
        AmityUserSearchResultComponentBehavior()

    var myCommunitiesComponentBehavior: AmityMyCommunitiesComponentBehavior =
        AmityMyCommunitiesComponentBehavior()
}