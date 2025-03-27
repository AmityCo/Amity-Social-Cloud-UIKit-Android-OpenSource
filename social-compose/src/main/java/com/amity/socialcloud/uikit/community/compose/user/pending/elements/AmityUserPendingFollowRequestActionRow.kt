package com.amity.socialcloud.uikit.community.compose.user.pending.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityUserPendingFollowRequestActionRow(
    modifier: Modifier = Modifier,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = AmityTheme.colors.highlight,
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            enabled = true,
            modifier = modifier.weight(1f),
            onClick = onAccept,
        ) {
            Text(
                text = "Accept",
                style = AmityTheme.typography.captionLegacy.copy(
                    color = AmityTheme.colors.background,
                ),
            )
        }

        OutlinedButton(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(1.dp, AmityTheme.colors.baseShade3),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            modifier = modifier.weight(1f),
            onClick = onDecline,
        ) {
            Text(
                text = "Decline",
                style = AmityTheme.typography.bodyLegacy,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityPendingPostActionRowPreview() {
    AmityUserPendingFollowRequestActionRow()
}