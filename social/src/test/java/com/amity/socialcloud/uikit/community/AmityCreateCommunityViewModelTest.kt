package com.amity.socialcloud.uikit.community

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.uikit.community.data.AmitySelectCategoryItem
import com.amity.socialcloud.uikit.community.ui.viewModel.AmityCreateCommunityViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AmityCreateCommunityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun initTest() {
        val viewModel = AmityCreateCommunityViewModel()
        assertEquals(viewModel.initialCategory, "")
        assertEquals(viewModel.category.get(), AmitySelectCategoryItem())
        assertTrue(viewModel.initialStateChanged.value == false)
        assertEquals(viewModel.communityId.get(), "")
        assertEquals(viewModel.communityName.get(), "")
        assertEquals(viewModel.description.get(), "")
        assertTrue(viewModel.isPublic.get())
        assertTrue(viewModel.addMemberVisible.get())
        assertFalse(viewModel.isAdmin.get())
        assertFalse(viewModel.nameError.get())
    }

    @Test
    fun changePostTypeTest() {
        val viewModel = AmityCreateCommunityViewModel()
        viewModel.changePostType(false)
        assertFalse(viewModel.isPublic.get())
        assertTrue(viewModel.initialStateChanged.value == true)
    }

    @Test
    fun changeAdminPostTest() {
        val viewModel = AmityCreateCommunityViewModel()
        assertFalse(viewModel.isAdmin.get())
        viewModel.changeAdminPost()
        assertTrue(viewModel.isAdmin.get())
    }

    @Test
    fun when_createCommunity_expect_SingleCommunity() {
        val ekoCommunity: AmityCommunity = mockk()
        every { ekoCommunity.getCommunityId() } returns "testId"
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every {
            communityRepository.createCommunity(any())
                .isPublic(any()).description(any()).userIds(any()).build().create()
        } returns Single.just(ekoCommunity)

        val viewModel = AmityCreateCommunityViewModel()
        viewModel.isAdmin.set(false)
        val res = viewModel.createCommunity().blockingGet()
        assertEquals(res.getCommunityId(), "testId")
    }

    @Test
    fun when_editCommunity_expect_SingleCommunity() {
        val ekoCommunity: AmityCommunity = mockk()
        every { ekoCommunity.getCommunityId() } returns "testId"
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every {
            communityRepository.updateCommunity(any())
                .displayName(any()).isPublic(any()).description(any()).build().update()
        } returns Single.just(ekoCommunity)

        val viewModel = AmityCreateCommunityViewModel()
        val res = viewModel.editCommunity().blockingGet()
        assertEquals(res.getCommunityId(), "testId")
    }

    @Test
    fun setCommunityDetailsTest() {
        val displayName = "displayName"
        val description = "description"
        val ekoCommunity: AmityCommunity = mockk()
        val ekoImage: AmityImage = mockk()
        val ekoCommunityCategory: AmityCommunityCategory = mockk()
        every { ekoCommunityCategory.getName() } returns "test"
        val mockList = ArrayList<AmityCommunityCategory>()
        mockList.add(ekoCommunityCategory)
        every { ekoImage.getUrl(any()) } returns "testUrl"
        every { ekoCommunity.getDisplayName() } returns displayName
        every { ekoCommunity.getDescription() } returns description
        every { ekoCommunity.isPublic() } returns true
        every { ekoCommunity.getAvatar() } returns ekoImage
        every { ekoCommunity.getCategories() } returns mockList
        every { ekoCommunity.getCommunityId() } returns "testID"

        val viewModel = AmityCreateCommunityViewModel()
        viewModel.setCommunityDetails(ekoCommunity)
        assertEquals(viewModel.communityName.get(), displayName)
        assertEquals(viewModel.description.get(), description)
        assertEquals(viewModel.avatarUrl.get(), "testUrl")
        assertEquals(viewModel.communityId.get(), "testID")
        assertTrue(viewModel.isPublic.get())
    }

    @Test
    fun setPropertyChangeCallbackTest() {
        val viewModel = AmityCreateCommunityViewModel()
        assertTrue(viewModel.initialStateChanged.value == false)

        viewModel.setPropertyChangeCallback()
        viewModel.communityName.set("test")
        assertTrue(viewModel.initialStateChanged.value == true)
        viewModel.communityName.set("")
        assertTrue(viewModel.initialStateChanged.value == false)

        viewModel.description.set("test description")
        assertTrue(viewModel.initialStateChanged.value == true)
        viewModel.description.set("")
        assertTrue(viewModel.initialStateChanged.value == false)
    }
}