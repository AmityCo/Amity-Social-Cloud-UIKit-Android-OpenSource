package com.amity.socialcloud.uikit.chat.recent.fragment

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.chat.channel.AmityChannelFilter
import com.amity.socialcloud.sdk.chat.channel.AmityChannelRepository
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.Flowable

class AmityRecentChatViewModel : AmityBaseViewModel() {

    var recentChatItemClickListener: AmityRecentChatItemClickListener? = null

    fun getRecentChat(): Flowable<PagedList<AmityChannel>> {
        val channelRepository: AmityChannelRepository = AmityChatClient.newChannelRepository()
        val types = listOf(AmityChannel.Type.CONVERSATION)

        return channelRepository.getChannels()
            .types(types)
            .filter(AmityChannelFilter.MEMBER)
            .build()
            .query()
    }
}