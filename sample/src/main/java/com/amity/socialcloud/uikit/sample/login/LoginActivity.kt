package com.amity.socialcloud.uikit.sample.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageActivity
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageActivity
import com.amity.socialcloud.uikit.sample.liveChat.AmityLiveChatListActivity

class LoginActivity : ComponentActivity() {

    private lateinit var viewModel: LoginViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "App can't send notifications", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        checkNotificationPermission()

        val versionName = try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: ""
        } catch (_: Exception) { "" }

        val versionCode = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, 0).longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0).versionCode
            }
        } catch (_: Exception) { 0 }

        val hasSession = AmityCoreClient.getUserId().isNotEmpty()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    var currentScreen by rememberSaveable { mutableStateOf(
                        if (hasSession) Screen.SELECT_MODULE else Screen.ENVIRONMENT_SETUP
                    ) }

                    when (currentScreen) {
                        Screen.ENVIRONMENT_SETUP -> EnvironmentSetupScreen(
                            viewModel = viewModel,
                            onAdvancedClick = { currentScreen = Screen.ADVANCED },
                            onLoginSuccess = { currentScreen = Screen.SELECT_MODULE },
                            buildVersionName = versionName,
                            buildVersionCode = versionCode,
                        )
                        Screen.ADVANCED -> AdvancedScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.ENVIRONMENT_SETUP },
                            onLoginSuccess = { currentScreen = Screen.SELECT_MODULE },
                        )
                        Screen.CHAT_MODULE -> ChatModuleScreen(
                            onChatV4Click = {
                                startActivity(
                                    Intent(this@LoginActivity, AmityChatHomePageActivity::class.java)
                                )
                            },
                            onLiveChatClick = {
                                startActivity(
                                    Intent(this@LoginActivity, AmityLiveChatListActivity::class.java)
                                )
                            },
                            onBack = { currentScreen = Screen.SELECT_MODULE },
                        )
                        Screen.SELECT_MODULE -> SelectModuleScreen(
                            viewModel = viewModel,
                            onChatClick = { currentScreen = Screen.CHAT_MODULE },
                            onSocialClick = {
                                startActivity(
                                    Intent(this@LoginActivity, AmitySocialHomePageActivity::class.java)
                                )
                            },
                            onChangeUser = { currentScreen = Screen.ENVIRONMENT_SETUP },
                            onLoggedOut = { currentScreen = Screen.ENVIRONMENT_SETUP },
                        )
                    }
                }
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val status = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            )
            if (status != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    enum class Screen {
        ENVIRONMENT_SETUP,
        ADVANCED,
        SELECT_MODULE,
        CHAT_MODULE,
    }
}
