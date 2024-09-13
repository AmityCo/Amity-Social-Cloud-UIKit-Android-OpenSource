package com.amity.socialcloud.uikit.common.behavior

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

open class AmityBaseBehaviorContext(
    private val baseContext: android.content.Context,
    private val baseLauncher: ActivityResultLauncher<Intent>? = null,
) {
    fun launchActivity(intent: Intent) {
        if (baseLauncher == null) {
            baseContext.startActivity(intent)
        } else {
            baseLauncher.launch(intent)
        }
    }
}