package com.amity.socialcloud.uikit.chat.compose.home.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite

@Composable
fun AmityChatListEmptyState(
    modifier: Modifier = Modifier,
    onCreateChatClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_empty),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = amityChatString("chat.home.empty.title"),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = AmityTheme.colors.baseShade3,
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = amityChatString("chat.home.empty.description"),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontSize = 13.sp,
                color = AmityTheme.colors.baseShade3,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCreateChatClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AmityTheme.colors.primary,
                contentColor = amityColorWhite,
            ),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_home_create),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = amityColorWhite,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = amityChatString("chat.create.new.chat"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = amityColorWhite,
                    ),
                )
            }
        }
    }
}
