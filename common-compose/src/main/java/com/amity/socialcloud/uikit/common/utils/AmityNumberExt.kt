package com.amity.socialcloud.uikit.common.utils

import java.util.Locale
import java.util.concurrent.TimeUnit

fun Number.formatVideoDuration(): String {
    val seconds = this.toLong()
    val hours = TimeUnit.SECONDS.toHours(seconds)
    val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
    val remainingSeconds = seconds % 60
    val formattedDuration = when {
        hours > 0 -> String.format(Locale.US, "%d:%02d:%02d", hours, minutes, remainingSeconds)
        else -> String.format(Locale.US, "%d:%02d", minutes, remainingSeconds)
    }

    return formattedDuration
}