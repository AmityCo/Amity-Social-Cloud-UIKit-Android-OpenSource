package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.video.stream.AmityStream

interface AmityPostLivestreamClickListener {
    fun onClickLivestreamVideo(stream: AmityStream)
}