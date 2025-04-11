package com.amity.socialcloud.uikit.chat.recent.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter

class AmityRecentChatAdapter : AmityBaseRecyclerViewPagingDataAdapter<AmityChannel>(diffCallBack) {

    private var recentChatItemClickListener: AmityRecentChatItemClickListener? = null

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityChannel>() {
            override fun areItemsTheSame(oldItem: AmityChannel, newItem: AmityChannel): Boolean =
                oldItem.getChannelId() == newItem.getChannelId()
                        && oldItem.getUnreadCount() == newItem.getUnreadCount()


            override fun areContentsTheSame(oldItem: AmityChannel, newItem: AmityChannel): Boolean =
                oldItem == newItem
        }
    }

    override fun getLayoutId(position: Int, obj: AmityChannel?): Int =
        R.layout.amity_item_recent_message

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
        AmityRecentChatViewHolder(view, recentChatItemClickListener)

    fun setCommunityChatItemClickListener(listener: AmityRecentChatItemClickListener?) {
        this.recentChatItemClickListener = listener
    }

}
