package com.amity.socialcloud.uikit.community.compose.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette.Swatch

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Swatch.toComposeColor(): Color {
    return Color(
        red = (rgb shr 16 and 0xFF) / 255f,
        green = (rgb shr 8 and 0xFF) / 255f,
        blue = (rgb and 0xFF) / 255f,
    )
}