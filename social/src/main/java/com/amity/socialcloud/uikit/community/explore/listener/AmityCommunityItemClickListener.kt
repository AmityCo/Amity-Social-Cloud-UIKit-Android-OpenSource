package com.amity.socialcloud.uikit.community.explore.listener

import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

interface AmityCommunityItemClickListener {
    fun onClick(community: AmityCommunity, position: Int)
}