package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.view.View
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.databinding.AmityItemUnknownMessageBinding
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityUnknownMsgViewModel

class AmityUnknownMessageViewHolder(
    itemView: View, private val itemViewModel: AmityUnknownMsgViewModel
) : AmityChatMessageBaseViewHolder(itemView, itemViewModel) {

    private val mBinding: AmityItemUnknownMessageBinding? = DataBindingUtil.bind(itemView)

    override fun setMessage(message: AmityMessage) {

    }
}