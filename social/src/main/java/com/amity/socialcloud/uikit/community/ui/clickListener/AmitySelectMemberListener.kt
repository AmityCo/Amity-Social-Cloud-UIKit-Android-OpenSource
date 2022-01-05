package com.amity.socialcloud.uikit.community.ui.clickListener

import com.amity.socialcloud.sdk.core.user.AmityUser

interface AmitySelectMemberListener {

    fun onMemberClicked(member: AmityUser, position: Int)
}