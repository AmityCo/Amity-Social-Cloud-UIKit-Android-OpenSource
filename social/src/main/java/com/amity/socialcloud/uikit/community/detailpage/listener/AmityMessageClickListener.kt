package com.amity.socialcloud.uikit.community.detailpage.listener

import com.amity.socialcloud.sdk.social.community.AmityCommunity

interface AmityMessageClickListener {
    fun onClickMessage(community: AmityCommunity?)
}