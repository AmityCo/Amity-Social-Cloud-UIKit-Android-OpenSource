package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.model.social.post.AmityPost

interface AmityPostOptionClickListener {
    fun onClickPostOption(post: AmityPost)
}