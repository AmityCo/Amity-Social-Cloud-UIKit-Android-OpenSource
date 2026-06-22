package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@Composable
fun AmityClipLoadStateError(
    onReloadClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmityTheme.colors.secondaryShade1),
    ) {
        Column(
            modifier = Modifier.align(
                Alignment.Center
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.amity_v4_reload_clip),
                contentDescription = "Error Icon",
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickableWithoutRipple {
                        onReloadClick()
                    }
            )
            Text(
                text = amitySocialString("amity_social_label_unable_to_load_clip"),
                style = AmityTheme.typography.body,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

        }
    }
}