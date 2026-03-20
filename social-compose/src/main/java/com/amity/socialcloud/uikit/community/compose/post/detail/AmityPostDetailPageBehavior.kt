package com.amity.socialcloud.uikit.community.compose.post.detail

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.behavior.AmityBaseBehaviorContext
import com.amity.socialcloud.uikit.common.utils.closePage

open class AmityPostDetailPageBehavior {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val post: AmityPost? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher)

        open fun closePage(context: Context) {
            context.pageContext.closePage()
        }
}