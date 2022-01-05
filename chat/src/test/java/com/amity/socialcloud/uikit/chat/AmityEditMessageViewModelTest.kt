package com.amity.socialcloud.uikit.chat

import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.AmityMessageRepository
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.editMessage.AmityEditMessageViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Test

class AmityEditMessageViewModelTest {

    @Test
    fun initTest() {
        val editMessageViewModel = AmityEditMessageViewModel()
        Assert.assertEquals(editMessageViewModel.messageLength, 0)
        Assert.assertEquals(editMessageViewModel.message.get(), null)
        Assert.assertFalse(editMessageViewModel.isSaveEnabled.get())
        Assert.assertEquals(editMessageViewModel.saveColor.get(), null)
    }

    @Test
    fun setterTest() {
        val editMessageViewModel = AmityEditMessageViewModel()
        editMessageViewModel.saveColor.set(1)
        Assert.assertEquals(editMessageViewModel.saveColor.get(), 1)

        val textData: AmityMessage.Data.TEXT = mockk()
        every { textData.getText() } returns "test"
        editMessageViewModel.textData.set(textData)
        Assert.assertEquals(editMessageViewModel.textData.get(), textData)
        Assert.assertEquals(editMessageViewModel.textData.get()?.getText(), "test")
    }

    @Test
    fun when_messageChange_expect_isSaveEnabled_change_value() {
        val editMessageViewModel = AmityEditMessageViewModel()
        editMessageViewModel.observeMessageChange()
        editMessageViewModel.message.set("test")
        Assert.assertTrue(editMessageViewModel.isSaveEnabled.get())

        editMessageViewModel.message.set("")
        Assert.assertFalse(editMessageViewModel.isSaveEnabled.get())
    }

    @Test
    fun when_getMessage_expect_flowable_Message() {
        val ekoMessage: AmityMessage = mockk()
        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        every { messageRepository.getMessage(any()) } returns Flowable.just(ekoMessage)
        every { ekoMessage.getUserId() } returns "test"
        val editMessageViewModel = AmityEditMessageViewModel()
        val res = editMessageViewModel.getMessage("test").blockingFirst()

        Assert.assertEquals(res.getUserId(), "test")
    }

    @Test
    fun when_saveMessage_expect_completableAsSuccess() {
        val editMessageViewModel = AmityEditMessageViewModel()
        val mockData: AmityMessage.Data.TEXT = mockk()
        every { mockData.edit().text(any()).build().apply() } returns Completable.complete()
        editMessageViewModel.textData.set(mockData)
        editMessageViewModel.message.set("test")
        Assert.assertEquals(editMessageViewModel.textData.get(), mockData)
        val res = editMessageViewModel.saveMessage().test()
        res.assertComplete()
    }

    @Test
    fun when_saveMessage_expect_completableAsFailed() {
        val editMessageViewModel = AmityEditMessageViewModel()
        val mockData: AmityMessage.Data.TEXT = mockk()
        every {
            mockData.edit().text(any()).build().apply()
        } returns Completable.error(Exception("test Exception"))
        editMessageViewModel.textData.set(mockData)
        editMessageViewModel.message.set("test")
        Assert.assertEquals(editMessageViewModel.textData.get(), mockData)
        val res = editMessageViewModel.saveMessage().test()
        res.assertErrorMessage("test Exception")
    }
}