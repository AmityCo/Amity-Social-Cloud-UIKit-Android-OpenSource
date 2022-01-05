package com.amity.socialcloud.uikit.chat.editMessage

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable
import io.reactivex.Flowable

class AmityEditMessageViewModel : AmityBaseViewModel() {

    val message = ObservableField<String>()
    var messageLength = 0
    val isSaveEnabled = ObservableBoolean(false)
    val textData = ObservableField<AmityMessage.Data.TEXT>()
    val saveColor = ObservableField<Int>()

    fun getMessage(messageId: String): Flowable<AmityMessage> {
        val messageRepository = AmityChatClient.newMessageRepository()
        return messageRepository.getMessage(messageId)
    }

    fun observeMessageChange() {
        message.addOnPropertyChanged {
            if (message.get()?.length == 0) {
                isSaveEnabled.set(false)
            } else if (message.get()?.length != messageLength) {
                isSaveEnabled.set(true)
            }

        }
    }

    fun saveMessage(): Completable {
        return textData.get()!!.edit()
            .text(message.get()?.trim()!!)
            .build()
            .apply()
    }

}