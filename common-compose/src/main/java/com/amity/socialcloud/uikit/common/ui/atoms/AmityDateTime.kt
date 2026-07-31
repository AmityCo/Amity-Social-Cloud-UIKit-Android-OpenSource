package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.localization.amityCommonString
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

// Pill corner radius is authored as a Figma literal (no bound cornerRadius variable), shared by
// both the "This year" and "Last year" date-pattern variants.
private val DateSeparatorCornerRadius = 20.dp

/**
 * AmityDateSeparator — the centered day-pill that breaks a chat message list into calendar-day
 * groups (e.g. "Mon, 15 Jan" / "Mon, 15 Jan 2023"). Presentational only: no interaction state, not
 * tappable.
 *
 * Always binds Surface/Date&Time/DateSeparator/Default (pill fill) together with
 * Text/Date&Time/DateSeparator/Default (label) — there is no partial styling. The caller supplies
 * the pre-formatted [label]; this atom never computes the date pattern itself.
 *
 * @param label the pre-formatted date string, e.g. "Mon, 15 Jan" or "Mon, 15 Jan 2023".
 * @param modifier applied to the full-width row the pill centers within.
 */
@Composable
fun AmityDateSeparator(
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            modifier = Modifier
                // ponytail: Figma authors a shared two-layer drop-shadow effect style on this pill,
                // but no colors-v2 elevation/shadow token exists anywhere in the registry for it —
                // approximated with a plain Compose shadow instead of leaving the pill flat. Revisit
                // once an elevation token family is introduced.
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(DateSeparatorCornerRadius))
                .background(
                    color = AmityTheme.token(AmityColorToken.SurfaceDateAndTimeDateSeparatorDefault),
                    shape = RoundedCornerShape(DateSeparatorCornerRadius),
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            style = AmityTheme.typography.captionLegacy.copy(
                fontWeight = FontWeight.Normal,
                color = AmityTheme.token(AmityColorToken.TextDateAndTimeDateSeparatorDefault),
            ),
        )
    }
}

/**
 * AmityTimestamp — the bare time/date label attached to a chat message (relative or absolute),
 * e.g. "45m", "5 May", "11:00". Never renders a container: no surface/border/radius, matching the
 * Figma layer's invisible fill. Fixed height 18 (the 13/18 line box).
 *
 * The caller supplies the pre-formatted [text]; this atom does not compute relative-time buckets or
 * absolute clock/date strings.
 *
 * @param text the pre-formatted time/date string.
 * @param modifier applied to the row.
 * @param edited appends an inline "(edited)" suffix after [text], separated by a 2dp gap, when true.
 * @param color label color for [text]. Defaults to Text/Timestamp/Default; callers rendering a
 * non-default state (e.g. a "Sending…" row) pass their own state token instead.
 */
@Composable
fun AmityTimestamp(
    text: String,
    modifier: Modifier = Modifier,
    edited: Boolean = false,
    color: Color = AmityTheme.token(AmityColorToken.TextTimestampDefault),
) {
    Row(
        modifier = modifier.height(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = text,
            style = AmityTheme.typography.captionLegacy.copy(
                fontWeight = FontWeight.Normal,
                color = color,
            ),
        )
        if (edited) {
            Text(
                text = amityCommonString("amity_common_time_edited_suffix"),
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal,
                    // ponytail: the "(edited)" suffix binds a cross-file Figma variable this repo
                    // cannot resolve a name for — no colors-v2 token exists for it. Approximated as
                    // the base-2 neutral grey at 85% opacity, matching the pre-existing hub
                    // playground's identical workaround. Flagged for the token-registry-keeper to
                    // import the source variable.
                    color = AmityTheme.colors.baseShade2.copy(alpha = 0.85f),
                ),
            )
        }
    }
}
