package com.amity.socialcloud.uikit.community.compose.post.detail.components

import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostPriority

enum class AmityPostContentComponentStyle {
    FEED,
    DETAIL,
    ANNOUNCEMENT_FEED,
    ANNOUNCEMENT_DETAIL,
    PIN_FEED,
    PIN_DETAIL;
    
    fun fromString(style: String): AmityPostContentComponentStyle {
        return when (style) {
            "FEED" -> FEED
            "DETAIL" -> DETAIL
            "ANNOUNCEMENT_FEED" -> ANNOUNCEMENT_FEED
            "ANNOUNCEMENT_DETAIL" -> ANNOUNCEMENT_DETAIL
            "PIN_FEED" -> PIN_FEED
            "PIN_DETAIL" -> PIN_DETAIL
            else -> FEED
        }
    }
}