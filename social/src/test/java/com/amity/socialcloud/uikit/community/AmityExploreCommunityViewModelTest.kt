package com.amity.socialcloud.uikit.community

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategorySortOption
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityExploreCommunityViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Test

class AmityExploreCommunityViewModelTest {

    @Test
    fun initTest() {
        val viewModel = AmityExploreCommunityViewModel()
        assertFalse(viewModel.emptyCategoryList.get())
        assertFalse(viewModel.emptyRecommendedList.get())
        assertFalse(viewModel.emptyTrendingList.get())

        viewModel.emptyCategoryList.set(true)
        viewModel.emptyRecommendedList.set(true)
        viewModel.emptyTrendingList.set(true)

        assertTrue(viewModel.emptyCategoryList.get())
        assertTrue(viewModel.emptyRecommendedList.get())
        assertTrue(viewModel.emptyTrendingList.get())
    }

    @Test
    fun when_getRecommendedCommunity_Expect_FlowablePagedListCommunity() {
        val mockList: PagedList<AmityCommunity> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.getRecommendedCommunities() } returns Flowable.just(mockList)

        val viewModel = AmityExploreCommunityViewModel()
        val res = viewModel.getRecommendedCommunity().blockingFirst()
        assertEquals(res.size, 5)
    }

    @Test
    fun when_getTrendingCommunity_Expect_FlowablePagedListCommunity() {
        val mockList: PagedList<AmityCommunity> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.getTrendingCommunities() } returns Flowable.just(mockList)

        val viewModel = AmityExploreCommunityViewModel()
        val res = viewModel.getTrendingCommunity().blockingFirst()
        assertEquals(res.size, 5)
    }

    @Test
    fun when_getCommunityCategory_Expect_FlowablePagedListCommunity() {
        val mockList: PagedList<AmityCommunityCategory> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every {
            communityRepository.getAllCategories().sortBy(AmityCommunityCategorySortOption.NAME)
                .includeDeleted(false)
                .build().query()
        } returns Flowable.just(mockList)

        val viewModel = AmityExploreCommunityViewModel()
        val res = viewModel.getCommunityCategory().blockingFirst()
        assertEquals(res.size, 5)
    }
}