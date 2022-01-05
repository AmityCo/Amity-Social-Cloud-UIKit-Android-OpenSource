package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityItemTextMessageReceiverBinding
import com.amity.socialcloud.uikit.chat.databinding.AmityPopupMsgReportBinding
import com.amity.socialcloud.uikit.chat.messages.popUp.AmityPopUp
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityTextMessageViewModel
import com.amity.socialcloud.uikit.common.components.AmityLongPressListener
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier

class AmityTextMsgReceiverViewHolder(
    itemView: View,
    private val itemViewModel: AmityTextMessageViewModel,
    context: Context
) : AmitySelectableMessageViewHolder(itemView, itemViewModel, context), AmityLongPressListener {

    private val binding: AmityItemTextMessageReceiverBinding? = DataBindingUtil.bind(itemView)
    private var popUp: AmityPopUp? = null

    init {
        binding?.vmTextMessage = itemViewModel
        binding?.lonPressListener = this
        addViewModelListener()
    }

    private fun addViewModelListener() {
        itemViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.DISMISS_POPUP -> popUp?.dismiss()
                else -> {
                }
            }
        }
    }

    override fun setMessageData(item: AmityMessage) {
        val data = item.getData() as? AmityMessage.Data.TEXT
        itemViewModel.text.set(data?.getText())
    }

    override fun showPopUp() {
        popUp = AmityPopUp()
        val anchor: View = itemView.findViewById(R.id.tvMessageIncoming)
        val inflater: LayoutInflater = LayoutInflater.from(anchor.context)
        val binding: AmityPopupMsgReportBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_popup_msg_report, null, true
        )
        binding.viewModel = itemViewModel
        if (itemViewModel.amityMessage?.isFlaggedByMe() == true) {
            binding.reportText.setText(R.string.amity_unreport)
        } else {
            binding.reportText.setText(R.string.amity_report)
        }
        popUp?.showPopUp(binding.root, anchor, itemViewModel, AmityPopUp.PopUpGravity.START)
    }

    override fun onLongPress() {
        itemViewModel.onLongPress(absoluteAdapterPosition)
    }
}