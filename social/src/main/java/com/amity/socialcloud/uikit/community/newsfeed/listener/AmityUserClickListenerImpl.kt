package com.amity.socialcloud.uikit.community.newsfeed.listener

import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.community.profile.activity.AmityUserProfileActivity

class AmityUserClickListenerImpl(private val fragment: Fragment) : AmityUserClickListener {

    override fun onClickUser(user: AmityUser) {
        val intent = AmityUserProfileActivity.newIntent(fragment.requireContext(), user.getUserId())
        fragment.requireContext().startActivity(intent)
    }
}