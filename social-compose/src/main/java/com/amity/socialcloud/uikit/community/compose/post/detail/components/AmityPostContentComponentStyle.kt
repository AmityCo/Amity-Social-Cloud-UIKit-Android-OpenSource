package com.amity.socialcloud.uikit.community.compose.post.detail.components

enum class AmityPostContentComponentStyle {
    FEED,
    DETAIL;
    
    fun fromString(style: String): AmityPostContentComponentStyle {
        return when (style) {
            "FEED" -> FEED
            "DETAIL" -> DETAIL
            else -> FEED
        }
    }
}