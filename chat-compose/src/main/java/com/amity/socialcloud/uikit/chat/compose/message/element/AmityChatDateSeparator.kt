package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Composable
fun AmityChatDateSeparator(
    dateTime: DateTime,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = formatDateSeparator(dateTime),
            modifier = Modifier
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                )
                .background(
                    color = AmityTheme.colors.background,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            style = AmityTheme.typography.captionLegacy.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = AmityTheme.colors.baseShade1,
            ),
        )
    }
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
