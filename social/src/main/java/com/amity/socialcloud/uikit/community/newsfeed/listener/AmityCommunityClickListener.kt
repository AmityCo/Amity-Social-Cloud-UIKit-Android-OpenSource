package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

interface AmityCommunityClickListener {
    fun onClickCommunity(community: AmityCommunity)
}