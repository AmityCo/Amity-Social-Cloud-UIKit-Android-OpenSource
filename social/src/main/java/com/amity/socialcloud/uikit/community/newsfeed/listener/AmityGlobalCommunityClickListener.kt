package com.amity.socialcloud.uikit.community.newsfeed.listener

import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity

@Deprecated("Beta usage")
interface AmityGlobalCommunityClickListener {

    fun onClickCommunity(fragment: Fragment, community: AmityCommunity) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = fragment.requireContext(),
            communityId = community.getCommunityId(),
        )
        fragment.requireContext().startActivity(intent)
    }
}