package com.amity.socialcloud.uikit.common.config

enum class AmityUIKitTheme(val key: String) {
    DEFAULT("default"),
    DARK("dark"),
    LIGHT("light");

    companion object {
        fun enumOf(value: String): AmityUIKitTheme = values().find { it.key == value } ?: DEFAULT
    }
}