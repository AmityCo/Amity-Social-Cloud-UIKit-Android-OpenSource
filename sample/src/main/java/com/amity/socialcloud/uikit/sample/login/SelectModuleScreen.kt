package com.amity.socialcloud.uikit.sample.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PageBg = Color(0xFFF2F2F7)
private val CardBg = Color.White
private val Ink = Color(0xFF1C1C1E)
private val Muted = Color(0xFF8E8E93)
private val Label = Color(0xFF6D6D72)
private val Divider = Color(0xFFE5E5EA)
private val AccentBg = Color(0xFF1C1C1E)
private val AccentText = Color.White
private val GreenDot = Color(0xFF34C759)
private val BadgeBg = Color(0xFFE3F6E8)
private val BadgeText = Color(0xFF1F7A3D)
private val ChipBg = Color(0xFFF0F0F3)

@Composable
fun SelectModuleScreen(
    viewModel: LoginViewModel,
    onChatClick: () -> Unit,
    onSocialClick: () -> Unit,
    onChangeUser: () -> Unit,
    onLoggedOut: () -> Unit,
) {
    val config by viewModel.config.collectAsState()
    val loggedInUserId by viewModel.loggedInUserId.collectAsState()
    val context = LocalContext.current

    val displayUser = config.displayName.trim().ifEmpty {
        config.userId.trim().ifEmpty { loggedInUserId.ifEmpty { LoginViewModel.DEFAULT_USER_ID } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Select Module",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                modifier = Modifier.padding(start = 18.dp, top = 16.dp, bottom = 12.dp),
            )

            // User info card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                ) {
                    Row {
                        Text("Logged in as ", fontSize = 15.sp, color = Color(0xFF3A3A3C))
                        Text(displayUser, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Ink)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // Environment badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(7.dp))
                            .background(BadgeBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GreenDot)
                        )
                        Text(
                            config.apiRegion.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BadgeText,
                        )
                        Text(
                            "› edit",
                            fontSize = 12.sp,
                            color = Muted,
                            modifier = Modifier.clickable(onClick = onChangeUser),
                        )
                    }
                }
            }

            // AVAILABLE MODULES
            GroupLabel("Available Modules")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                ModuleRow(
                    icon = "💬",
                    title = "Chat",
                    subtitle = "Tap to enter →",
                    onClick = onChatClick,
                )
                RowDivider()
                ModuleRow(
                    icon = "👥",
                    title = "Social",
                    subtitle = "Tap to enter →",
                    onClick = onSocialClick,
                )
            }

            // DEBUG section
            GroupLabel("Debug")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(if (config.syncNetworkConfig) 1f else 0.4f)
                        .let { mod ->
                            if (config.syncNetworkConfig) {
                                mod.clickable {
                                    viewModel.resyncNetworkConfig(
                                        onComplete = {
                                            Toast
                                                .makeText(context, "Config synced", Toast.LENGTH_SHORT)
                                                .show()
                                        },
                                        onError = { msg ->
                                            Toast
                                                .makeText(context, "Sync failed: $msg", Toast.LENGTH_SHORT)
                                                .show()
                                        },
                                    )
                                }
                            } else mod
                        }
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Re-sync Network Config", fontSize = 15.sp, color = Ink)
                    Text("Enabled when Sync = ON", fontSize = 11.sp, color = Muted)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Change User card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .clickable(onClick = onChangeUser),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Change User", fontSize = 15.sp, color = Ink)
                    Text("›", fontSize = 18.sp, color = Color(0xFFC0C0C6))
                }
            }
        }

        // Pinned footer — logout buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PageBg)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.logout()
                    onLoggedOut()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink),
            ) {
                Text(
                    "Log out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
            Button(
                onClick = {
                    viewModel.secureLogout()
                    onLoggedOut()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBg,
                    contentColor = AccentText,
                ),
            ) {
                Text(
                    "Secure log out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun ModuleRow(
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
