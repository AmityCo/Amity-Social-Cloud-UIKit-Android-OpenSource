package com.amity.socialcloud.uikit.community.compose.livestream.errorhandling

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityPostErrorPage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    AmityBasePage(pageId = "") {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .systemBarsPadding()
                .background(color = Color.White)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_livestream_unavailable),
                    contentDescription = null,
                    tint = AmityTheme.colors.baseShade4,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.amity_v4_livestream_deleted_page_title),
                    style = AmityTheme.typography.headLine.copy(
                        color = AmityTheme.colors.baseShade3,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.amity_v4_livestream_deleted_page_desc),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.baseShade3,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.primary,
                    ),
                    onClick = {
                        context.closePageWithResult(Activity.RESULT_OK)
                    }
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Go back",
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}