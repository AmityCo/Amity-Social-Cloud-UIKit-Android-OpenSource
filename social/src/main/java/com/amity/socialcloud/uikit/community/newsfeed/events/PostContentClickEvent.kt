package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream

sealed class PostContentClickEvent {

    class Text(val post: AmityPost): PostContentClickEvent()

    class Image(val images: List<AmityImage>, val selectedPosition: Int) : PostContentClickEvent()

    class Video(val parentPostId: String,val images: List<AmityImage>,val selectedPosition: Int) : PostContentClickEvent()

    class File(val file: AmityFile) : PostContentClickEvent()

    class Livestream(val stream: AmityStream) : PostContentClickEvent()
}