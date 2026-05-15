package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun DateTime.readableTimeDiff(): String {
    val diff = DateTime.now().millis - this.millis
    diff.let {
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)

        return when {
            days > 0 -> days.toString() + DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_time_days_suffix")
            hours > 0 -> hours.toString() + DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_time_hours_suffix")
            minutes > 0 -> minutes.toString() + DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_time_minutes_suffix")
            seconds > 1 -> seconds.toString() + DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_time_seconds_suffix")
            else -> DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_just_now")
        }
    }
}

fun DateTime.readableSocialTimeDiff(): String {
    val now = DateTime.now()

    if (now.year().get() > this.year().get()) {
        val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val formattedDateTime: String = formatter.format(this.toDate())
        return formattedDateTime
    } else {
        if (now.dayOfYear - this.dayOfYear <= 7) {
            //  within 7 days
            val diff = now.millis - this.millis
            diff.let {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)

                return when {
                    days > 0 -> days.toString() + DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_time_days_suffix")
                    hours > 0 -> hours.toString() + DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_time_hours_suffix")
                    minutes > 0 -> minutes.toString() + DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_time_minutes_suffix")
                    else -> DefaultAmityCommonStringProvider.getInstance().getString("amity_common_time_just_now")
                }
            }
        } else {
            val formatter = SimpleDateFormat("d MMM", Locale.getDefault())
            val formattedDateTime: String = formatter.format(this.toDate())
            return formattedDateTime
        }
    }
}