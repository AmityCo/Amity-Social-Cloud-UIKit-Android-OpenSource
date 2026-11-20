package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.community.compose.post.autoplay.AmityFeedAutoPlayChoice
import com.amity.socialcloud.uikit.community.compose.post.autoplay.AmityFeedAutoPlayGate

/** Section title in the own-profile menu sheet, for example Account or Settings. */
@Composable
fun AmityUserMenuSectionTitle(text: String) {
    Text(
        text = text,
        style = AmityTheme.typography.titleBold,
        // The sheet's column centres its children; the Figma puts section titles on the left.
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    )
}

/**
 * The Autoplay in feed row of the own-profile menu. It shows the member's current choice on the
 * right; a tap opens [AmityFeedAutoPlaySettingSheet].
 */
@Composable
fun AmityFeedAutoPlaySettingRow(onClick: () -> Unit) {
    val choice by AmityFeedAutoPlayGate.choice.collectAsState()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .testTag("user_menu/autoplay_setting")
            .clickableWithoutRipple { onClick() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_autoplay_setting),
            contentDescription = null,
            tint = AmityTheme.colors.base,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = amitySocialString("amity_social_autoplay_setting_row"),
            style = AmityTheme.typography.body,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
        )
        Text(
            text = amitySocialString(choice.stringKey),
            style = AmityTheme.typography.caption.copy(color = AmityTheme.colors.baseShade1),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

/**
 * The member's autoplay choice: on Wi-Fi and mobile data, on Wi-Fi only, or Never. It opens over
 * the profile menu. Tapping a choice saves it and closes the sheet; tapping outside or Back closes
 * only this sheet and returns to the profile menu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityFeedAutoPlaySettingSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val choice by AmityFeedAutoPlayGate.choice.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = Modifier.semantics { testTagsAsResourceId = true },
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .navigationBarsPadding()
        ) {
            AmityFeedAutoPlayChoice.entries.forEach { option ->
                val isSelected = option == choice
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag(option.testTag)
                        .semantics { selected = isSelected }
                        .clickableWithoutRipple {
                            AmityFeedAutoPlayGate.setChoice(context, option)
                            onDismiss()
                        }
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = amitySocialString(option.stringKey),
                        style = AmityTheme.typography.body,
                        modifier = Modifier.weight(1f),
                    )
                    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                        RadioButton(
                            selected = isSelected,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AmityTheme.colors.primary,
                                unselectedColor = AmityTheme.colors.baseShade2,
                            ),
                            onClick = {
                                AmityFeedAutoPlayGate.setChoice(context, option)
                                onDismiss()
                            },
                        )
                    }
                }
            }
        }
    }
}
