package com.amity.socialcloud.uikit.chat.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.messages.viewHolder.*
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityAudioMsgViewModel
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityImageMsgViewModel
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityTextMessageViewModel
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityUnknownMsgViewModel
import com.amity.socialcloud.uikit.chat.util.MessageType

class AmityMessageItemUtil {

    fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?,
        messageItemListener: AmityMessageItemListener?,
        audioPlayListener: AmityAudioPlayListener
    ): AmityChatMessageBaseViewHolder {
        return when (itemType) {
            MessageType.MESSAGE_ID_TEXT_RECEIVER -> getReceiverTextMsgViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener
            )
            MessageType.MESSAGE_ID_TEXT_SENDER -> getSenderTextMsgViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener,
                messageItemListener
            )
            MessageType.MESSAGE_ID_IMAGE_RECEIVER -> getReceiverImageMsgViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener
            )
            MessageType.MESSAGE_ID_IMAGE_SENDER -> getSenderImageMsgViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener
            )
            MessageType.MESSAGE_ID_AUDIO_RECEIVER -> getReceiverAudioMsgViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener,
                audioPlayListener
            )
            MessageType.MESSAGE_ID_AUDIO_SENDER -> getSenderAudioMsgViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener,
                audioPlayListener
            )
            MessageType.MESSAGE_ID_CUSTOM_RECEIVER -> getReceiverCustomMessageViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener
            )
            MessageType.MESSAGE_ID_CUSTOM_SENDER -> getSenderCustomMessageViewHolder(
                inflater,
                parent,
                itemType,
                viewHolderListener
            )
            else -> getUnknownMessageViewHolder(inflater, parent)
        }
    }

    private fun getReceiverTextMsgViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            val itemViewModel = AmityTextMessageViewModel()
            AmityTextMsgReceiverViewHolder(
                inflater.inflate(
                    R.layout.amity_item_text_message_receiver,
                    parent, false
                ), itemViewModel, parent.context
            )
        }
    }

    private fun getSenderTextMsgViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?,
        messageItemListener: AmityMessageItemListener?,
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            val itemViewModel = AmityTextMessageViewModel()
            itemViewModel.onAmityEventReceived += { event ->
                (event.dataObj as? Int)?.let {
                    messageItemListener?.onMessageItemClicked(it)
                }
            }
            AmityTextMsgSenderViewHolder(
                inflater.inflate(
                    R.layout.amity_item_text_message_sender,
                    parent, false
                ), itemViewModel, parent.context
            )
        }
    }

    private fun getReceiverImageMsgViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            val itemViewModel = AmityImageMsgViewModel()
            AmityImageMsgReceiverViewHolder(
                inflater.inflate(
                    R.layout.amity_item_image_msg_receiver,
                    parent, false
                ), itemViewModel, parent.context
            )
        }

    }

    private fun getSenderImageMsgViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            val itemViewModel = AmityImageMsgViewModel()
            AmityImageMsgSenderViewHolder(
                inflater.inflate(
                    R.layout.amity_item_image_msg_sender,
                    parent, false
                ), itemViewModel, parent.context
            )
        }
    }

    private fun getReceiverAudioMsgViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?,
        audioPlayListener: AmityAudioPlayListener
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            val itemViewModel = AmityAudioMsgViewModel()
            AmityAudioMsgReceiverViewHolder(
                inflater.inflate(
                    R.layout.amity_item_audio_message_receiver,
                    parent, false
                ), itemViewModel, parent.context, audioPlayListener
            )
        }
    }

    private fun getSenderAudioMsgViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?,
        audioPlayListener: AmityAudioPlayListener
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            val itemViewModel = AmityAudioMsgViewModel()
            AmityAudioMsgSenderViewHolder(
                inflater.inflate(
                    R.layout.amity_item_audio_message_sender,
                    parent, false
                ), itemViewModel, parent.context, audioPlayListener
            )
        }
    }

    private fun getUnknownMessageViewHolder(inflater: LayoutInflater, parent: ViewGroup):
            AmityChatMessageBaseViewHolder {
        return AmityUnknownMessageViewHolder(
            inflater.inflate(
                R.layout.amity_item_unknown_message, parent,
                false
            ), AmityUnknownMsgViewModel()
        )
    }

    private fun getSenderCustomMessageViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            AmityUnknownMessageViewHolder(
                inflater.inflate(
                    R.layout.amity_item_unknown_message, parent,
                    false
                ), AmityUnknownMsgViewModel()
            )
        }
    }

    private fun getReceiverCustomMessageViewHolder(
        inflater: LayoutInflater, parent: ViewGroup,
        itemType: Int,
        viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener?
    ): AmityChatMessageBaseViewHolder {
        return if (viewHolderListener?.getViewHolder(inflater, parent, itemType) != null) {
            viewHolderListener.getViewHolder(inflater, parent, itemType)!!
        } else {
            AmityUnknownMessageViewHolder(
                inflater.inflate(
                    R.layout.amity_item_unknown_message, parent,
                    false
                ), AmityUnknownMsgViewModel()
            )
        }
    }

    fun getMessageType(message: AmityMessage?): Int {
        if (message == null) {
            return MessageType.MESSAGE_ID_UNKNOWN
        }
        return getContentType(message, message.getCreatorId() == AmityCoreClient.getUserId())
    }

    private fun getContentType(message: AmityMessage, isSelf: Boolean): Int {
        return when (message.getDataType()) {
            AmityMessage.DataType.TEXT -> if (isSelf) {
                MessageType.MESSAGE_ID_TEXT_SENDER
            } else {
                MessageType.MESSAGE_ID_TEXT_RECEIVER
            }
            AmityMessage.DataType.IMAGE -> if (isSelf) {
                MessageType.MESSAGE_ID_IMAGE_SENDER
            } else {
                MessageType.MESSAGE_ID_IMAGE_RECEIVER
            }
            AmityMessage.DataType.FILE -> if (isSelf) {
                MessageType.MESSAGE_ID_FILE_SENDER
            } else {
                MessageType.MESSAGE_ID_FILE_RECEIVER
            }
            AmityMessage.DataType.AUDIO -> if (isSelf) {
                MessageType.MESSAGE_ID_AUDIO_SENDER
            } else {
                MessageType.MESSAGE_ID_AUDIO_RECEIVER
            }
            AmityMessage.DataType.CUSTOM -> if (isSelf) {
                MessageType.MESSAGE_ID_CUSTOM_SENDER
            } else {
                MessageType.MESSAGE_ID_CUSTOM_RECEIVER
            }
            else -> MessageType.MESSAGE_ID_UNKNOWN
        }
    }
}