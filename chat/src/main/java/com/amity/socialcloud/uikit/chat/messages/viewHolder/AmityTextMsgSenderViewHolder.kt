package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityItemTextMessageSenderBinding
import com.amity.socialcloud.uikit.chat.databinding.AmityPopupTextMsgSenderBinding
import com.amity.socialcloud.uikit.chat.editMessage.AmityEditMessageActivity
import com.amity.socialcloud.uikit.chat.messages.popUp.AmityPopUp
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityTextMessageViewModel
import com.amity.socialcloud.uikit.common.components.AmityLongPressListener
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier

class AmityTextMsgSenderViewHolder(
    itemView: View,
    private val itemViewModel: AmityTextMessageViewModel,
    context: Context
) : AmitySelectableMessageViewHolder(itemView, itemViewModel, context), AmityLongPressListener {

    private val binding: AmityItemTextMessageSenderBinding? = DataBindingUtil.bind(itemView)
    private var popUp: AmityPopUp? = null

    init {
        binding?.vmTextMessage = itemViewModel
        binding?.lonPressListener = this
        addViewModelListener()
    }

    private fun addViewModelListener() {
        itemViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.EDIT_MESSAGE -> {
                    navigateToEditMessage()
                    popUp?.dismiss()
                }
                AmityEventIdentifier.DISMISS_POPUP -> popUp?.dismiss()
                else -> {
                }
            }
        }
    }

    override fun setMessageData(item: AmityMessage) {
        val data = item.getData() as AmityMessage.Data.TEXT
        itemViewModel.text.set(data.getText())
    }

    override fun showPopUp() {
        popUp = AmityPopUp()
        val anchor: View = itemView.findViewById(R.id.tvMessageOutgoing)
        val inflater: LayoutInflater = LayoutInflater.from(anchor.context)
        val binding: AmityPopupTextMsgSenderBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_popup_text_msg_sender, null, true
        )
        binding.viewModel = itemViewModel

        if (!itemViewModel.isFailed.get()) {
            popUp?.showPopUp(binding.root, anchor, itemViewModel, AmityPopUp.PopUpGravity.END)
        }

    }

    private fun navigateToEditMessage() {
        val intent = AmityEditMessageActivity.newIntent(
            itemView.context,
            itemViewModel.amityMessage?.getMessageId() ?: ""
        )
        itemView.context.startActivity(intent)
    }

    override fun onLongPress() {
        itemViewModel.onLongPress(absoluteAdapterPosition)
    }
}