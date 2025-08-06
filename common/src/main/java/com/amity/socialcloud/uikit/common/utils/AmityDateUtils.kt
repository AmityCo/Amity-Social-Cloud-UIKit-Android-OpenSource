package com.amity.socialcloud.uikit.common.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object AmityDateUtils {

    private const val DAY_OF_WEEK = "EEEE"
    private const val YEAR = "yyyy"
    private const val MONTH_WITH_DATE = "MMMM d"
    private const val TIME_FORMAT = "h:mm a"
    const val TIME_MINUTE_FORMAT = "m:ss"
    private const val HOUR_IN_MILLISECOND = 3600000
    private var formatter = SimpleDateFormat(TIME_MINUTE_FORMAT, Locale.getDefault())

    private fun getTimeStr(timestamp: Long): String =
        SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(Date(timestamp))

    fun getRelativeDate(timestamp: Long): String = when {
        DateUtils.isToday(timestamp) -> AmityConstants.TODAY
        isYesterday(timestamp) -> AmityConstants.YESTERDAY
        isCurrentYear(timestamp) -> SimpleDateFormat(
            "$MONTH_WITH_DATE, $YEAR", Locale.getDefault()
        ).format(Date(timestamp))
        else -> SimpleDateFormat(
            DAY_OF_WEEK + " " + AmityConstants.DOT_SEPARATOR +
                    " " + MONTH_WITH_DATE + ", " + YEAR, Locale.getDefault()
        ).format(Date(timestamp))
    }

    fun getMessageTime(timestamp: Long): String = if (DateUtils.isToday(timestamp)) {
        getTimeStr(timestamp)
    } else {
        SimpleDateFormat(
            "dd/MM/yy", Locale.getDefault()
        ).format(Date(timestamp))
    }

    private fun isYesterday(time: Long): Boolean =
        DateUtils.isToday(time + TimeUnit.DAYS.toMillis(1))

    private fun isCurrentYear(timestamp: Long): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return year == cal.get(Calendar.YEAR)
    }

    @SuppressLint("DefaultLocale")
    fun getFormattedElapsedTime(milliSeconds: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds.toLong()) % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}