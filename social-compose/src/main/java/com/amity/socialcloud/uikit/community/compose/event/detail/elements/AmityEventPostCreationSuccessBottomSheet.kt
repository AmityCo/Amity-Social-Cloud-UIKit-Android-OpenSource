package com.amity.socialcloud.uikit.community.compose.event.detail.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

/**
 * Post-creation success sheet (entry point A of the pin-event flow).
 *
 * Shown once to the host immediately after they create an event, in place of the plain
 * "event created" toast. Nudges them to share the event to a feed so more people can find it.
 * The primary action reuses the same "post to feed" routing as the 3-dot menu item; the
 * secondary action just dismisses.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityEventPostCreationSuccessBottomSheet(
    shouldShow: Boolean,
    onDismiss: () -> Unit,
    onPostToFeed: () -> Unit,
) {
    if (!shouldShow) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AmityTheme.colors.background,
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 34.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(
                            color = AmityTheme.colors.baseShade3,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = CommonR.drawable.amity_ic_event_empty),
                contentDescription = "event created bottom sheet icon",
                colorFilter = ColorFilter.tint(AmityTheme.colors.baseShade2),
                modifier = Modifier.size(60.dp)
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = amitySocialString("amity_social_label_event_post_creation_success_title"),
                style = AmityTheme.typography.headLine,
                color = AmityTheme.colors.base,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = amitySocialString("amity_social_label_event_post_creation_success_description"),
                style = AmityTheme.typography.body,
                color = AmityTheme.colors.baseShade1,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onPostToFeed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.primary
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = amitySocialString("amity_social_button_post_to_feed"),
                    style = AmityTheme.typography.bodyBold.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                    ),
                    color = amityColorWhite,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = AmityTheme.colors.primaryShade2,
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .height(48.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                border = BorderStroke(width = 1.dp, color = AmityTheme.colors.secondaryShade3),
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = amitySocialString("amity_social_button_maybe_later"),
                    style = AmityTheme.typography.bodyBold,
                    color = AmityTheme.colors.secondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
