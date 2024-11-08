package com.amity.socialcloud.uikit.community.compose.target.poll

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostComposerPageActivity


open class AmityPollTargetSelectionPageBehavior {

    open fun goToPollPostComposerPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String,
        targetType: AmityPost.TargetType,
        community: AmityCommunity? = null,
    ) {
        val intent = AmityPollPostComposerPageActivity.newIntent(
            context = context,
            targetId = targetId,
            targetType = targetType,
            community = community,
        )
        launcher.launch(intent)
    }
}