package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

interface AmityCreatePostCommunitySelectionListener {
    fun onClickCommunity(community: AmityCommunity, position: Int)
}