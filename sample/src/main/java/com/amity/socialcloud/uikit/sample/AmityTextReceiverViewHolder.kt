package com.amity.socialcloud.uikit.sample

import android.view.View
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.messages.viewHolder.AmityChatMessageBaseViewHolder
import com.amity.socialcloud.uikit.sample.databinding.AmityItemTextReceiverBinding

class AmityTextReceiverViewHolder(
    itemView: View,
    private val itemViewModel: AmityMyTextMsgViewModel
) : AmityChatMessageBaseViewHolder(itemView, itemViewModel) {

    private val binding: AmityItemTextReceiverBinding? = DataBindingUtil.bind(itemView)

    init {
        binding?.viewModel = itemViewModel
    }

    override fun setMessage(message: AmityMessage) {
        val data = message.getData() as AmityMessage.Data.TEXT
        val text = data.getText()
        /**
         * Data binding can be used to set the views
         */
        itemViewModel.text.set(text)

        /**
         * Alternatively individual views can be set
         */
        binding?.tvMsgReceiver?.text = text
        binding?.tvTime?.text = itemViewModel.msgTime.get()
    }
}