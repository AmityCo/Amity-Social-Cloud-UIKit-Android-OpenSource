package com.amity.socialcloud.uikit.community.compose.target

enum class AmityPostTargetSelectionPageType(val key: String) {
    POST("post_generic"),
    POLL("post_poll"),
    LIVESTREAM("post_live_stream");

    companion object {
        fun enumOf(value: String?): AmityPostTargetSelectionPageType {
            return values().find { it.key == value } ?: POST
        }
    }
}