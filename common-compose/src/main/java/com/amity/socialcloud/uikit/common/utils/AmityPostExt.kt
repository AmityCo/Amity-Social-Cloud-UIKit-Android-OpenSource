package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.sdk.model.social.post.AmityPost

fun AmityPost.isSupportedDataTypes(): Boolean {
    return getChildren().isEmpty() || getChildren().any { child ->
        val data = child.getData()
        return data is AmityPost.Data.TEXT
                || data is AmityPost.Data.IMAGE
                || data is AmityPost.Data.VIDEO
                || data is AmityPost.Data.LIVE_STREAM
                || data is AmityPost.Data.POLL
    }
}

