package com.amity.socialcloud.uikit.chat.messages.viewModel

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.service.AmityFileService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityAudioMsgViewModel : AmitySelectableMessageViewModel() {

    val audioUrl = ObservableField("")
    var audioUri: Uri? = Uri.EMPTY
    val isPlaying = ObservableBoolean(false)
    val duration = ObservableField("0:00")
    val progressMax = ObservableInt(0)
    val senderFillColor = ObservableField(R.color.amityMessageBubble)
    val receiverFillColor = ObservableField(R.color.amityMessageBubbleInverse)
    val uploading = ObservableBoolean(false)
    val uploadProgress = ObservableField(0)
    val buffering = ObservableBoolean(false)

    init {
        uploadProgress.addOnPropertyChanged {
            if (uploadProgress.get() == 100) {
                uploading.set(false)
            } else {
                uploading.set(true)
            }
        }

        /**
         * Not using now will be used when we'll start downloading Audio Files
         * @author sumitlakra
         * @date 12/01/2020
         */
//        audioUrl.addOnPropertyChanged {
//            if (audioUrl.get() != null) {
//                triggerEvent(EventIdentifier.SET_AUDIO_FILE_PROPERTIES)
//            }
//        }
    }

    fun playButtonClicked() {
        if (!buffering.get()) {
            triggerEvent(AmityEventIdentifier.AUDIO_PLAYER_PLAY_CLICKED)
        }
    }

    fun getUploadProgress(ekoMessage: AmityMessage) {
        if (!ekoMessage.isDeleted()) {
            when (ekoMessage.getState()) {
                AmityMessage.State.SYNCED, AmityMessage.State.SYNCING -> {
                    uploading.set(false)
                    duration.set("0:00")
                    val audioMsg = ekoMessage.getData() as AmityMessage.Data.AUDIO
                    audioUrl.set(audioMsg.getAudio()?.getUrl())
                }
                AmityMessage.State.UPLOADING, AmityMessage.State.FAILED -> {
                    uploading.set(ekoMessage.getState() == AmityMessage.State.UPLOADING)
                    addDisposable(AmityFileService().getUploadInfo(ekoMessage.getMessageId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { uploadInfo ->
                            uploadProgress.set(uploadInfo.getProgressPercentage())
                        }.doOnError {
                            Log.e(
                                "AmityAudioMsgViewModel",
                                "Audio upload error ${it.localizedMessage}"
                            )
                        }.subscribe()
                    )
                }
                else -> {
                }

            }
        }

    }
}