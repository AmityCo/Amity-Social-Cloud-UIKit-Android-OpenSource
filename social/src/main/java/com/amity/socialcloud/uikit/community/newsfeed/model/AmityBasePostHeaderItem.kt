package com.amity.socialcloud.uikit.community.newsfeed.model

import com.amity.socialcloud.sdk.model.social.post.AmityPost

data class AmityBasePostHeaderItem(val post: AmityPost,
                              val showTarget: Boolean,
                              val showOptions: Boolean = true)
