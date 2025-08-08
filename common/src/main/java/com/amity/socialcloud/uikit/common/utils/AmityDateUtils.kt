package com.amity.socialcloud.uikit.common.utils

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
    private const val TIME_HOUR_FORMAT = "HH:mm:ss"
    private const val HOUR_IN_MILLISECOND = 3600000
    private const val EMPTY_FORMATTED_TIME = "0:00"
    private const val TRANSFORMED_EMPTY_FORMATTED_TIME = "0:01"
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

    private fun getFormattedTime(milliSeconds: Int): String {
        if (milliSeconds / HOUR_IN_MILLISECOND > 0) {
            formatter = SimpleDateFormat(TIME_MINUTE_FORMAT, Locale.getDefault())
        }
        val date = Date(milliSeconds.toLong())
        return formatter.format(date)
    }

    fun getFormattedTimeForChat(milliSeconds: Int): String {
        var formattedTime = getFormattedTime(milliSeconds)
        if (formattedTime == EMPTY_FORMATTED_TIME) {
            formattedTime = TRANSFORMED_EMPTY_FORMATTED_TIME
        }
        return formattedTime
    }
}