package com.amity.socialcloud.uikit.sample

import android.content.Context
import android.content.Intent

fun Context.shareLinkToExternalApp(uri: String) {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_TEXT, uri)
    startActivity(Intent.createChooser(shareIntent, "Share link using"))
}