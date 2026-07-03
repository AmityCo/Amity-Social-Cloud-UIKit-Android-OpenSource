package com.amity.socialcloud.uikit.chat.compose.message.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@Composable
fun AmityChatReportMessageDeletedErrorContent(
    onCloseClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.95f)
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.amity_ic_chat_report_reason_error),
            contentDescription = "error",
            modifier = Modifier.size(60.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = amityChatString("chat.report.error.title"),
            style = AmityTheme.typography.headLine,
            color = AmityTheme.colors.baseShade3,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = amityChatString("chat.report.error.desc"),
            style = AmityTheme.typography.body,
            color = AmityTheme.colors.baseShade3,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(Modifier.height(1.dp)
            .fillMaxWidth()
            .background(AmityTheme.colors.divider))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AmityTheme.colors.primary,
            ),
        ) {
            Text(
                text = amityChatString("chat.report.error.close"),
                style = AmityTheme.typography.bodyBold,
                color = amityColorWhite,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
