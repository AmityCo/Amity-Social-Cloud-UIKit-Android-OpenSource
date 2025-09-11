package com.amity.socialcloud.uikit.community.compose.utils

import android.content.Context
import android.content.Intent

fun sharePost(context: Context, postLink: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, postLink)
        putExtra(Intent.EXTRA_SUBJECT, "Check out this post")
    }

    val chooserIntent = Intent.createChooser(shareIntent, "Share post via")
    context.startActivity(chooserIntent)
}
