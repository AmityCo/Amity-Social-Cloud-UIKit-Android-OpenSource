package com.amity.socialcloud.uikit.community.explore.listener

import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory

interface AmityCategoryItemClickListener {
    fun onCategorySelected(category: AmityCommunityCategory)
}