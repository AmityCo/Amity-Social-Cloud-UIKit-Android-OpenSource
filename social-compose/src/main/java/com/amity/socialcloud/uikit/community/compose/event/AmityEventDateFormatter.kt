package com.amity.socialcloud.uikit.community.compose.event

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

/**
 * Formats event timestamp with start and end times.
 * Formats as "Today/Tomorrow/Yesterday" for relative dates, or full date for others
 * If endTime is null, defaults to 12 hours after startTime
 * 
 * @param startTime Event start time (required)
 * @param endTime Event end time (optional, defaults to startTime + 12 hours)
 * @return Formatted time string
 */
fun formatEventTimestamp(startTime: DateTime, endTime: DateTime?): String {
    val today = LocalDate.now()
    val startDate = startTime.toLocalDate()
    
    val timeFormatter = DateTimeFormat.forPattern("h:mm aa")
    val dateTimeFormatter = DateTimeFormat.forPattern("MMM dd yyyy, h:mm aa")
    
    // If no endTime provided, default to 12 hours after startTime
    val finalEndTime = endTime ?: startTime.plusHours(12)
    val endDate = finalEndTime.toLocalDate()
    
    // Same day (start and end on same date)
    if (startDate == endDate) {
        val formattedEndTime = timeFormatter.print(finalEndTime)
        return when {
            startDate == today.minusDays(1) -> {
                "Yesterday, ${timeFormatter.print(startTime)} to $formattedEndTime"
            }
            startDate == today -> {
                "Today, ${timeFormatter.print(startTime)} to $formattedEndTime"
            }
            startDate == today.plusDays(1) -> {
                "Tomorrow, ${timeFormatter.print(startTime)} to $formattedEndTime"
            }
            else -> {
                "${dateTimeFormatter.print(startTime)} to $formattedEndTime"
            }
        }
    }
    
    // Different days (start and end on different dates)
    val formattedEndTime = dateTimeFormatter.print(finalEndTime)
    return when {
        startDate == today.minusDays(1) -> {
            "Yesterday, ${timeFormatter.print(startTime)} to $formattedEndTime"
        }
        startDate == today -> {
            "Today, ${timeFormatter.print(startTime)} to $formattedEndTime"
        }
        startDate == today.plusDays(1) -> {
            "Tomorrow, ${timeFormatter.print(startTime)} to $formattedEndTime"
        }
        else -> {
            "${dateTimeFormatter.print(startTime)} to $formattedEndTime"
        }
    }
}
