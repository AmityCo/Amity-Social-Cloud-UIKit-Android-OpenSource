package com.amity.socialcloud.uikit.community.newsfeed.model

import com.amity.socialcloud.sdk.model.social.post.AmityPost

data class AmityBasePostItem(
		val post: AmityPost,
		val headerItems: List<AmityBasePostHeaderItem>,
		val contentItems: List<AmityBasePostContentItem>,
		val footerItems: List<AmityBasePostFooterItem>
)
