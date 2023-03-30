package com.amity.socialcloud.uikit.community.newsfeed.model

import com.amity.socialcloud.sdk.model.social.post.AmityPost

data class AmityBasePostContentItem(val post: AmityPost, var showFullContent: Boolean = false)
