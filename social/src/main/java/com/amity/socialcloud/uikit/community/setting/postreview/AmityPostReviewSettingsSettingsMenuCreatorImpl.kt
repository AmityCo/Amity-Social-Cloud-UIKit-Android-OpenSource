package com.amity.socialcloud.uikit.community.setting.postreview

import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.Flowable

class AmityPostReviewSettingsSettingsMenuCreatorImpl(private val fragment: AmityPostReviewSettingsFragment) : AmityPostReviewSettingsMenuCreator {

    override fun createApproveMemberPostMenu(isChecked: Flowable<Boolean>): AmitySettingsItem.ToggleContent {
        return AmitySettingsItem.ToggleContent(
                title = R.string.amity_approve_member_post,
                description = R.string.amity_approve_member_post_desc,
                isToggled = isChecked,
                isTitleBold = true,
                callback = fragment::toggleApproveMemberPostEvent
        )
    }
}