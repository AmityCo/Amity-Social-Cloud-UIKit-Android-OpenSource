package com.amity.socialcloud.uikit.common.behavior

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar



open class AmityGlobalBehavior : AmityBaseBehavior()  {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val product: AmityProduct? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher)

    open fun handleVisitorUserAction() {
        AmityUIKitSnackbar.publishSnackbarMessage("Create an account or sign in to continue.")
    }

    open fun handleNonMemberAction() {
        AmityUIKitSnackbar.publishSnackbarMessage("Join community to interact.")
    }

    open fun handleNonFollowerAction() {
        AmityUIKitSnackbar.publishSnackbarMessage("Follow user to interact.")
    }

    open fun onPostProductTagClick(context: AmityGlobalBehavior.Context): Boolean {
        return false
    }

    open fun onLivestreamProductTagClick(context: AmityGlobalBehavior.Context): Boolean {
        return false
    }
}