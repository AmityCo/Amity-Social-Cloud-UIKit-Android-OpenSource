package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.core.Completable

open class AmityChatMessageBaseViewModel : AmityBaseViewModel() {

    val isSelf = ObservableBoolean(false)
    val sender = ObservableField("")
    val msgTime = ObservableField("")
    val msgDate = ObservableField("")
    val isDateVisible = ObservableBoolean(false)
    val isSenderVisible = ObservableBoolean(false)
    var amityMessage: AmityMessage? = null
    val isDeleted = ObservableBoolean(false)
    val editedAt = ObservableField("")
    val isEdited = ObservableBoolean(false)
    val dateFillColor = ObservableField(R.color.amityColorBase)
    val isFailed = ObservableBoolean(false)

    fun deleteMessage(): Completable? {
        return amityMessage?.let {
            AmityChatClient.newMessageRepository().softDeleteMessage(it.getMessageId())
        }
    }
}