package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.databinding.ObservableField
import com.amity.socialcloud.uikit.chat.R

class AmityTextMessageViewModel : AmitySelectableMessageViewModel() {

    val text = ObservableField<String>()
    val senderFillColor = ObservableField<Int>(R.color.amityColorPrimary)
    val receiverFillColor = ObservableField<Int>(R.color.amityMessageBubbleInverse)
}