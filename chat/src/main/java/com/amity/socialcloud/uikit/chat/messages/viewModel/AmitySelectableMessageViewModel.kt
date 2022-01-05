package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.databinding.ObservableBoolean
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier

open class AmitySelectableMessageViewModel : AmityChatMessageBaseViewModel() {

    val inSelectionMode = ObservableBoolean(false)

    fun onLongPress(position: Int): Boolean {
        triggerEvent(AmityEventIdentifier.MESSAGE_LONG_PRESS, position)
        return true
    }

    fun onEditClick() {
        triggerEvent(AmityEventIdentifier.EDIT_MESSAGE)
    }

    fun onDeleteClick() {
        triggerEvent(AmityEventIdentifier.DELETE_MESSAGE)
    }

    fun onReportClick() {
        if (isFlaggedByMe()) {
            triggerEvent(AmityEventIdentifier.UNREPORT_MESSAGE)
        } else {
            triggerEvent(AmityEventIdentifier.REPORT_MESSAGE)
        }
    }

    fun isFlaggedByMe() : Boolean {
        return amityMessage?.isFlaggedByMe() == true
    }

    fun onFailedMsgClick() {
        triggerEvent(AmityEventIdentifier.FAILED_MESSAGE)
    }
}