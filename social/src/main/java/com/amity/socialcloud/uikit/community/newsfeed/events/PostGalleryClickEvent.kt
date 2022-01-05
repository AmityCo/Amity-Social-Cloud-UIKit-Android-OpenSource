package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.social.feed.AmityPost

sealed class PostGalleryClickEvent {


    class Image(val post: AmityPost) : PostGalleryClickEvent()

    class Video(val post: AmityPost) : PostGalleryClickEvent()

}