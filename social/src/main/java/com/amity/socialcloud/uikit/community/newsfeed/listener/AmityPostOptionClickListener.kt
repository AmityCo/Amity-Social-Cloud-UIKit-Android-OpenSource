package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.social.feed.AmityPost

interface AmityPostOptionClickListener {
    fun onClickPostOption(post: AmityPost)
}