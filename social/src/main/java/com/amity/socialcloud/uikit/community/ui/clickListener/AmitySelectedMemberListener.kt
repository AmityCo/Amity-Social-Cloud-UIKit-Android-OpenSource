package com.amity.socialcloud.uikit.community.ui.clickListener

import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem

interface AmitySelectedMemberListener {

    fun onMemberRemoved(memberAmity: AmitySelectMemberItem)
}