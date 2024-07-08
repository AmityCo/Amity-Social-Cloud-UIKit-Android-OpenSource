package com.amity.socialcloud.uikit.community.compose.target

enum class AmityTargetSelectionPageType(val key: String) {
    POST("post_generic"),
    POLL("post_poll"),
    LIVESTREAM("post_live_stream"),
    STORY("story_community");

    companion object {
        fun enumOf(value: String?): AmityTargetSelectionPageType {
            return values().find { it.key == value } ?: POST
        }
    }
}