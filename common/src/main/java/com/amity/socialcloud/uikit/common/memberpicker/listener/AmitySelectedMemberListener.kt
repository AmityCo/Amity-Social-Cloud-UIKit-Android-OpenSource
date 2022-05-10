package  com.amity.socialcloud.uikit.common.memberpicker.listener

import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem

interface AmitySelectedMemberListener {

    fun onMemberRemoved(memberAmity: AmitySelectMemberItem)
}