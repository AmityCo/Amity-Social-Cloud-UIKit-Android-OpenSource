package com.amity.socialcloud.uikit.community.compose.livestream.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityBaseWarningPage(
    pageId: String,
    iconRes: Int,
    title: String,
    description: String,
    buttonText: String,
    onOkClick: () -> Unit = {}
) {
    AmityBasePage(pageId = pageId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
                .background(AmityTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // App Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Live stream",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(AmityTheme.colors.divider)
            ) {}

            // Main content area - centered
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Warning icon
                    Box(
                        modifier = Modifier
                            .size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = iconRes),
                            contentDescription = "Warning Icon",
                            modifier = Modifier
                                .size(56.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Main message
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = AmityTheme.colors.base,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Subtitle message
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        color = AmityTheme.colors.baseShade1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }


            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(AmityTheme.colors.divider)
            ) {}

            // OK Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onOkClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun AmityLivestreamBannedPage(
    onOkClick: () -> Unit = {}
) {
    AmityBaseWarningPage(
        pageId = "live_stream_banned_page",
        iconRes = R.drawable.amity_ic_base_warning,
        title =  "You've been banned.",
        description = "You can no longer access this live stream.", buttonText = "OK",
        onOkClick = onOkClick,
    )
}

@Composable
fun AmityLivestreamDeclinedPage(
    onOkClick: () -> Unit = {}
) {
    AmityBaseWarningPage(
        pageId = "live_stream_declined_page",
        iconRes = R.drawable.amity_ic_livestream_unavailable,
        title =  "Something went wrong",
        description = "The content you're looking for is unavailable.", buttonText = "Go back",
        onOkClick = onOkClick,
    )
}

// Usage example
@Preview(showBackground = true)
@Composable
fun AmityLivestreamBannedPagePreview() {
    AmityLivestreamBannedPage()
}