package com.amity.socialcloud.uikit.community

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.sdk.social.community.AmityCommunitySortOption
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.community.mycommunity.viewmodel.AmityMyCommunityListViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AmityMyCommunityListViewModelTest {

    @Test
    fun when_getCommunityList_expect_FlowablePagedListCommunity() {
        val mockList: PagedList<AmityCommunity> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every {
            communityRepository.getCommunities().withKeyword(any())
                .filter(AmityCommunityFilter.MEMBER).sortBy(AmityCommunitySortOption.DISPLAY_NAME)
                .includeDeleted(false)
                .build().query()
        } returns Flowable.just(mockList)

        val viewModel = AmityMyCommunityListViewModel()
        val res = viewModel.getCommunityList().blockingFirst()
        assertEquals(res.size, 5)
    }

    @Test
    fun when_searchString_change_expect_event_SEARCH_STRING_CHANGED() {
        var searchStringChanged = false
        val viewModel = AmityMyCommunityListViewModel()
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.SEARCH_STRING_CHANGED -> searchStringChanged = true
                else -> {
                }
            }
        }
        viewModel.setPropertyChangeCallback()
        viewModel.searchString.set("123")
        assertTrue(searchStringChanged)
    }
}