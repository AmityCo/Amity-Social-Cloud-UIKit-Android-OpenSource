package com.amity.socialcloud.uikit.common.behavior

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext



open class AmityGlobalBehavior : AmityBaseBehavior()  {

    class Context(
        val pageContext: android.content.Context,
        val activityLauncher: ActivityResultLauncher<Intent>? = null,
        val product: AmityProduct? = null,
        val communityId: String? = null,
        // Set by UIKit when the tap comes from a livestream player that can float. Do not read
        // it directly — call [startActivityWithPictureInPicture]. Null elsewhere, so that helper
        // simply starts the Activity.
        private val pipNavigator: ((Intent) -> Unit)? = null,
    ) : AmityBaseBehaviorContext(pageContext, activityLauncher) {

        /**
         * Navigate from a livestream product tap **and keep the stream floating** over the
         * destination. Call this instead of [pageContext].startActivity: on Android system
         * Picture-in-Picture must be entered before the player loses the foreground and the
         * destination must land in its own task, and UIKit owns that ordering. Passing the
         * Intent here lets UIKit enter PiP first, then launch it once the window is pinned.
         *
         * Outside a floating-capable player this is a plain `startActivity`, so it is always safe
         * to call. Do not add launch flags — UIKit adds the task flags PiP needs.
         */
        fun startActivityWithPictureInPicture(intent: Intent) {
            pipNavigator?.invoke(intent) ?: pageContext.startActivity(intent)
        }
    }

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

    open fun handleVisitorUsageLimitReached() {
        val intent = Intent()
        intent.setClassName(
            AmityAppContext.getContext(),
            "com.amity.socialcloud.uikit.community.compose.visitor.AmityVisitorUsageLimitPageActivity"
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        AmityAppContext.getContext().startActivity(intent)
    }

    open fun handleVisitorUsageLimitSignIn() {
        AmityUIKitSnackbar.publishSnackbarMessage("Create an account or sign in to continue.")
    }

    open fun onPostProductTagClick(context: AmityGlobalBehavior.Context): Boolean {
        return false
    }

    /**
     * Called when a viewer taps a tagged product in a livestream player.
     *
     * Return `true` to claim the tap — you have handled the navigation and UIKit does nothing
     * further. Return `false` (the default) to get UIKit's built-in product web view, which
     * leaves the stream playing in a floating window over it. The default does **not** navigate.
     *
     * To keep the stream floating over your destination, navigate with
     * [Context.startActivityWithPictureInPicture] rather than starting the Activity yourself —
     * UIKit enters PiP first, then launches your Intent into its own task. Starting the Activity
     * directly enters PiP too late (the window never opens) and, with the wrong flags, leaves the
     * destination with no back stack so Back exits the app. [context] carries the tapped
     * [AmityProduct] and, for a community livestream, its [Context.communityId].
     */
    open fun onLivestreamProductTagClick(context: AmityGlobalBehavior.Context): Boolean {
        return false
    }
}