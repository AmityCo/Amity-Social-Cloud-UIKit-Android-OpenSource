package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.social.community.AmityCommunity

interface AmityCreatePostCommunitySelectionListener {
    fun onClickCommunity(community: AmityCommunity, position: Int)
}