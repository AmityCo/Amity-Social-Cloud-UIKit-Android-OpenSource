package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple

@Composable
fun AmityUserPendingRequestButton(
    modifier: Modifier = Modifier,
    pendingCount: Int,
    onClick: () -> Unit,
) {
    Row(modifier = Modifier.padding(bottom = 12.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(
                    color = AmityTheme.colors.baseShade4,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .clickableWithoutRipple {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(6.dp)
                            .height(6.dp)
                            .background(
                                color = AmityTheme.colors.primary,
                                shape = RoundedCornerShape(size = 3.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "New follow requests",
                        style = TextStyle(
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(600),
                            color = AmityTheme.colors.base
                        )
                    )
                }
                Text(
                    text = "$pendingCount requests need your approval",
                    style = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight(400),
                        color = AmityTheme.colors.baseShade1,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}