package com.amity.socialcloud.uikit.common.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class AmityPickFileContract : ActivityResultContract<String?, Uri?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "file/*"
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) intent?.data
        else null
    }
}