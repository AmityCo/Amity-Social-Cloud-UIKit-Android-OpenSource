package com.amity.socialcloud.uikit.community

import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.community.members.AmityMembershipItemViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Test

class AmityMembershipItemViewModelTest {

    @Test
    fun when_reportUser_expect_success() {

        val ekoUser: AmityUser = mockk()
        every { ekoUser.report().flag() } returns Completable.complete()
        val viewModel = AmityMembershipItemViewModel()
        val res = viewModel.reportUser(ekoUser).test()
        res.assertComplete()
    }

    @Test
    fun when_reportUser_expect_failure() {
        val ekoUser: AmityUser = mockk()
        every { ekoUser.report().flag() } returns Completable.error(Exception("test_error"))
        val viewModel = AmityMembershipItemViewModel()
        val res = viewModel.reportUser(ekoUser).test()
        res.assertErrorMessage("test_error")
    }
}