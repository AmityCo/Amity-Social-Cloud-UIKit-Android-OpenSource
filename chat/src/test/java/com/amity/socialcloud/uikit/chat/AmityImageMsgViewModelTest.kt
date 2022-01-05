package com.amity.socialcloud.uikit.chat

import android.util.Log
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.sdk.core.file.AmityFileRepository
import com.amity.socialcloud.sdk.core.file.AmityUploadInfo
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityImageMsgViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AmityImageMsgViewModelTest {

    @get:Rule
    val schedulers = AmityRxImmediateSchedulerRule()

    @Test
    fun getterSetterTest() {
        val viewModel = AmityImageMsgViewModel()
        assertEquals(viewModel.imageUrl.get(), "")

        viewModel.imageUrl.set("test_url")
        assertEquals(viewModel.imageUrl.get(), "test_url")
        assertFalse(viewModel.uploading.get())
        assertEquals(viewModel.uploadProgress.get(), 0)
    }

    @Test
    fun when_upload_progress_changes_uploading_state_test() {
        val viewModel = AmityImageMsgViewModel()
        assertFalse(viewModel.uploading.get())
        viewModel.uploadProgress.set(50)
        assertTrue(viewModel.uploading.get())
        viewModel.uploadProgress.set(100)
        assertFalse(viewModel.uploading.get())
    }

    @Test
    fun getUploadProgress_when_state_is_syncingTest() {
        val ekoMessage: AmityMessage = mockk()
        val imageData: AmityMessage.Data.IMAGE = mockk(relaxed = true)
        every { ekoMessage.getState() } returns AmityMessage.State.SYNCED
        every { ekoMessage.getData() } answers {
            imageData
        }
        every { imageData.getImage()?.getFilePath() } returns null
        every { imageData.getImage()?.getUrl(any()) } returns "test_url"


        val viewModel = AmityImageMsgViewModel()
        viewModel.getImageUploadProgress(ekoMessage)
        assertEquals(viewModel.imageUrl.get(), "test_url")
    }

    @Test
    fun getUploadProgress_when_state_is_uploadingTest() {
        val ekoMessage: AmityMessage = mockk()
        val imageData: AmityMessage.Data.IMAGE = mockk(relaxed = true)
        every { ekoMessage.getState() } returns AmityMessage.State.UPLOADING
        every { ekoMessage.getMessageId() } returns "messageId"
        every { ekoMessage.getData() } answers {
            imageData
        }
        every { imageData.getImage()?.getFilePath() } returns null
        mockkStatic(AmityCoreClient::class)
        val fileRepository: AmityFileRepository = mockk()
        val ekoUploadInfo: AmityUploadInfo = mockk()
        every { AmityCoreClient.newFileRepository() } returns fileRepository
        every { ekoUploadInfo.getProgressPercentage() } returns 50
        every { fileRepository.getUploadInfo(any()) } returns Flowable.just(ekoUploadInfo)

        val viewModel = AmityImageMsgViewModel()
        viewModel.getImageUploadProgress(ekoMessage)
        assertTrue(viewModel.uploading.get())
        assertEquals(viewModel.uploadProgress.get(), 50)

        every { fileRepository.getUploadInfo(any()) } returns Flowable.error(Exception("test"))
        mockkStatic(Log::class)
        var ret = 0
        every { Log.e(any(), any()) } answers {
            ret = 6
            ret
        }

        viewModel.getImageUploadProgress(ekoMessage)
        assertEquals(ret, 6)
    }
}