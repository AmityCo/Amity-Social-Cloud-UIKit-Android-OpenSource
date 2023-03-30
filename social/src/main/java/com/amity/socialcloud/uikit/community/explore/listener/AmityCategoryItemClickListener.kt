package com.amity.socialcloud.uikit.community.explore.listener

import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory

interface AmityCategoryItemClickListener {
    fun onCategorySelected(category: AmityCommunityCategory)
}