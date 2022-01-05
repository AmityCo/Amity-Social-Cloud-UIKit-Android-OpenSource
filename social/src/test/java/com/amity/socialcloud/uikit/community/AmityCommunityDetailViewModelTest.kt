package com.amity.socialcloud.uikit.community

import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert.*
import org.junit.Test

class AmityCommunityDetailViewModelTest {

    @Test
    fun initTest() {
        val viewModel = AmityCommunityDetailViewModel()
        assertEquals(viewModel.communityId, "")
        assertEquals(viewModel.avatarUrl.get(), "")
        assertEquals(viewModel.category.get(), "")
        assertEquals(viewModel.posts.get(), "0")
        assertEquals(viewModel.members.get(), "0")
        assertEquals(viewModel.description.get(), "")
        assertTrue(viewModel.isPublic.get())
        assertTrue(viewModel.isMember.get())
        assertFalse(viewModel.isOfficial.get())
        assertFalse(viewModel.isModerator.get())
    }

    @Test
    fun when_GetCommunityDetails_expect_flowable_Community() {
        val ekoCommunity: AmityCommunity = mockk()
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.getCommunity(any()) } returns Flowable.just(ekoCommunity)
        every { ekoCommunity.getChannelId() } returns "testId"
        val viewModel = AmityCommunityDetailViewModel()
        val res = viewModel.getCommunityDetail().blockingFirst()
        assertEquals(res.getChannelId(), "testId")
    }

    @Test
    fun setCommunityTest() {
        val displayName = "DisplayName"
        val postCount = 1000
        val membersCount = 10
        val description = "Description"
        val avatar: AmityImage = mockk()
        mockkStatic(AmitySocialClient::class)
        every { AmityCoreClient.getUserId() } returns "testUser"
        val ekoCommunityCategory: AmityCommunityCategory = mockk()
        every { ekoCommunityCategory.getName() } returns "test"
        val mockList = ArrayList<AmityCommunityCategory>()
        mockList.add(ekoCommunityCategory)
        val ekoCommunity: AmityCommunity = mockk()
        every { ekoCommunity.getDisplayName() } returns displayName
        every { ekoCommunity.getPostCount() } returns postCount
        every { ekoCommunity.getMemberCount() } returns membersCount
        every { ekoCommunity.getDescription() } returns description
        every { ekoCommunity.isPublic() } returns true
        every { ekoCommunity.isJoined() } returns true
        every { ekoCommunity.isOfficial() } returns true
        every { ekoCommunity.getDescription() } returns description
        every { ekoCommunity.getUserId() } returns "testUser"
        every { avatar.getUrl(any()) } returns "testUrl"
        every { ekoCommunity.getAvatar() } returns avatar
        every { ekoCommunity.getCategories() } returns mockList

        val viewModel = AmityCommunityDetailViewModel()
        viewModel.setCommunity(ekoCommunity)
        assertEquals(viewModel.name.get(), displayName)
        assertEquals(viewModel.category.get(), "test")
        assertEquals(viewModel.posts.get(), "1K")
        assertEquals(viewModel.members.get(), "10")
        assertEquals(viewModel.description.get(), description)
        assertEquals(viewModel.avatarUrl.get(), "testUrl")
        assertTrue(viewModel.isPublic.get())
        assertTrue(viewModel.isMember.get())
        assertTrue(viewModel.isOfficial.get())
    }

    @Test
    fun when_join_community_expect_success() {
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.joinCommunity(any()) } returns Completable.complete()
        val viewModel = AmityCommunityDetailViewModel()
        val res = viewModel.joinCommunity().test()
        res.assertComplete()
    }

    @Test
    fun when_join_community_expect_failure() {
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.joinCommunity(any()) } returns Completable.error(Exception("test_error"))
        val viewModel = AmityCommunityDetailViewModel()
        val res = viewModel.joinCommunity().test()
        res.assertErrorMessage("test_error")
    }

    @Test
    fun onPrimaryButtonClickTest() {
        var editProfile = false
        val viewModel = AmityCommunityDetailViewModel()
        viewModel.isModerator.set(true)
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.EDIT_PROFILE -> editProfile = true
                else -> {
                }
            }
        }
        viewModel.onMessageButtonClick()
        assertTrue(editProfile)
    }
}