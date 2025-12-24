package com.amity.socialcloud.uikit.community.compose.target.event

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.amity.socialcloud.uikit.community.compose.event.setup.AmityEventSetupPageActivity
import com.amity.socialcloud.uikit.community.compose.event.setup.AmityEventSetupPageMode

open class AmityEventTargetSelectionPageBehavior {

    open fun goToEventSetupPage(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        targetId: String? = null,
        targetType: AmityEventTargetType,
        mode: AmityEventSetupPageMode = AmityEventSetupPageMode.Create(),
    ) {
        val intent = AmityEventSetupPageActivity.newIntent(
            context = context,
            mode = mode
        )
        // TODO: Pass targetId and targetType to the event setup page when backend integration is ready
        launcher.launch(intent)
    }
}
