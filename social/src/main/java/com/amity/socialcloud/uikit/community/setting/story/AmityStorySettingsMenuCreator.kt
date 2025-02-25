package com.amity.socialcloud.uikit.community.setting.story

import com.amity.socialcloud.uikit.community.setting.AmitySettingsItem
import io.reactivex.rxjava3.core.Flowable

interface AmityStorySettingsMenuCreator {
    fun createAllowCommentMenu(isChecked: Flowable<Boolean>): AmitySettingsItem.ToggleContent
}