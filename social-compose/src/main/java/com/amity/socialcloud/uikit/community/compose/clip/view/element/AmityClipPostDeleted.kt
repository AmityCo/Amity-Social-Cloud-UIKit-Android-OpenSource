package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityClipPostDeleted(
    onNextClipClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmityTheme.colors.baseShade1),
    ) {
        Column(
            modifier = Modifier.align(
                Alignment.Center
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.amity_v4_clip_post_deleted),
                contentDescription = "Error Icon",
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickableWithoutRipple {
                        onNextClipClick()
                    }
            )
            Text(
                text = "This clip is no longer available.",
                style = AmityTheme.typography.body,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White
                ),
                onClick = onNextClipClick,
            ) {
                Text(
                    text = "Watch next clip",
                    style = AmityTheme.typography.bodyBold,
                    color = Color.White,
                )
            }
        }

    }
}

@Preview
@Composable
fun AmityClipPostDeletedPreview() {
    AmityClipPostDeleted()
}