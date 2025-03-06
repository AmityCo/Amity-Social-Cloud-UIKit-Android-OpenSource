package com.amity.socialcloud.uikit.community.compose.search.components

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity

class AmityPostSearchResultComponentBehaviour {

    open fun goToPostDetailPage(
        context: Context,
        id: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL,
        hideTarget: Boolean = false,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = id,
            category = category,
            hideTarget = hideTarget,
        )
        context.startActivity(intent)
    }

}