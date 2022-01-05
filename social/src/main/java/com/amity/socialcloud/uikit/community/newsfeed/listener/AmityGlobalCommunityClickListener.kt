package com.amity.socialcloud.uikit.community.newsfeed.listener

import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity

@Deprecated("Beta usage")
interface AmityGlobalCommunityClickListener {

    fun onClickCommunity(fragment: Fragment, community: AmityCommunity) {
        val intent = AmityCommunityPageActivity.newIntent(fragment.requireContext(), community)
        fragment.requireContext().startActivity(intent)
    }
}