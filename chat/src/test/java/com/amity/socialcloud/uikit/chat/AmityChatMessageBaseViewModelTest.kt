package com.amity.socialcloud.uikit.chat

import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityChatMessageBaseViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Assert
import org.junit.Test

class AmityChatMessageBaseViewModelTest {

    @Test
    fun getterTest() {
        val baseViewModel = AmityChatMessageBaseViewModel()
        Assert.assertFalse(baseViewModel.isSelf.get())
        Assert.assertEquals(baseViewModel.sender.get(), "")
        Assert.assertEquals(baseViewModel.msgTime.get(), "")
        Assert.assertEquals(baseViewModel.msgDate.get(), "")
        Assert.assertFalse(baseViewModel.isDateVisible.get())
        Assert.assertTrue(baseViewModel.isSenderVisible.get())
        Assert.assertNull(baseViewModel.amityMessage)
        Assert.assertFalse(baseViewModel.isDeleted.get())
        Assert.assertEquals(baseViewModel.editedAt.get(), "")
        Assert.assertFalse(baseViewModel.isEdited.get())
        Assert.assertEquals(baseViewModel.dateFillColor.get(), R.color.amityColorBase)
        Assert.assertFalse(baseViewModel.isFailed.get())
    }

    @Test
    fun when_deleteMessage_expect_completableAsSuccess() {
        val baseViewModel = AmityChatMessageBaseViewModel()
        val mockMessage: AmityMessage = mockk()
        every { mockMessage.delete() } returns Completable.complete()
        baseViewModel.amityMessage = mockMessage
        var res = baseViewModel.deleteMessage()?.test()
        res?.assertComplete()

        baseViewModel.amityMessage = null
        res = baseViewModel.deleteMessage()?.test()
        Assert.assertNull(res)
    }

    @Test
    fun when_deleteMessage_expect_completableAsError() {
        val baseViewModel = AmityChatMessageBaseViewModel()
        val mockMessage: AmityMessage = mockk()
        every { mockMessage.delete() } returns Completable.error(Exception("error_message"))
        baseViewModel.amityMessage = mockMessage
        val res = baseViewModel.deleteMessage()?.test()
        res?.assertErrorMessage("error_message")
    }

}