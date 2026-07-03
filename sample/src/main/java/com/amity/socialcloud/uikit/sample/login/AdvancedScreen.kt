package com.amity.socialcloud.uikit.sample.login

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val PageBg = Color(0xFFF2F2F7)
private val CardBg = Color.White
private val Ink = Color(0xFF1C1C1E)
private val Muted = Color(0xFF8E8E93)
private val Label = Color(0xFF6D6D72)
private val AccentBg = Color(0xFF1C1C1E)
private val AccentText = Color.White
private val ToggleOff = Color(0xFFE3E3E8)
private val ToggleOn = Color(0xFF1C1C1E)
private val ErrorRed = Color(0xFFE5484D)
private val LinkBlue = Color(0xFF2F6FED)
private val ChipBg = Color(0xFFF0F0F3)

@Composable
fun AdvancedScreen(
    viewModel: LoginViewModel,
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val config by viewModel.config.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val envChanged = viewModel.envHasChanged()
    val context = LocalContext.current

    when (loginState) {
        is LoginState.Success -> {
            viewModel.resetLoginState()
            onLoginSuccess()
        }
        else -> {}
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
            // Nav bar
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
                    "Advanced",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink,
                )
                Spacer(modifier = Modifier.weight(1f))
                // Placeholder for symmetry
                Box(modifier = Modifier.width(48.dp))
            }

            // SECURITY section
            GroupLabel("Security")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                ToggleRow(
                    label = "Secure Mode",
                    checked = config.secureMode,
                    onCheckedChange = { viewModel.updateSecureMode(it) },
                )
            }

            if (config.secureMode) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    modifier = Modifier.padding(horizontal = 14.dp),
                ) {
                    FieldRow(
                        label = "Auth Signature URL",
                        value = config.authSignatureUrl,
                        placeholder = "https://my-server/auth-signature",
                        onValueChange = { viewModel.updateAuthSignatureUrl(it) },
                    )

                    if (config.userType == UserType.VISITOR) {
                        RowDivider()
                        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val displayDate = dateFormatter.format(Date(config.authSignatureExpiresAtMillis))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val cal = Calendar.getInstance().apply {
                                        timeInMillis = config.authSignatureExpiresAtMillis
                                    }
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            TimePickerDialog(
                                                context,
                                                { _, hour, minute ->
                                                    val newCal = Calendar.getInstance().apply {
                                                        set(year, month, day, hour, minute, 0)
                                                        set(Calendar.MILLISECOND, 0)
                                                    }
                                                    viewModel.updateAuthSignatureExpiresAt(newCal.timeInMillis)
                                                },
                                                cal.get(Calendar.HOUR_OF_DAY),
                                                cal.get(Calendar.MINUTE),
                                                true
                                            ).show()
                                        },
                                        cal.get(Calendar.YEAR),
                                        cal.get(Calendar.MONTH),
                                        cal.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                                .padding(horizontal = 14.dp, vertical = 11.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text("Auth Signature Expires At", fontSize = 12.sp, color = Label)
                                Text(displayDate, fontSize = 15.sp, color = Ink)
                            }
                            Text("📅", fontSize = 15.sp)
                        }
                    }
                }
            } else {
                Text(
                    "Auth Signature URL & Expires-At appear here when Secure Mode is ON",
                    fontSize = 11.sp,
                    color = Color(0xFFB8B8BE),
                    modifier = Modifier.padding(start = 18.dp, top = 6.dp),
                )
            }

            // BEHAVIOUR section
            GroupLabel("Behaviour")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                ToggleRow(
                    label = "Visitor Can View Clip",
                    subtitle = "All platforms except React Native",
                    checked = config.visitorCanViewClip,
                    onCheckedChange = { viewModel.updateVisitorCanViewClip(it) },
                )
                RowDivider()
                ToggleRow(
                    label = "Hide Explore",
                    checked = config.hideExplore,
                    onCheckedChange = { viewModel.updateHideExplore(it) },
                )
                RowDivider()
                ToggleRow(
                    label = "Social Community Creation Button",
                    checked = config.socialCommunityCreationButtonVisible,
                    onCheckedChange = { viewModel.updateSocialCommunityCreation(it) },
                )
            }

            // APPEARANCE section
            GroupLabel("Appearance")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                Text(
                    "Theme",
                    fontSize = 12.sp,
                    color = Label,
                    modifier = Modifier.padding(start = 14.dp, top = 11.dp),
                )
                // Segmented control
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    AppTheme.entries.forEach { theme ->
                        val isActive = config.theme == theme
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isActive) AccentBg else ChipBg)
                                .clickable { viewModel.updateTheme(theme) }
                                .padding(vertical = 9.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                theme.label,
                                fontSize = 14.sp,
                                color = if (isActive) AccentText else Ink,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            )
                        }
                    }
                }

                ToggleRow(
                    label = "Sync Network Config",
                    subtitle = "Applied after login — overrides settings above when ON",
                    checked = config.syncNetworkConfig,
                    onCheckedChange = { viewModel.updateSyncNetworkConfig(it) },
                )
            }
        }

        // Pinned footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PageBg)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { viewModel.login() },
                enabled = loginState !is LoginState.Loading && config.apiKey.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBg,
                    contentColor = AccentText,
                ),
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = AccentText,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        if (envChanged) "Apply & Log in →" else "Log in →",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                }
            }

            if (loginState is LoginState.Error) {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = ErrorRed,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
            } else {
                Text(
                    text = "Becomes \"Apply & Log in →\" when the environment changes",
                    fontSize = 11.sp,
                    color = Muted,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                )
            }
        }
    }
}

@Composable
fun ToggleRow(
    label: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(label, fontSize = 15.sp, color = Ink)
            if (subtitle != null) {
                Text(subtitle, fontSize = 11.sp, color = Muted, modifier = Modifier.padding(top = 3.dp))
            }
        }
        // Custom toggle matching the mockup style
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(26.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(if (checked) ToggleOn else ToggleOff)
                .clickable { onCheckedChange(!checked) },
        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(22.dp)
                    .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                    .clip(RoundedCornerShape(11.dp))
                    .background(Color.White),
            )
        }
    }
}
