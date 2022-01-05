package com.amity.socialcloud.uikit.community

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.uikit.community.home.fragments.AmityCommunityHomeViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class AmityCommunityHomeViewModelTest {

    @Test
    fun initTest() {
        val viewModel = AmityCommunityHomeViewModel()
        assertFalse(viewModel.isSearchMode.get())
        assertFalse(viewModel.emptySearch.get())
    }

    @Test
    fun when_searchCommunity_expect_FlowablePagedListCommunity() {
        val mockList: PagedList<AmityCommunity> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every {
            communityRepository.getCommunities().withKeyword(any())
                .sortBy(any()).includeDeleted(false)
                .build()
                .query()
        } returns Flowable.just(mockList)

        val viewModel = AmityCommunityHomeViewModel()
        val res = viewModel.searchCommunity("abc").blockingFirst()
        assertEquals(res.size, 5)
    }
}