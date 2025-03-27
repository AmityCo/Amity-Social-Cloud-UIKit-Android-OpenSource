package com.amity.socialcloud.uikit.community.compose.livestream.util

enum class LivestreamScreenType {
    CREATE,
    WATCH;

    companion object {
        fun fromString(value: String): LivestreamScreenType? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}

enum class LivestreamErrorScreenType {
    INTERNET,
    LIMIT_EXCEEDED,
    DELETED,
    TERMINATED,
    NONE;

    companion object {
        fun fromString(value: String): LivestreamErrorScreenType? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}