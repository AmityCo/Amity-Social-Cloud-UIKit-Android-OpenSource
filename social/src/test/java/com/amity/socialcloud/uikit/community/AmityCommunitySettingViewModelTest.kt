package com.amity.socialcloud.uikit.community

import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunityRepository
import com.amity.socialcloud.uikit.community.setting.AmityCommunitySettingViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.Completable
import org.junit.Test

class AmityCommunitySettingViewModelTest {

    @Test
    fun when_leaveCommunity_expect_success() {
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.leaveCommunity(any()) } returns Completable.complete()

        val viewModel = AmityCommunitySettingViewModel()
        //val res = viewModel.leaveCommunity().test()
        //res.assertComplete()
    }

    @Test
    fun when_leaveCommunity_expect_failure() {
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.leaveCommunity(any()) } returns Completable.error(Exception("test_error"))

        val viewModel = AmityCommunitySettingViewModel()
        //val res = viewModel.leaveCommunity().test()
        //res.assertErrorMessage("test_error")
    }

    @Test
    fun when_deleteCommunity_expect_success() {
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.deleteCommunity(any()) } returns Completable.complete()

        val viewModel = AmityCommunitySettingViewModel()
        //val res = viewModel.closeCommunity().test()
        //res.assertComplete()
    }

    @Test
    fun when_deleteCommunity_expect_failure() {
        mockkStatic(AmitySocialClient::class)
        val communityRepository: AmityCommunityRepository = mockk()
        every { AmitySocialClient.newCommunityRepository() } returns communityRepository
        every { communityRepository.deleteCommunity(any()) } returns Completable.error(Exception("test_error"))

        val viewModel = AmityCommunitySettingViewModel()
        //val res = viewModel.closeCommunity().test()
        //res.assertErrorMessage("test_error")
    }
}