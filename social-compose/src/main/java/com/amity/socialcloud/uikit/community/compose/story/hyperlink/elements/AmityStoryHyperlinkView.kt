package com.amity.socialcloud.uikit.community.compose.story.hyperlink.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme

@Composable
fun AmityStoryHyperlinkView(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .widthIn(min = 0.dp, max = 300.dp)
            .background(
                color = Color(0xCCFFFFFF),
                shape = RoundedCornerShape(size = 24.dp)
            )
            .clickable { onClick() }
            .padding(start = 12.dp, top = 10.dp, end = 16.dp, bottom = 10.dp)
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.amity_ic_hyperlink
            ),
            contentDescription = null,
            tint = AmityTheme.colors.primary
        )
        Text(
            text = text,
            style = AmityTheme.typography.body.copy(
                color = AmityTheme.colors.secondary,
                fontWeight = FontWeight.SemiBold,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF123456)
@Composable
fun AmityStoryHyperlinkViewPreview() {
    AmityStoryHyperlinkView(
        text = "amity.co"
    ) {}
}