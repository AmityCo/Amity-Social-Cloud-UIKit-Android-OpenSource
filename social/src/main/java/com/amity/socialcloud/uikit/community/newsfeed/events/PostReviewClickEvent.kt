package com.amity.socialcloud.uikit.community.newsfeed.events

import com.amity.socialcloud.sdk.social.feed.AmityPost

sealed class PostReviewClickEvent(val post: AmityPost) {

    class ACCEPT(post: AmityPost) : PostReviewClickEvent(post)
    class DECLINE(post: AmityPost) : PostReviewClickEvent(post)
}