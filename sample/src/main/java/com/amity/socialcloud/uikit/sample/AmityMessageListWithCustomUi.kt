package com.amity.socialcloud.uikit.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.chat.messages.adapter.AmityMessagePagingAdapter
import com.amity.socialcloud.uikit.chat.messages.composebar.AmityChatRoomComposeBar
import com.amity.socialcloud.uikit.chat.messages.fragment.AmityChatRoomFragment
import com.amity.socialcloud.uikit.chat.messages.viewHolder.AmityChatMessageBaseViewHolder
import com.amity.socialcloud.uikit.chat.util.MessageType

class AmityMessageListWithCustomUi : AppCompatActivity(),
    AmityMessagePagingAdapter.CustomViewHolderListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelId = intent.getStringExtra("CHANNEL_ID") ?: ""
        setContentView(R.layout.amity_activity_message_list_with_custom_ui)

        val messageListFragment = AmityChatRoomFragment.newInstance(channelId)
            .composeBar(AmityChatRoomComposeBar.TEXT)
            .customViewHolder(this)
            .build(this)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, messageListFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): AmityChatMessageBaseViewHolder? {
        return when (viewType) {
            MessageType.MESSAGE_ID_TEXT_RECEIVER -> AmityTextReceiverViewHolder(
                inflater.inflate(R.layout.amity_item_text_receiver, parent, false), AmityMyTextMsgViewModel()
            )
            MessageType.MESSAGE_ID_TEXT_SENDER -> AmityTextSenderViewHolder(
                inflater.inflate(R.layout.amity_item_text_sender, parent, false), AmityMyTextMsgViewModel()
            )
            else -> null
        }
    }
}