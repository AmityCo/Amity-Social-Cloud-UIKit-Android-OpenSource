package  com.amity.socialcloud.uikit.common.memberpicker.listener

import com.amity.socialcloud.sdk.core.user.AmityUser

interface AmitySelectMemberListener {

    fun onMemberClicked(member: AmityUser, position: Int)
}