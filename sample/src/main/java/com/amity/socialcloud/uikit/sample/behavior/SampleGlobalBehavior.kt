package com.amity.socialcloud.uikit.sample.behavior

import android.content.Intent
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageActivity
import com.amity.socialcloud.uikit.sample.login.LoginPreferences

/**
 * The sample app's behaviour overrides.
 *
 * Exists mainly to exercise the livestream product-tag hook end to end: an integrator claiming the
 * tap, navigating somewhere of their own, and the stream continuing in a floating window over it.
 * That path is hard to test otherwise, since UIKit's own destination is the product web view.
 */
class SampleGlobalBehavior : AmityGlobalBehavior() {

    /**
     * With **In-app PiP Testing** on (login → Advanced → Behaviour), a product tap goes to the
     * social home page rather than the product web view, so the floating window can be checked
     * against a destination UIKit did not choose. Off, this returns false and UIKit opens its own
     * product page as usual.
     *
     * The Intent is handed to [AmityGlobalBehavior.Context.startActivityWithPictureInPicture] rather than started
     * here: on Android the stream only floats if Picture-in-Picture is entered while the player is
     * still resumed and the destination lands in its own task, and UIKit owns that ordering.
     * Starting it directly would enter PiP too late for a window to open, and leave the
     * destination with no back stack.
     */
    override fun onLivestreamProductTagClick(context: AmityGlobalBehavior.Context): Boolean {
        if (!LoginPreferences.isInAppPipTestingEnabled(context.pageContext)) {
            return false
        }
        // No launch flags — UIKit adds the task flags Picture-in-Picture needs.
        val intent = Intent(context.pageContext, AmitySocialHomePageActivity::class.java)
        context.startActivityWithPictureInPicture(intent)
        return true
    }
}
