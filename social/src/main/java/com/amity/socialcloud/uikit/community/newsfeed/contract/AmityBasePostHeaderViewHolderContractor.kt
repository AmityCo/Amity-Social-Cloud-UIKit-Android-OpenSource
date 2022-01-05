package com.amity.socialcloud.uikit.community.newsfeed.contract

import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostActionAvatarClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostActionCommunityClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostMoreOptionClickListener

interface AmityBasePostHeaderViewHolderContractor {

	fun setMoreOptionOnClickListener(listener: AmityPostMoreOptionClickListener)
	fun setAvatarClickListener(listener: AmityPostActionAvatarClickListener)
	fun setCommunityClickListener(listener: AmityPostActionCommunityClickListener)

}