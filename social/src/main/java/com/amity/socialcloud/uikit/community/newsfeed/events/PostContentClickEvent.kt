package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.core.AmityFile
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.sdk.video.stream.AmityStream

sealed class PostContentClickEvent {

    class Text(val post: AmityPost): PostContentClickEvent()

    class Image(val images: List<AmityImage>, val selectedPosition: Int) : PostContentClickEvent()

    class Video(val parentPostId: String,val images: List<AmityImage>,val selectedPosition: Int) : PostContentClickEvent()

    class File(val file: AmityFile) : PostContentClickEvent()

    class Livestream(val stream: AmityStream) : PostContentClickEvent()
}