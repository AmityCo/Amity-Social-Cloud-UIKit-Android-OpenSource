package com.amity.socialcloud.uikit.community.compose.screenshot.fixtures

import com.amity.socialcloud.sdk.model.core.user.AmityUser
import io.mockk.every
import io.mockk.mockk

object FakeUserFactory {

    fun currentUser(
        userId: String = "user-001",
        displayName: String = "Test User",
    ): AmityUser = mockk(relaxed = true) {
        every { getUserId() } returns userId
        every { getDisplayName() } returns displayName
        every { getDescription() } returns ""
    }
}
