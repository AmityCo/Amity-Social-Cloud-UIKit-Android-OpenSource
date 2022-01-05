package com.amity.socialcloud.uikit.community.setting.postreview

import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.Flowable

interface AmityPostReviewSettingsMenuCreator {
    fun createApproveMemberPostMenu(isChecked: Flowable<Boolean>): AmitySettingsItem.ToggleContent
}