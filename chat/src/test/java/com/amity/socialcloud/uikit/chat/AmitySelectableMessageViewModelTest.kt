package com.amity.socialcloud.uikit.chat

import com.amity.socialcloud.uikit.chat.messages.viewModel.AmitySelectableMessageViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import org.junit.Assert
import org.junit.Test

class AmitySelectableMessageViewModelTest {

    @Test
    fun getter_setterTest() {
        val viewModel = AmitySelectableMessageViewModel()
        Assert.assertFalse(viewModel.inSelectionMode.get())

        viewModel.inSelectionMode.set(true)
        Assert.assertTrue(viewModel.inSelectionMode.get())
    }

    @Test
    fun clickEventTest() {
        val viewModel = AmitySelectableMessageViewModel()
        var longPress = false
        var editClick = false
        var deleteClick = false
        var reportClick = false
        var failedClick = true

        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.MESSAGE_LONG_PRESS -> longPress = true
                AmityEventIdentifier.EDIT_MESSAGE -> editClick = true
                AmityEventIdentifier.DELETE_MESSAGE -> deleteClick = true
                AmityEventIdentifier.REPORT_MESSAGE -> reportClick = true
                AmityEventIdentifier.FAILED_MESSAGE -> failedClick = true
                else -> {
                }
            }
        }

        viewModel.onLongPress()
        Assert.assertTrue(longPress)

        viewModel.onEditClick()
        Assert.assertTrue(editClick)

        viewModel.onDeleteClick()
        Assert.assertTrue(deleteClick)

        viewModel.onReportClick()
        Assert.assertTrue(reportClick)

        viewModel.onFailedMsgClick()
        Assert.assertTrue(failedClick)
    }
}