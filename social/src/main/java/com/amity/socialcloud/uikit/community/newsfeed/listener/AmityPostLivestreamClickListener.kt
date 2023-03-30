package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.sdk.model.video.stream.AmityStream

interface AmityPostLivestreamClickListener {
    fun onClickLivestreamVideo(stream: AmityStream)
}