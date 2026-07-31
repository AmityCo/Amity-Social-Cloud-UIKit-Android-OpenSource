package com.amity.socialcloud.uikit.chat.compose.common

import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Chat's own relative-time formatter for list rows. Mirrors the shared social formatter but renders
 * the most-recent bucket (under a minute) as "Now" instead of "Just now"; minute/hour/day suffixes
 * and the date fallbacks match. Kept separate so chat's time copy can evolve without affecting the
 * social surfaces that use the shared formatter.
 */
fun DateTime.readableChatTimeDiff(): String {
    val now = DateTime.now()

    if (now.year().get() > this.year().get()) {
        return SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(this.toDate())
    }

    if (now.dayOfYear - this.dayOfYear <= 7) {
        val diff = now.millis - this.millis
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val common = DefaultAmityCommonStringProvider.getInstance()
        return when {
            days > 0 -> days.toString() + common.getString("amity_common_time_time_days_suffix")
            hours > 0 -> hours.toString() + common.getString("amity_common_time_time_hours_suffix")
            minutes > 0 -> minutes.toString() + common.getString("amity_common_time_time_minutes_suffix")
            else -> DefaultAmityChatStringProvider.getInstance().getString("chat.list.timestamp.now")
        }
    }

    return SimpleDateFormat("d MMM", Locale.getDefault()).format(this.toDate())
}
