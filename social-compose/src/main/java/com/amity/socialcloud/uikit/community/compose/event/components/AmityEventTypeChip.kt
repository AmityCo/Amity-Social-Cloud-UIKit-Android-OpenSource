package com.amity.socialcloud.uikit.community.compose.event.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.model.social.event.AmityEventType
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

/**
 * A small, on-surface pill that labels an event as "In-person" or "Virtual".
 *
 * Unlike the cover-overlay badge used in [com.amity.socialcloud.uikit.community.compose.community.profile.component.EventCardItem],
 * this chip is styled for placement directly on a surface (e.g. the notification
 * tray item secondary line), so it uses a neutral shaded background instead of a
 * translucent dark overlay.
 */
@Composable
fun AmityEventTypeChip(
    type: AmityEventType?,
    modifier: Modifier = Modifier,
) {
    val label = when (type) {
        AmityEventType.IN_PERSON -> amitySocialString("amity_social_button_in_person")
        AmityEventType.VIRTUAL -> amitySocialString("amity_social_button_virtual")
        else -> amitySocialString("amity_social_button_virtual")
    }

    Text(
        text = label,
        style = AmityTheme.typography.captionBold,
        color = amityColorWhite,
        modifier = modifier
            .background(
                color = amityColorBlack.copy(alpha = 0.5f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun AmityEventTypeChipPreview() {
    AmityEventTypeChip(type = AmityEventType.IN_PERSON)
}
