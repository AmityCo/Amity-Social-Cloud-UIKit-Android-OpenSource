package com.amity.socialcloud.uikit.community.compose.livestream.create.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityMediaAndCameraNoPermissionView(
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    onOpenSettingClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier,
            text = title ?: "",
            style = AmityTheme.typography.titleBold,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            modifier = Modifier,
            text = description ?: "",
            style = AmityTheme.typography.caption,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Button(
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = AmityTheme.colors.primary,
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                onOpenSettingClick()
            }
        ) {
            Text(
                modifier = Modifier,
                text = "Open settings",
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            )
        }
    }
}

@Preview
@Composable
fun AmityCameraAndMicNoPermissionViewPreview() {
    AmityMediaAndCameraNoPermissionView(
        modifier = Modifier.height(300.dp),
        title = "Allow access to your\ncamera and microphone",
        description = "This lets you record and live stream\n" +
                "from this device."
    )
}