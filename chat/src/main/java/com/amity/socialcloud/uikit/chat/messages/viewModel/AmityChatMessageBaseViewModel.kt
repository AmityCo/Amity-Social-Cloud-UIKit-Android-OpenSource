package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Completable

open class AmityChatMessageBaseViewModel : AmityBaseViewModel() {

    val isSelf = ObservableBoolean(false)
    val sender = ObservableField("")
    val msgTime = ObservableField("")
    val msgDate = ObservableField("")
    val isDateVisible = ObservableBoolean(false)
    val isSenderVisible = ObservableBoolean(true)
    var amityMessage: AmityMessage? = null
    val isDeleted = ObservableBoolean(false)
    val editedAt = ObservableField("")
    val isEdited = ObservableBoolean(false)
    val dateFillColor = ObservableField<Int>(R.color.amityColorBase)
    val isFailed = ObservableBoolean(false)

    fun deleteMessage(): Completable? {
        return amityMessage?.delete()
    }
}