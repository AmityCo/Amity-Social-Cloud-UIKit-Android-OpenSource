package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun InteractionButton(
    @DrawableRes icon: Int,
    count: String? = null,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(32.dp)
                .clickableWithoutRipple {
                    onClick()
                }
        )

        Spacer(Modifier.height(4.dp))

        if (count != null) {
            Text(
                text = count,
                color = Color.White,
                style = AmityTheme.typography.captionBold,
            )
        }
    }
}