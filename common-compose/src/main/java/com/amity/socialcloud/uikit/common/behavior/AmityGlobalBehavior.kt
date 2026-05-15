package com.amity.socialcloud.uikit.common.behavior

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider



open class AmityGlobalBehavior : AmityBaseBehavior()  {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val product: AmityProduct? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher)

    open fun handleVisitorUserAction() {
        AmityUIKitSnackbar.publishSnackbarMessage(
            DefaultAmityCommonStringProvider.getInstance().getString("amity_common_label_sign_in_to_continue")
        )
    }

    open fun handleNonMemberAction() {
        AmityUIKitSnackbar.publishSnackbarMessage(
            DefaultAmityCommonStringProvider.getInstance().getString("amity_common_label_join_community_to_interact")
        )
    }

    open fun handleNonFollowerAction() {
        AmityUIKitSnackbar.publishSnackbarMessage(
            DefaultAmityCommonStringProvider.getInstance().getString("amity_common_label_follow_user_to_interact")
        )
    }

    open fun onPostProductTagClick(context: AmityGlobalBehavior.Context): Boolean {
        return false
    }

    open fun onLivestreamProductTagClick(context: AmityGlobalBehavior.Context): Boolean {
        return false
    }
}