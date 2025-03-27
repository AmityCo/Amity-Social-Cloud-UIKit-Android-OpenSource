package com.amity.socialcloud.uikit.chat.compose.live.composer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityMessageAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityMessageComposeReplyLabel(
    modifier: Modifier = Modifier,
    parentMessage: AmityMessage?,
    onDismiss: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(color = AmityTheme.colors.baseShade4)
            .padding(start = 16.dp, top = 0.dp, end = 12.dp, bottom = 0.dp)
    ) {
        val creator = parentMessage?.getCreator()
        val text = buildAnnotatedString {
            append("Replying to ")
            append(creator?.getDisplayName() ?: "")
        }

        val data = parentMessage?.getData()
        val parentText = if (data is AmityMessage.Data.TEXT) data.getText() else ""

        AmityMessageAvatarView(
            avatarUrl = creator?.getAvatar()?.getUrl() ?: "",
            modifier = Modifier.size(32.dp)
        )

        Column(
            modifier = modifier
                .weight(1f)
                .padding(start = 16.dp, end = 12.dp)
        ) {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.baseShade1,
                ),
            )
            Spacer(modifier = modifier.height(2.dp))
            Text(
                text = parentText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.colors.baseShade1,
                ),
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.amity_ic_close),
            contentDescription = null,
            tint = AmityTheme.colors.secondaryShade2,
            modifier = modifier
                .size(16.dp)
                .clickable { onDismiss() }
                .padding(2.dp)
        )
    }
}

@Preview
@Composable
fun AmityMessageComposeReplyLabelPreview() {
    AmityMessageComposeReplyLabel(
        parentMessage = null
    ) {}
}