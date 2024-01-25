package com.amity.socialcloud.uikit

import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTargetTabComponentBehavior

@UnstableApi
class AmityUIKit4Behavior {

    var storyTabComponentBehavior: AmityStoryTargetTabComponentBehavior =
        AmitySocialBehaviorHelper.storyTabComponentBehavior
        set(value) {
            field = value
            AmitySocialBehaviorHelper.storyTabComponentBehavior = value
        }

}