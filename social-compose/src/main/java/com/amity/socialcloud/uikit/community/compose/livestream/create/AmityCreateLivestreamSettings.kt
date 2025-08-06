package com.amity.socialcloud.uikit.community.compose.livestream.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityCreateLivestreamSettings(
    isReadOnly: Boolean,
    onReadOnlyToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(255.dp)
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Set live stream to read-only",
                    color = Color.White,
                    style = AmityTheme.typography.bodyBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Members who are not streamer can read messages but cannot send any messages.",
                    color = Color(0xFFA5A9B5),
                    style = AmityTheme.typography.caption
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Switch(
                checked = isReadOnly,
                onCheckedChange = onReadOnlyToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AmityTheme.colors.primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Gray
                )
            )
        }
    }
}