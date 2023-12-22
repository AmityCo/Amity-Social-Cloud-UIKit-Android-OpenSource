package com.amity.socialcloud.uikit.community.compose.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.amity.socialcloud.uikit.common.config.AmityUIKitDrawableResolver
import com.google.gson.JsonObject

fun JsonObject.getBackgroundColor(): Color {
    return getValue("background_color").asColor()
}

fun JsonObject.getValue(name: String): String {
    return get(name)?.asString ?: ""
}

fun JsonObject.getValueAsList(name: String): List<String> {
    return get(name)?.asJsonArray?.map { it.asString } ?: emptyList()
}

fun String.asColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color.Transparent
    }
}

fun List<String>.asColorList(): List<Color> {
    return this.map { it.asColor() }
}

@DrawableRes
fun String.asDrawableRes(): Int {
    return AmityUIKitDrawableResolver.getDrawableRes(this)
}

fun String.asBoolean(): Boolean {
    return this.toBoolean()
}