package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.core.user.AmityUser

interface AmityUserClickListener {
    fun onClickUser(user: AmityUser)
}