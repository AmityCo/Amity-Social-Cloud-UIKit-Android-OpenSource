package com.amity.socialcloud.uikit.community.compose.post.detail.elements


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText

@Composable
fun AmityPostModeratorBadge(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
) {
    AmityBaseElement(
        componentScope = componentScope,
        elementId = "moderator_badge"
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            modifier = modifier
                .background(
                    color = AmityTheme.colors.primaryShade3,
                    shape = RoundedCornerShape(size = 20.dp)
                )
                .padding(start = 4.dp, end = 6.dp)
                .height(18.dp)
                .testTag(getAccessibilityId()),
        ) {
            Icon(
                painter = painterResource(id = getConfig().getIcon()),
                contentDescription = null,
                tint = AmityTheme.colors.primary,
            )
            Text(
                text = getConfig().getText(),
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 18.sp,
                    color = AmityTheme.colors.primary,
                )
            )
        }
    }
}