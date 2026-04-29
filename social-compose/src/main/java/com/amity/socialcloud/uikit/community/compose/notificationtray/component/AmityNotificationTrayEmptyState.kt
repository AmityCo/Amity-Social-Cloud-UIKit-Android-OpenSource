package com.amity.socialcloud.uikit.community.compose.notificationtray.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText

@Composable
fun AmityNotificationTrayEmptyState(
    modifier: Modifier,
    pageScope: AmityComposePageScope? = null,
) {
    AmityBaseElement(
        pageScope = pageScope,
        elementId = "empty_notification"
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val iconRes = getConfig().getIcon()
            Image(
                painter = painterResource(
                    if (iconRes != 0) iconRes else R.drawable.amity_ic_notification_tray_empty
                ),
                contentDescription = null
            )
            Spacer(Modifier.height(8.dp))
            val text = getConfig().getText().ifEmpty { "No notifications" }
            Text(
                text = text,
                style = AmityTheme.typography.titleBold,
                color = AmityTheme.colors.baseShade3
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "You're up to date!",
                style = AmityTheme.typography.body,
                color = AmityTheme.colors.baseShade3
            )
        }
    }
}

@Preview
@Composable
fun AmityNotificationPreview() {
    AmityNotificationTrayEmptyState(modifier = Modifier)
}
