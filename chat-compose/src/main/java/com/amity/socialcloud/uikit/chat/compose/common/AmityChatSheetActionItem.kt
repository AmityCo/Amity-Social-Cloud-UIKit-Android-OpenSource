package com.amity.socialcloud.uikit.chat.compose.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

/**
 * Chat's bottom-sheet action row — the design-token replacement for the legacy
 * `AmityBottomSheetActionItem` (which non-chat features keep using).
 *
 * Binds the List-row token grammar: icon `Icon/List/Leading/{Default|Destructive}/Default`,
 * label `Text/List/Header/{Default|Destructive}/Default`. Geometry matches the legacy row
 * (24dp icon · 12dp gap · 4/16 padding) so existing sheets don't shift.
 */
@Composable
fun AmityChatSheetActionItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int?,
    text: String,
    destructive: Boolean = false,
    onClick: () -> Unit,
) {
    val iconTint = if (destructive) {
        AmityTheme.token(AmityColorToken.IconListLeadingDestructiveDefault)
    } else {
        AmityTheme.token(AmityColorToken.IconListLeadingDefaultDefault)
    }
    val textColor = if (destructive) {
        AmityTheme.token(AmityColorToken.TextListHeaderDestructiveDefault)
    } else {
        AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onClick() }
            .padding(horizontal = 4.dp, vertical = 16.dp)
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
                color = textColor,
            )
        )
    }
}

/**
 * Composable-icon overload (16dp gap · 16 padding) for rows whose leading slot is a composed
 * atom, e.g. an icon-button-styled leading glyph.
 */
@Composable
fun AmityChatSheetActionItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    text: String,
    destructive: Boolean = false,
    onClick: () -> Unit,
) {
    val textColor = if (destructive) {
        AmityTheme.token(AmityColorToken.TextListHeaderDestructiveDefault)
    } else {
        AmityTheme.token(AmityColorToken.TextListHeaderDefaultDefault)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onClick() }
            .padding(16.dp)
    ) {
        icon()
        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
                color = textColor,
            )
        )
    }
}
