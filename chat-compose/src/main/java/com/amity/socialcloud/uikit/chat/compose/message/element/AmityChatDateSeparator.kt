package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDateSeparator
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Composable
fun AmityChatDateSeparator(
    dateTime: DateTime,
    modifier: Modifier = Modifier,
) {
    // The 8dp vertical margin is this message list's own spacing around the separator, not part of
    // the atom's pill geometry (see AmityDateSeparator's Design Tokens/Geometry — pill padding is
    // 4/8 vert/horiz only), so it stays here rather than baked into the shared atom.
    AmityDateSeparator(
        label = formatDateSeparator(dateTime),
        modifier = modifier.padding(vertical = 8.dp),
    )
}

private fun formatDateSeparator(dateTime: DateTime): String {
    val currentYear = DateTime.now().year
    val messageYear = dateTime.year

    val pattern = if (messageYear == currentYear) "EEE, d MMM" else "EEE, d MMM yyyy"
    return DateTimeFormat.forPattern(pattern).print(dateTime)
}

/**
 * Returns true if two dates (millis) fall on different calendar days.
 */
fun shouldShowDateSeparator(currentDateMillis: Long, nextDateMillis: Long): Boolean {
    val dt1 = DateTime(currentDateMillis)
    val dt2 = DateTime(nextDateMillis)
    return dt1.year != dt2.year || dt1.dayOfYear != dt2.dayOfYear
}
