package com.amity.socialcloud.uikit.community.compose.post.detail.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityReportErrorScreen(
    modifier: Modifier = Modifier,
    onCloseSheetClick: () -> Unit = {},
) {

    // Add state to track button enabled state
    val (isButtonEnabled, setButtonEnabled) = remember { mutableStateOf(true) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_livestream_unavailable),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade4,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.amity_v4_livestream_deleted_page_title),
                style = AmityTheme.typography.headLine.copy(
                    color = AmityTheme.colors.baseShade3,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.amity_v4_livestream_deleted_page_desc),
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.colors.baseShade3,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            HorizontalDivider(
                color = AmityTheme.colors.baseShade4,
            )

            // Submit button
            Button(
                onClick = {
                    setButtonEnabled(false)
                    onCloseSheetClick()
                },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmityTheme.colors.primary,
                    disabledContainerColor = AmityTheme.colors.primaryShade3
                ),
            ) {
                Text(
                    text = "Close",
                    style = AmityTheme.typography.bodyBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun AmityReportErrorScreenPreview() {
    AmityReportErrorScreen()
}