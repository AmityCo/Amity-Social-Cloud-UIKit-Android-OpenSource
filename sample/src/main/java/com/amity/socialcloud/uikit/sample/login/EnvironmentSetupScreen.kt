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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PageBg = Color(0xFFF2F2F7)
private val CardBg = Color.White
private val Ink = Color(0xFF1C1C1E)
private val Muted = Color(0xFF8E8E93)
private val Label = Color(0xFF6D6D72)
private val Divider = Color(0xFFE5E5EA)
private val ClearBg = Color(0xFFC7C7CC)
private val AccentBg = Color(0xFF1C1C1E)
private val AccentText = Color.White
private val ErrorRed = Color(0xFFE5484D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvironmentSetupScreen(
    viewModel: LoginViewModel,
    onAdvancedClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    buildVersionName: String = "",
    buildVersionCode: Int = 0,
) {
    val config by viewModel.config.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val envChanged = viewModel.envHasChanged()
    var apiKeyVisible by remember { mutableStateOf(false) }
    var regionExpanded by remember { mutableStateOf(false) }
    var userTypeExpanded by remember { mutableStateOf(false) }

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
            Text(
                text = "Android UI-Kit",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                modifier = Modifier.padding(start = 18.dp, top = 16.dp, bottom = 12.dp),
            )

            // USER section
            GroupLabel("User")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                // User ID
                FieldRow(
                    label = "User ID",
                    value = config.userId,
                    placeholder = LoginViewModel.DEFAULT_USER_ID,
                    onValueChange = { viewModel.updateUserId(it) },
                    trailingContent = {
                        if (config.userId.isNotEmpty()) {
                            ClearButton { viewModel.updateUserId("") }
                        }
                    },
                )
                RowDivider()

                // Display Name
                FieldRow(
                    label = "Display Name (Optional)",
                    value = config.displayName,
                    placeholder = "Optional — leave blank to omit",
                    onValueChange = { viewModel.updateDisplayName(it) },
                )
                RowDivider()

                // User Type dropdown
                ExposedDropdownMenuBox(
                    expanded = userTypeExpanded,
                    onExpandedChange = { userTypeExpanded = it },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("User Type", fontSize = 12.sp, color = Label)
                            Text(config.userType.label, fontSize = 15.sp, color = Ink)
                        }
                        Text("⌄", fontSize = 16.sp, color = Muted)
                    }
                    ExposedDropdownMenu(
                        expanded = userTypeExpanded,
                        onDismissRequest = { userTypeExpanded = false },
                    ) {
                        UserType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.label) },
                                onClick = {
                                    viewModel.updateUserType(type)
                                    userTypeExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            // NETWORK section
            GroupLabel("Network")
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                // API Region dropdown
                ExposedDropdownMenuBox(
                    expanded = regionExpanded,
                    onExpandedChange = { regionExpanded = it },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("API Region", fontSize = 12.sp, color = Label)
                            Text(config.apiRegion.label, fontSize = 15.sp, color = Ink)
                        }
                        Text("⌄", fontSize = 16.sp, color = Muted)
                    }
                    ExposedDropdownMenu(
                        expanded = regionExpanded,
                        onDismissRequest = { regionExpanded = false },
                    ) {
                        ApiRegion.entries.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(region.label) },
                                onClick = {
                                    viewModel.updateApiRegion(region)
                                    regionExpanded = false
                                },
                            )
                        }
                    }
                }
                RowDivider()

                // API Key (masked)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("API Key (linked to region)", fontSize = 12.sp, color = Label)
                        if (apiKeyVisible) {
                            BasicTextField(
                                value = config.apiKey,
                                onValueChange = { viewModel.updateApiKey(it) },
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Ink,
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        } else {
                            Text(
                                "•••••••••••••••••••",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Ink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "👁",
                            fontSize = 15.sp,
                            modifier = Modifier.clickable { apiKeyVisible = !apiKeyVisible },
                        )
                        ClearButton { viewModel.resetApiKey() }
                    }
                }
                RowDivider()

                // Upload URL
                FieldRow(
                    label = "Upload URL (linked to region)",
                    value = config.uploadUrl,
                    placeholder = "",
                    onValueChange = { viewModel.updateUploadUrl(it) },
                    isMono = true,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Advanced options card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .clickable(onClick = onAdvancedClick),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(Color(0xFFF0F0F3)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("⚙", fontSize = 14.sp)
                        }
                        Text("Advanced options…", fontSize = 15.sp, color = Ink)
                    }
                    Text("›", fontSize = 18.sp, color = Color(0xFFC0C0C6))
                }
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
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            if (buildVersionName.isNotEmpty()) {
                Text(
                    text = "Build $buildVersionName ($buildVersionCode) · ${config.apiRegion.label.lowercase()}",
                    fontSize = 11.sp,
                    color = Color(0xFFAEB4BD),
                    modifier = Modifier.padding(top = 5.dp, bottom = 8.dp),
                )
            }
        }
    }
}

@Composable
fun GroupLabel(title: String) {
    Text(
        text = title.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Label,
        letterSpacing = 0.3.sp,
        modifier = Modifier.padding(start = 18.dp, top = 14.dp, bottom = 7.dp),
    )
}

@Composable
fun FieldRow(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isMono: Boolean = false,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, color = Label)
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = if (isMono) 13.sp else 15.sp,
                    fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default,
                    color = Ink,
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                placeholder,
                                fontSize = if (isMono) 13.sp else 15.sp,
                                color = Color(0xFFB8B8BE),
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        trailingContent?.invoke()
    }
}

@Composable
fun ClearButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(ClearBg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text("✕", fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun RowDivider() {
    HorizontalDivider(
        color = Divider,
        thickness = 1.dp,
        modifier = Modifier.padding(start = 14.dp),
    )
}
