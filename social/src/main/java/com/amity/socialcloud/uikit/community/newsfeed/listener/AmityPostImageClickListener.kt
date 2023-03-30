package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.model.core.file.AmityImage

interface AmityPostImageClickListener : AmityPostMediaClickListener {
    fun onClickImage(images: List<AmityImage>, position: Int)
}