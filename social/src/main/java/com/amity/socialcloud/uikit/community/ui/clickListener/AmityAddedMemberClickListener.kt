package com.amity.socialcloud.uikit.community.ui.clickListener

import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem

interface AmityAddedMemberClickListener {

    fun onMemberRemoved(itemAmity: AmitySelectMemberItem)

    fun onAddButtonClicked()

    fun onMemberCountClicked()
}