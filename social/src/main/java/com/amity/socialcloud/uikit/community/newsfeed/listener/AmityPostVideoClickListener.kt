package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.core.file.AmityImage

interface AmityPostVideoClickListener : AmityPostMediaClickListener {
    fun onClickVideoThumbnail(parentPostId: String, images: List<AmityImage>, position: Int)
}