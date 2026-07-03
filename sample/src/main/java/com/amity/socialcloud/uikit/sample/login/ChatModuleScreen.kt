package com.amity.socialcloud.uikit.sample.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PageBg = Color(0xFFF2F2F7)
private val CardBg = Color.White
private val Ink = Color(0xFF1C1C1E)
private val Muted = Color(0xFF8E8E93)
private val ChipBg = Color(0xFFF0F0F3)
private val LinkBlue = Color(0xFF2F6FED)

@Composable
fun ChatModuleScreen(
    onChatV4Click: () -> Unit,
    onLiveChatClick: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "‹ Back",
                fontSize = 15.sp,
                color = LinkBlue,
                modifier = Modifier.clickable(onClick = onBack),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Chat",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink,
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.width(48.dp))
        }

        GroupLabel("Select Chat Type")
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            modifier = Modifier.padding(horizontal = 14.dp),
        ) {
            ChatOptionRow(
                icon = "💬",
                title = "Chat V4",
                subtitle = "Standard messaging",
                onClick = onChatV4Click,
            )
            RowDivider()
            ChatOptionRow(
                icon = "🔴",
                title = "Live Chat",
                subtitle = "Real-time live chat channels",
                onClick = onLiveChatClick,
            )
        }
    }
}

@Composable
private fun ChatOptionRow(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(ChipBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(icon, fontSize = 19.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Ink)
            Text(subtitle, fontSize = 12.sp, color = Muted)
        }
        Text("›", fontSize = 18.sp, color = Color(0xFFC0C0C6))
    }
}
