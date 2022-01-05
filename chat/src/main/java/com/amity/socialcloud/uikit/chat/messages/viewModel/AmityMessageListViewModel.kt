package com.amity.socialcloud.uikit.chat.messages.viewModel

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.AmityMessageRepository
import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.chat.channel.AmityChannelMember
import com.amity.socialcloud.sdk.chat.channel.AmityChannelRepository
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.sdk.chat.message.AmityMessageLoader
import com.amity.socialcloud.sdk.core.AmityConnectionState
import com.amity.socialcloud.uikit.common.components.AmityChatComposeBarClickListener
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime

class AmityMessageListViewModel : AmityChatMessageBaseViewModel() {

    val text = ObservableField<String>()
    val title = ObservableField<String>()
    val avatarUrl = ObservableField<String>()
    var channelID: String = ""
        set(value) {
            field = value
            messageLoader = AmityChatClient.newMessageRepository()
                .getMessages(value)
                .parentId(null)
                .build()
                .loader()
        }
    var isRVScrolling = false
    val isScrollable = ObservableBoolean(false)
    val stickyDate = ObservableField<String>("")
    val showComposeBar = ObservableBoolean(false)
    val keyboardHeight = ObservableInt(0)
    val isVoiceMsgUi = ObservableBoolean(false)
    val isRecording = ObservableBoolean(false)
    var hasScrolled = false

    lateinit var messageLoader: AmityMessageLoader

    fun toggleRecordingView() {
        isVoiceMsgUi.set(!isVoiceMsgUi.get())
        if (isVoiceMsgUi.get()) {
            triggerEvent(AmityEventIdentifier.SHOW_AUDIO_RECORD_UI)
        }
    }

    fun getChannelType(): Flowable<AmityChannel> {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        return channelRepository.getChannel(channelID)
    }

    fun getDisplayName(): Flowable<PagedList<AmityChannelMember>> {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        return channelRepository.membership(channelID).getMembers().build().query()
    }

    fun joinChannel(): Completable {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        return channelRepository.joinChannel(channelID).ignoreElement()
    }

    fun startReading() {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        channelRepository.membership(channelID).startReading()
    }

    fun stopReading() {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        channelRepository.membership(channelID).stopReading()
    }

    fun getAllMessages(): Flowable<List<AmityMessage>> {
        return messageLoader.getResult()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLatestMessage(): Flowable<AmityMessage> {
        return AmityChatClient.newChannelRepository()
            .getChannel(channelID)
            .firstElement()
            .flatMapPublisher {
                it.latestMessage()
                    .isDeleted(false)
                    .build()
                    .query()
            }
    }

    fun sendMessage() {
        if (!isVoiceMsgUi.get()) {
            val messageRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()
            addDisposable(
                messageRepository.createMessage(channelID).with()
                    .text(text.get()!!.trim())
                    .build().send().observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            triggerEvent(AmityEventIdentifier.MSG_SEND_SUCCESS)
                        }

                        override fun onError(e: Throwable) {
                            triggerEvent(AmityEventIdentifier.MSG_SEND_ERROR)
                        }
                    })
            )
            text.set("")
        }
    }

    fun sendImageMessage(imageUri: Uri): Completable {
        val messageRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()
        return messageRepository.createMessage(channelID).with()
            .image(imageUri).build().send()
    }

    fun sendAudioMessage(audioFileUri: Uri): Completable {
        val messageRepository: AmityMessageRepository = AmityChatClient.newMessageRepository()
        return messageRepository.createMessage(channelID).with()
            .audio(audioFileUri).build().send()
    }

    fun toggleComposeBar() {
        triggerEvent(AmityEventIdentifier.TOGGLE_CHAT_COMPOSE_BAR)
    }

    val composeBarClickListener = object : AmityChatComposeBarClickListener {
        override fun onCameraClicked() {
            triggerEvent(AmityEventIdentifier.CAMERA_CLICKED)
        }

        override fun onAlbumClicked() {
            triggerEvent(AmityEventIdentifier.PICK_IMAGE)
        }

        override fun onFileClicked() {
        }

        override fun onLocationCLicked() {
        }
    }

    fun observeRefreshStatus(onRefreshNeeded: () -> Unit): Completable {
        return Flowable.combineLatest(
            reconnectedStateFlowable.map { DateTime.now() },
            disconnectedStateFlowable.map { DateTime.now() },
            { reconnectedTime, disconnectedTime ->
                disconnectedTime.plusSeconds(RECONNECTION_DELAY_SECONDS).isBefore(reconnectedTime)
            })
            .filter { it }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onRefreshNeeded.invoke() }
            .ignoreElements()
    }

    fun observeConnectionStatus(
        onDisconnected: () -> Unit,
        onReconnected: () -> Unit
    ): Completable {
        return connectionStateFlowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (it == AmityConnectionState.CONNECTED) {
                    onReconnected.invoke()
                } else if (it == AmityConnectionState.DISCONNECTED) {
                    onDisconnected.invoke()
                }
            }
            .ignoreElements()
    }

    fun onRVScrollStateChanged(rv: RecyclerView, newState: Int) {
        isScrollable.set(rv.computeVerticalScrollRange() > rv.height)
        isRVScrolling = if (isScrollable.get()) {
            newState == RecyclerView.SCROLL_STATE_DRAGGING ||
                    newState == RecyclerView.SCROLL_STATE_SETTLING
        } else {
            false
        }
        if (isRVScrolling) {
            hasScrolled = true
        }
    }

    private val reconnectedStateFlowable = AmityCoreClient.getConnectionState()
        .filter { it == AmityConnectionState.CONNECTED }


    private val disconnectedStateFlowable = AmityCoreClient.getConnectionState()
        .filter { it == AmityConnectionState.DISCONNECTED }

    private val connectionStateFlowable = AmityCoreClient.getConnectionState()
}

private const val RECONNECTION_DELAY_SECONDS = 3