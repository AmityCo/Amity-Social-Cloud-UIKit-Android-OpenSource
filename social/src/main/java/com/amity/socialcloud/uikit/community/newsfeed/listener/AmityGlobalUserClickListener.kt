package com.amity.socialcloud.uikit.community.newsfeed.listener

import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.profile.activity.AmityUserProfileActivity

@Deprecated("Beta usage")
interface AmityGlobalUserClickListener {
    fun onClickUser(fragment: Fragment, user: AmityUser) {
        val intent = AmityUserProfileActivity.newIntent(fragment.requireContext(), user.getUserId())
        fragment.requireContext().startActivity(intent)
    }
}