package com.amity.socialcloud.uikit.community.setting.story

import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.rxjava3.core.Flowable

class AmityStorySettingsMenuCreatorImpl(private val fragment: AmityStorySettingsFragment) :
    AmityStorySettingsMenuCreator {

    override fun createAllowCommentMenu(isChecked: Flowable<Boolean>): AmitySettingsItem.ToggleContent {
        return AmitySettingsItem.ToggleContent(
            title = R.string.amity_allow_comment,
            description = R.string.amity_allow_comment_description,
            isToggled = isChecked,
            isTitleBold = true,
            callback = fragment::toggleAllowCommentEvent
        )
    }
}