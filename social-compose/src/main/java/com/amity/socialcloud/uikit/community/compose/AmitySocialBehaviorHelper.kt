package com.amity.socialcloud.uikit.community.compose

import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.uikit.community.compose.story.create.AmityCreateStoryPageBehavior
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTargetTabComponentBehavior

@UnstableApi
object AmitySocialBehaviorHelper {

    var createStoryPageBehavior: AmityCreateStoryPageBehavior =
        AmityCreateStoryPageBehavior()

//    var draftStoryPageBehavior: AmityDraftStoryPageBehavior =
//        AmityDraftStoryPageBehavior()


    var storyTabComponentBehavior: AmityStoryTargetTabComponentBehavior =
        AmityStoryTargetTabComponentBehavior()

}