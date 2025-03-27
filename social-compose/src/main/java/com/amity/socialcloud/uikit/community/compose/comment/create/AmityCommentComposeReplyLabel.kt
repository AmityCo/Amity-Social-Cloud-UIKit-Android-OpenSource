package com.amity.socialcloud.uikit.community.compose.comment.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityCommentComposeReplyLabel(
    modifier: Modifier = Modifier,
    displayName: String,
    onClose: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = AmityTheme.colors.baseShade4)
            .padding(start = 16.dp, top = 10.dp, end = 12.dp, bottom = 10.dp)
    ) {
        val text = buildAnnotatedString {
            append("Replying to ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append(displayName)
            }
        }

        Text(
            text = text,
            style = AmityTheme.typography.bodyLegacy.copy(
                color = AmityTheme.colors.baseShade1,
            ),
        )

        Icon(
            painter = painterResource(id = R.drawable.amity_ic_close),
            contentDescription = null,
            tint = AmityTheme.colors.baseShade2,
            modifier = modifier
                .size(16.dp)
                .clickable { onClose() }
                .padding(2.dp)
        )
    }
}

@Preview
@Composable
fun AmityCommentComposeReplyLabelPreview() {
    AmityCommentComposeReplyLabel(
        displayName = "Johnie Burke"
    ) {}
}
