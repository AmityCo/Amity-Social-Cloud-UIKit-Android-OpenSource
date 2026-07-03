package com.amity.socialcloud.uikit.chat.compose.message.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Shared "Waiting for network…" row used as a header subtitle whenever
 * the SDK socket connection drops. Mirrors Flutter `_buildWaitingForNetwork`
 * in `lib/v4/chat/home/chat_home_page.dart` and `lib/v4/chat/message/chat_page.dart`.
 */
@Composable
fun AmityChatWaitingForNetworkRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(16.dp)
                .padding(top = 0.dp, bottom = 2.dp),
            color = AmityTheme.colors.baseShade1,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = amityChatString("chat.waiting.for.network"),
            style = AmityTheme.typography.caption.copy(
                color = AmityTheme.colors.baseShade1,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
