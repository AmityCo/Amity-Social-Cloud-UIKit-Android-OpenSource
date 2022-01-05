package com.amity.socialcloud.uikit.chat

import android.net.Uri
import android.util.Log
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.sdk.core.file.AmityFileRepository
import com.amity.socialcloud.sdk.core.file.AmityUploadInfo
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityAudioMsgViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AmityAudioMsgViewModelTest {

    @get:Rule
    val schedulers = AmityRxImmediateSchedulerRule()

    @Test
    fun initTest() {
        val viewModel = AmityAudioMsgViewModel()
        assertEquals(viewModel.audioUrl.get(), "")
        assertEquals(viewModel.audioUri, Uri.EMPTY)
        assertFalse(viewModel.isPlaying.get())
        assertEquals(viewModel.duration.get(), "0:00")
        assertEquals(viewModel.progressMax.get(), 0)
        assertEquals(viewModel.senderFillColor.get(), R.color.amityMessageBubble)
        assertEquals(viewModel.receiverFillColor.get(), R.color.amityMessageBubbleInverse)
        assertFalse(viewModel.uploading.get())
        assertEquals(viewModel.uploadProgress.get(), 0)
        assertFalse(viewModel.buffering.get())
    }

    @Test
    fun uploadingStateTest() {
        val viewModel = AmityAudioMsgViewModel()
        assertFalse(viewModel.uploading.get())
        viewModel.uploadProgress.set(50)
        assertTrue(viewModel.uploading.get())
        viewModel.uploadProgress.set(100)
        assertFalse(viewModel.uploading.get())
        viewModel.uploadProgress.set(null)
        assertTrue(viewModel.uploading.get())
    }

    @Test
    fun playButtonClickTest() {
        val viewModel = AmityAudioMsgViewModel()
        var playButtonClicked = false
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.AUDIO_PLAYER_PLAY_CLICKED -> playButtonClicked = true
                else -> {
                }
            }
        }
        viewModel.buffering.set(true)
        viewModel.playButtonClicked()
        assertFalse(playButtonClicked)

        viewModel.buffering.set(false)
        viewModel.playButtonClicked()
        assertTrue(playButtonClicked)
    }

    @Test
    fun getUploadProgress_when_state_is_syncing_or_syncedTest() {
        val ekoMessage: AmityMessage = mockk()
        val messageData: AmityMessage.Data.AUDIO = mockk(relaxed = true)
        every { ekoMessage.isDeleted() } returns false
        every { ekoMessage.getState() } returns AmityMessage.State.SYNCED
        every { ekoMessage.getData() } answers {
            messageData
        }
        every { messageData.getAudio()?.getUrl() } returns "synced_url"

        val viewModel = AmityAudioMsgViewModel()
        viewModel.getUploadProgress(ekoMessage)
        assertFalse(viewModel.uploading.get())
        assertEquals(viewModel.duration.get(), "0:00")
        assertEquals(viewModel.audioUrl.get(), "synced_url")

        every { messageData.getAudio()?.getUrl() } returns null
        viewModel.getUploadProgress(ekoMessage)
        assertNull(viewModel.audioUrl.get())

        every { ekoMessage.getState() } returns AmityMessage.State.SYNCING
        every { messageData.getAudio()?.getUrl() } returns "syncing_url"
        viewModel.getUploadProgress(ekoMessage)
        assertEquals(viewModel.audioUrl.get(), "syncing_url")

        every { ekoMessage.getState() } returns AmityMessage.State.CREATED
        viewModel.getUploadProgress(ekoMessage)
        assertEquals(viewModel.audioUrl.get(), "syncing_url")
    }

    @Test
    fun getUploadProgress_when_state_is_uploading_or_failedTest() {
        val ekoMessage: AmityMessage = mockk()
        every { ekoMessage.getMessageId() } returns "messageID"
        every { ekoMessage.getState() } returns AmityMessage.State.UPLOADING
        mockkStatic(AmityCoreClient::class)
        val fileRepository: AmityFileRepository = mockk()
        val ekoUploadInfo: AmityUploadInfo = mockk()
        every { AmityCoreClient.newFileRepository() } returns fileRepository
        every { fileRepository.getUploadInfo(any()) } returns Flowable.just(ekoUploadInfo)
        every { ekoUploadInfo.getProgressPercentage() } returns 50

        every { ekoMessage.isDeleted() } returns true
        val viewModel = AmityAudioMsgViewModel()
        viewModel.getUploadProgress(ekoMessage)
        assertEquals(viewModel.uploadProgress.get(), 0)

        every { ekoMessage.isDeleted() } returns false

        viewModel.getUploadProgress(ekoMessage)
        assertEquals(viewModel.uploadProgress.get(), 50)
        assertTrue(viewModel.uploading.get())

        mockkStatic(Log::class)
        var ret = 0
        every { Log.e(any(), any()) } answers {
            ret = 6
            ret
        }
        every { ekoMessage.getState() } returns AmityMessage.State.FAILED
        every { fileRepository.getUploadInfo(any()) } returns Flowable.error(Exception("test"))
        viewModel.getUploadProgress(ekoMessage)
        assertFalse(viewModel.uploading.get())
        assertEquals(ret, 6)

    }
}