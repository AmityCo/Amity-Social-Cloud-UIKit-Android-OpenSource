package com.amity.socialcloud.uikit.community

import androidx.paging.PagedList
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.core.user.AmityUserRepository
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.ui.viewModel.AmitySelectMembersViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Test

class AmitySelectMembersViewModelTest {

    @Test
    fun initTest() {
        val viewModel = AmitySelectMembersViewModel()
        assertEquals(viewModel.searchString.get(), "")
        assertEquals(viewModel.selectedMembersList.size, 0)
        assertEquals(viewModel.selectedMemberSet.size, 0)
        assertEquals(viewModel.memberMap.size, 0)
        assertEquals(viewModel.searchMemberMap.size, 0)
        assertFalse(viewModel.isSearchUser.get())
    }

    @Test
    fun when_getAllUsers_expect_Flowablex_PagedListUser() {
        val mockList: PagedList<AmityUser> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmityCoreClient::class)
        val userRepository: AmityUserRepository = mockk()
        every { AmityCoreClient.newUserRepository() } returns userRepository
        every {
            userRepository.searchUserByDisplayName(any()).build().query()
        } returns Flowable.just(mockList)

        val viewModel = AmitySelectMembersViewModel()
        val res = viewModel.getAllUsers().blockingFirst()
        assertEquals(res.size, 5)
    }

    @Test
    fun when_searchUser_expect_Flowablex_PagedListUser() {
        val mockList: PagedList<AmityUser> = mockk()
        every { mockList.size } returns 5
        mockkStatic(AmityCoreClient::class)
        val userRepository: AmityUserRepository = mockk()
        every { AmityCoreClient.newUserRepository() } returns userRepository
        every {
            userRepository.searchUserByDisplayName(any()).build().query()
        } returns Flowable.just(mockList)

        val viewModel = AmitySelectMembersViewModel()
        viewModel.searchString.set("Sum")
        val res = viewModel.searchUser().blockingFirst()
        assertEquals(res.size, 5)
    }

    @Test
    fun prepareSelectedMembersListTest() {
        val member = AmitySelectMemberItem("1", "testUrl", "testName", "", true)
        val viewModel = AmitySelectMembersViewModel()

        viewModel.prepareSelectedMembersList(member, true)
        assertEquals(viewModel.selectedMembersList.size, 1)

        viewModel.prepareSelectedMembersList(member, false)
        assertEquals(viewModel.selectedMembersList.size, 0)
    }

    @Test
    fun when_searchString_change_expect_event_SEARCH_STRING_CHANGED() {
        var searchStringChanged = false
        val viewModel = AmitySelectMembersViewModel()
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