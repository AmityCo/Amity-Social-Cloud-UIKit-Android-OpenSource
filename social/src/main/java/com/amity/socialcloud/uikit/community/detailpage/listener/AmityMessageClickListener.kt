package com.amity.socialcloud.uikit.community.detailpage.listener

import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

interface AmityMessageClickListener {
    fun onClickMessage(community: AmityCommunity?)
}