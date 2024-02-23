package com.amity.socialcloud.uikit.community.compose

import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTargetTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior

object AmitySocialBehaviorHelper {

    var createStoryPageBehavior: AmityCreateStoryPageBehavior =
        AmityCreateStoryPageBehavior()

//    var draftStoryPageBehavior: AmityDraftStoryPageBehavior =
//        AmityDraftStoryPageBehavior()


    var storyTabComponentBehavior: AmityStoryTargetTabComponentBehavior =
        AmityStoryTargetTabComponentBehavior()

    var viewStoryPageBehavior: AmityViewStoryPageBehavior =
        AmityViewStoryPageBehavior()
}