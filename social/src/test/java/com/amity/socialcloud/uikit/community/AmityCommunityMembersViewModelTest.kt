package com.amity.socialcloud.uikit.community

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.community.members.AmityCommunityMembersViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Test

class AmityCommunityMembersViewModelTest {

    @Test
    fun initTest() {
        val viewModel = AmityCommunityMembersViewModel()
        assertEquals(viewModel.communityId, "")
        assertEquals(viewModel.searchString.get(), "")
        assertFalse(viewModel.emptyMembersList.get())
    }

    @Test
    fun when_getCommunityMembers_expect_Flowable_pagedList_CommunityMemberShip() {
        val mockList: PagedList<AmityCommunityMember> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every {
            communityRepository.membership(any()).getCommunityMemberships()
                .filter(any()).build().query()
        } returns Flowable.just(mockList)

        val viewModel = AmityCommunityMembersViewModel()
        val res = viewModel.getCommunityMembers().blockingFirst()
        assertEquals(res.size, 5)
    }

    @Test
    fun when_searchString_change_expect_event_SEARCH_STRING_CHANGED() {
        var searchStringChanged = false
        val viewModel = AmityCommunityMembersViewModel()
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