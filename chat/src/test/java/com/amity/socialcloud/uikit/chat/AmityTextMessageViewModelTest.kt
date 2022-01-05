package com.amity.socialcloud.uikit.chat

import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityTextMessageViewModel
import org.junit.Assert
import org.junit.Test

class AmityTextMessageViewModelTest {

    @Test
    fun getter_setterTest() {
        val viewModel = AmityTextMessageViewModel()
        Assert.assertNull(viewModel.text.get())
        Assert.assertEquals(viewModel.senderFillColor.get(), R.color.amityColorPrimary)
        Assert.assertEquals(viewModel.receiverFillColor.get(), R.color.amityMessageBubbleInverse)

        viewModel.text.set("test")
        viewModel.senderFillColor.set(100)
        viewModel.receiverFillColor.set(101)

        Assert.assertEquals(viewModel.text.get(), "test")
        Assert.assertEquals(viewModel.senderFillColor.get(), 100)
        Assert.assertEquals(viewModel.receiverFillColor.get(), 101)
    }
}