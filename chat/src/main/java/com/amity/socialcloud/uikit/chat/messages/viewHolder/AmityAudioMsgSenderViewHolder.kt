package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityItemAudioMessageSenderBinding
import com.amity.socialcloud.uikit.chat.databinding.AmityPopupMsgDeleteBinding
import com.amity.socialcloud.uikit.chat.messages.popUp.AmityPopUp
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityAudioMsgViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier

class AmityAudioMsgSenderViewHolder(
    itemView: View,
    private val itemViewModel: AmityAudioMsgViewModel,
    context: Context,
    audioPlayListener: AmityAudioPlayListener
) : AmityAudioMsgBaseViewHolder(itemView, itemViewModel, context, audioPlayListener) {

    private val binding: AmityItemAudioMessageSenderBinding? = DataBindingUtil.bind(itemView)
    private var popUp: AmityPopUp? = null

    init {
        binding?.vmAudioMsg = itemViewModel
        addViewModelListeners()
    }

    override fun getAudioViewHolder(): AmityAudioMsgBaseViewHolder = this

    private fun addViewModelListeners() {
        itemViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.DISMISS_POPUP -> popUp?.dismiss()
                else -> {
                }
            }
        }
    }

    override fun setMessageData(item: AmityMessage) {
        itemViewModel.getUploadProgress(item)
    }

    override fun showPopUp() {
        if (!itemViewModel.uploading.get()) {
            popUp = AmityPopUp()
            val anchor: View = itemView.findViewById(R.id.layoutAudio)
            val inflater: LayoutInflater = LayoutInflater.from(anchor.context)
            val binding: AmityPopupMsgDeleteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.amity_popup_msg_delete, null, true
            )
            binding.viewModel = itemViewModel
            popUp?.showPopUp(binding.root, anchor, itemViewModel, AmityPopUp.PopUpGravity.END)
        }
    }
}