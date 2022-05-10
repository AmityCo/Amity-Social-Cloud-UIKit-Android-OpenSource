package  com.amity.socialcloud.uikit.common.memberpicker.listener

import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem

interface AmityAddedMemberClickListener {

    fun onMemberRemoved(itemAmity: AmitySelectMemberItem)

    fun onAddButtonClicked()

    fun onMemberCountClicked()
}