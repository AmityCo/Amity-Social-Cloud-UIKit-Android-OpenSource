package com.amity.socialcloud.uikit.community.newsfeed.model

import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.newsfeed.util.AmityTimelineType

data class AmityBasePostHeaderItem(val post: AmityPost,
                              val showTarget: Boolean,
                              val showOptions: Boolean = true)
