package com.amity.socialcloud.uikit.chat

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.chat.channel.AmityChannelRepository
import com.amity.socialcloud.uikit.chat.recent.fragment.AmityRecentChatViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Test


class AmityRecentChatViewModelTest {

    @Test
    fun when_getRecentChat_expect_PagedListOfChannel() {
        mockkStatic(AmityChatClient::class)
        val channelRepository: AmityChannelRepository = mockk()
        every { AmityChatClient.newChannelRepository() } returns channelRepository
        val mockList: PagedList<AmityChannel> = mockk()
        every { mockList.size } returns 5
        every {
            channelRepository.getChannels().types(any()).filter(any()).build()
                .query()
        } returns Flowable.just(mockList)

        val viewModel = AmityRecentChatViewModel()
        val res = viewModel.getRecentChat().blockingFirst()
        Assert.assertEquals(res.size, 5)
    }
}