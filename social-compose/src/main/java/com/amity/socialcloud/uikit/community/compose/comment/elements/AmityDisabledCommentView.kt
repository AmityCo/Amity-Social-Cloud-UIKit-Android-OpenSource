package com.amity.socialcloud.uikit.community.compose.comment.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityDisabledCommentView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.background(AmityTheme.colors.background)
    ) {
        HorizontalDivider(
            color = AmityTheme.colors.divider
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_lock),
                contentDescription = null,
                tint = AmityTheme.colors.baseShade2
            )
            Text(
                text = context.getString(R.string.amity_comments_disabled_message),
                style = AmityTheme.typography.bodyLegacy.copy(
                    color = AmityTheme.colors.baseShade2
                ),
                modifier = Modifier.testTag("comment_tray_component/disabled_text_view")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityDisabledCommentViewPreview() {
    AmityDisabledCommentView()
}