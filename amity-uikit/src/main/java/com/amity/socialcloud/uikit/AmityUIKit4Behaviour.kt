package com.amity.socialcloud.uikit

import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTargetTabComponentBehavior
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageBehavior

class AmityUIKit4Behavior {

    var createStoryPageBehavior: AmityCreateStoryPageBehavior =
        AmitySocialBehaviorHelper.createStoryPageBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.createStoryPageBehavior = value
        }

    var storyTabComponentBehavior: AmityStoryTargetTabComponentBehavior =
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
}