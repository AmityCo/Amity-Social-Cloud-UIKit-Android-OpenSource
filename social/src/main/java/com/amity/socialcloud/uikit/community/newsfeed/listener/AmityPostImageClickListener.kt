package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.core.file.AmityImage

interface AmityPostImageClickListener : AmityPostMediaClickListener {
    fun onClickImage(mages: List<AmityImage>, position: Int)
}