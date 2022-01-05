package com.amity.socialcloud.uikit.community.mycommunity.listener

import com.amity.socialcloud.sdk.social.community.AmityCommunity

interface AmityMyCommunityItemClickListener {
    fun onCommunitySelected(community: AmityCommunity?)
}