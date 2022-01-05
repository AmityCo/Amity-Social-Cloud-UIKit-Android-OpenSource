package com.amity.socialcloud.uikit.sample

import androidx.databinding.ObservableField
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityChatMessageBaseViewModel

class AmityMyTextMsgViewModel : AmityChatMessageBaseViewModel() {

    val text = ObservableField<String>("")
}