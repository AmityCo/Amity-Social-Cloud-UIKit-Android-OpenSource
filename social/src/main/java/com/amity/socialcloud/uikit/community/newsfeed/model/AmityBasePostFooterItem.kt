package com.amity.socialcloud.uikit.community.newsfeed.model

import com.amity.socialcloud.sdk.model.social.post.AmityPost

sealed class AmityBasePostFooterItem(val post: AmityPost) {

    class POST_ENGAGEMENT(post: AmityPost,
                          val isReadOnly: Boolean) : AmityBasePostFooterItem(post)

    class COMMENT_PREVIEW(post: AmityPost,
                          val isReadOnly: Boolean) : AmityBasePostFooterItem(post)

    class POST_REVIEW(post: AmityPost) : AmityBasePostFooterItem(post)

}

