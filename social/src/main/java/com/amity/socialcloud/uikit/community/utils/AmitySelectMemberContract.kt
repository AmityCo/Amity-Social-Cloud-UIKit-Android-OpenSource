package com.amity.socialcloud.uikit.community.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.ui.view.AmityMemberPickerActivity

class AmitySelectMemberContract :
    ActivityResultContract<ArrayList<AmitySelectMemberItem>, ArrayList<AmitySelectMemberItem>>() {
    override fun createIntent(context: Context, input: ArrayList<AmitySelectMemberItem>): Intent {
        return Intent(context, AmityMemberPickerActivity::class.java).apply {
            putParcelableArrayListExtra(AmityConstants.MEMBERS_LIST, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ArrayList<AmitySelectMemberItem>? {
        return if (resultCode == Activity.RESULT_OK) intent?.getParcelableArrayListExtra(
            AmityConstants.MEMBERS_LIST
        )
        else null
    }
}