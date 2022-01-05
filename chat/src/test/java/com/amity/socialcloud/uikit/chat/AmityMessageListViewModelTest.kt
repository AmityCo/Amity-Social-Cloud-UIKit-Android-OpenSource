package com.amity.socialcloud.uikit.chat

import android.net.Uri
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.AmityMessageRepository
import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.chat.channel.AmityChannelMember
import com.amity.socialcloud.sdk.chat.channel.AmityChannelRepository
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityMessageListViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Test

class AmityMessageListViewModelTest {

    @Test
    fun initTest() {
        val viewModel = AmityMessageListViewModel()
        viewModel.title.set("title")
        Assert.assertEquals(viewModel.title.get(), "title")
        viewModel.channelID = "channelId"
        Assert.assertEquals(viewModel.channelID, "channelId")
        Assert.assertFalse(viewModel.isScrollable.get())
        Assert.assertFalse(viewModel.showComposeBar.get())
        Assert.assertEquals(viewModel.stickyDate.get(), "")
        Assert.assertEquals(viewModel.keyboardHeight.get(), 0)
        Assert.assertFalse(viewModel.isRecording.get())

    }

    @Test
    fun when_joinChannel_Expect_Success() {
        mockkStatic(AmityChatClient::class)
        val channelRepository: AmityChannelRepository = mockk()
        every { AmityChatClient.newChannelRepository() } returns channelRepository
        every {
            channelRepository.joinChannel(any()).ignoreElement()
        } returns Completable.complete()

        val viewModel = AmityMessageListViewModel()
        val res = viewModel.joinChannel().test()
        res.assertComplete()
    }

    @Test
    fun when_joinChannel_Expect_Failure() {
        mockkStatic(AmityChatClient::class)
        val channelRepository: AmityChannelRepository = mockk()
        every { AmityChatClient.newChannelRepository() } returns channelRepository
        every { channelRepository.joinChannel(any()).ignoreElement() } returns Completable.error(
            Exception("error_message")
        )

        val viewModel = AmityMessageListViewModel()
        val res = viewModel.joinChannel().test()
        res.assertErrorMessage("error_message")
    }

    @Test
    fun startReadingTest() {
        mockkStatic(AmityChatClient::class)
        val channelRepository: AmityChannelRepository = mockk()
        every { AmityChatClient.newChannelRepository() } returns channelRepository
        every { channelRepository.membership(any()).startReading() } returns Unit
        val viewModel = AmityMessageListViewModel()
        viewModel.startReading()
        verify(exactly = 1) { channelRepository.membership(any()).startReading() }

    }

    @Test
    fun stopReadingTest() {
        mockkStatic(AmityChatClient::class)
        val channelRepository: AmityChannelRepository = mockk()
        every { AmityChatClient.newChannelRepository() } returns channelRepository
        every { channelRepository.membership(any()).stopReading() } returns Unit
        val viewModel = AmityMessageListViewModel()
        viewModel.stopReading()
        verify(exactly = 1) { channelRepository.membership(any()).stopReading() }

    }

    @Test
    fun when_getAllMessages_expect_pagedListOfMessage() {
        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        val mockList: PagedList<AmityMessage> = mockk()
        every { mockList.size } returns 5
        every {
            messageRepository.getMessages(any()).parentId(any()).build().query()
        } returns Flowable.just(mockList)

        val viewModel = AmityMessageListViewModel()
        val res = viewModel.getAllMessages().blockingFirst()
        Assert.assertEquals(res.size, 5)
    }

    @Test
    fun when_sendMessage_expect_success() {
        var msgSuccess = false
        val viewModel = AmityMessageListViewModel()
        viewModel.text.set("Test text")
        Assert.assertEquals(viewModel.text.get(), "Test text")

        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        every {
            messageRepository.createMessage(any()).with().text(any()).build().send()
        } returns Completable.complete()

        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.MSG_SEND_SUCCESS -> msgSuccess = true
                else -> {
                }
            }
        }

        viewModel.sendMessage()
        Assert.assertTrue(msgSuccess)
        Assert.assertEquals(viewModel.text.get(), "")

        viewModel.text.set("Test text")
        viewModel.isVoiceMsgUi.set(true)
        viewModel.sendMessage()
        Assert.assertEquals(viewModel.text.get(), "Test text")

    }

    @Test
    fun when_sendMessage_expect_failure() {
        var msgError = false
        val viewModel = AmityMessageListViewModel()
        viewModel.text.set("Test text")
        Assert.assertEquals(viewModel.text.get(), "Test text")

        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        every {
            messageRepository.createMessage(any()).with().text(any()).build().send()
        } returns Completable.error(Exception("test_exception"))

        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.MSG_SEND_ERROR -> msgError = true
                else -> {
                }
            }
        }

        viewModel.sendMessage()
        Assert.assertTrue(msgError)
        Assert.assertEquals(viewModel.text.get(), "")
    }

    @Test
    fun when_sendImageMessage_expect_success() {
        val viewModel = AmityMessageListViewModel()
        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        every {
            messageRepository.createMessage(any()).with().image(any()).build().send()
        } returns Completable.complete()
        val imageUri: Uri = mockk()
        val res = viewModel.sendImageMessage(imageUri).test()
        res.assertComplete()
    }

    @Test
    fun when_sendImageMessage_expect_failure() {
        val viewModel = AmityMessageListViewModel()
        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        every {
            messageRepository.createMessage(any()).with().image(any()).build().send()
        } returns Completable.error(Exception("test_exception"))
        val imageUri: Uri = mockk()
        val res = viewModel.sendImageMessage(imageUri).test()
        res.assertErrorMessage("test_exception")

    }

    @Test
    fun composeBarClickListenerTest() {
        val viewModel = AmityMessageListViewModel()
        var leftIconClick = false
        var centerIconClick = false
        var rightIconClick = false

        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.CAMERA_CLICKED -> leftIconClick = true
                AmityEventIdentifier.PICK_IMAGE -> centerIconClick = true
                AmityEventIdentifier.PICK_FILE -> rightIconClick = true
                else -> {
                }
            }
        }

        viewModel.composeBarClickListener.onCameraClicked()
        Assert.assertTrue(leftIconClick)

        viewModel.composeBarClickListener.onAlbumClicked()
        Assert.assertTrue(centerIconClick)

        viewModel.composeBarClickListener.onFileClicked()
        viewModel.composeBarClickListener.onLocationCLicked()

    }

    @Test
    fun onRVScrollStateChangedTest() {
        val viewModel = AmityMessageListViewModel()
        val recyclerView: RecyclerView = mockk()

        viewModel.isRVScrolling = true
        every { recyclerView.computeVerticalScrollRange() } returns 10
        every { recyclerView.height } returns 5
        initialScrollStateTest(recyclerView, viewModel)

        testWhenScrollable(recyclerView, viewModel)

        every { recyclerView.computeVerticalScrollRange() } returns 5
        every { recyclerView.height } returns 10

        testWhenNotScrollable(recyclerView, viewModel)
    }

    private fun initialScrollStateTest(
        recyclerView: RecyclerView,
        viewModel: AmityMessageListViewModel
    ) {
        viewModel.onRVScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE)
        Assert.assertFalse(viewModel.isRVScrolling)
    }

    private fun testWhenScrollable(
        recyclerView: RecyclerView,
        viewModel: AmityMessageListViewModel
    ) {
        viewModel.isRVScrolling = false
        viewModel.onRVScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_DRAGGING)
        Assert.assertTrue(viewModel.isRVScrolling)

        viewModel.isRVScrolling = false
        viewModel.onRVScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_SETTLING)
        Assert.assertTrue(viewModel.isRVScrolling)

        viewModel.isRVScrolling = false
        viewModel.onRVScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE)
        Assert.assertFalse(viewModel.isRVScrolling)
    }

    private fun testWhenNotScrollable(
        recyclerView: RecyclerView,
        viewModel: AmityMessageListViewModel
    ) {
        viewModel.isRVScrolling = true
        viewModel.onRVScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE)
        Assert.assertFalse(viewModel.isRVScrolling)

        viewModel.isRVScrolling = true
        viewModel.onRVScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_SETTLING)
        Assert.assertFalse(viewModel.isRVScrolling)

        viewModel.isRVScrolling = true
        viewModel.onRVScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_DRAGGING)
        Assert.assertFalse(viewModel.isRVScrolling)
    }

    @Test
    fun toggleRecordingViewTest() {
        var toggleComposeBar = false
        val viewModel = AmityMessageListViewModel()
        Assert.assertFalse(viewModel.isVoiceMsgUi.get())
        var showAudioUi = false
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.SHOW_AUDIO_RECORD_UI -> showAudioUi = true
                AmityEventIdentifier.TOGGLE_CHAT_COMPOSE_BAR -> toggleComposeBar = true
                else -> {
                }
            }
        }

        viewModel.toggleRecordingView()
        Assert.assertTrue(viewModel.isVoiceMsgUi.get())
        Assert.assertTrue(showAudioUi)

        viewModel.toggleRecordingView()
        Assert.assertFalse(viewModel.isVoiceMsgUi.get())

        viewModel.toggleComposeBar()
        Assert.assertTrue(toggleComposeBar)
    }

    @Test
    fun when_getChannelType_expect_Flowable_Channel() {
        val ekoChannel: AmityChannel = mockk()
        mockkStatic(AmityChatClient::class)
        val channelRepository: AmityChannelRepository = mockk()
        every { AmityChatClient.newChannelRepository() } returns channelRepository
        every { channelRepository.getChannel(any()) } returns Flowable.just(ekoChannel)

        val viewModel = AmityMessageListViewModel()
        val res = viewModel.getChannelType().blockingFirst()
        Assert.assertEquals(res, ekoChannel)
    }

    @Test
    fun when_getDisplayName_expect_Flowable_PagedList_ChannelMembership() {
        val mockList: PagedList<AmityChannelMember> = mockk()
        mockkStatic(AmityChatClient::class)
        val channelRepository: AmityChannelRepository = mockk()
        every { AmityChatClient.newChannelRepository() } returns channelRepository
        every {
            channelRepository.membership(any()).getMembers().build().query()
        } returns Flowable.just(mockList)

        val viewModel = AmityMessageListViewModel()
        val res = viewModel.getDisplayName().blockingFirst()
        Assert.assertEquals(res, mockList)
    }

    @Test
    fun when_sendAudioMessage_expect_success() {
        val audioFileUri: Uri = mockk()
        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        every {
            messageRepository.createMessage(any()).with()
                .audio(any()).build().send()
        } returns Completable.complete()

        val viewModel = AmityMessageListViewModel()
        val res = viewModel.sendAudioMessage(audioFileUri).test()
        res.assertComplete()
    }

    @Test
    fun when_sendAudioMessage_expect_error() {
        val audioFileUri: Uri = mockk()
        mockkStatic(AmityChatClient::class)
        val messageRepository: AmityMessageRepository = mockk()
        every { AmityChatClient.newMessageRepository() } returns messageRepository
        every {
            messageRepository.createMessage(any()).with()
                .audio(any()).build().send()
        } returns Completable.error(Exception("test"))

        val viewModel = AmityMessageListViewModel()
        val res = viewModel.sendAudioMessage(audioFileUri).test()
        res.assertErrorMessage("test")
    }
}