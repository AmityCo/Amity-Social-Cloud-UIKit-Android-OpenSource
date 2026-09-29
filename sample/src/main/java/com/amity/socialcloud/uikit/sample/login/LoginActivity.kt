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
import com.amity.socialcloud.uikit.sample.discoverywidget.AmityDiscoveryWidgetTestActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.visitor.AmityVisitorUsageLimitPageActivity
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
                    // The module screen is reachable from before and after login, so
                    // Back has to return where it was opened from rather than to a
                    // fixed screen.
                    var moduleFlagsOrigin by rememberSaveable { mutableStateOf(Screen.ADVANCED) }

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
                            onModuleFlagsClick = {
                                moduleFlagsOrigin = Screen.ADVANCED
                                currentScreen = Screen.MODULE_FLAGS
                            },
                        )
                        Screen.MODULE_FLAGS -> ModuleFlagsScreen(
                            onBack = { currentScreen = moduleFlagsOrigin },
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
                            onModuleFlagsClick = {
                                moduleFlagsOrigin = Screen.SELECT_MODULE
                                currentScreen = Screen.MODULE_FLAGS
                            },
                            onDiscoveryWidgetClick = {
                                startActivity(
                                    AmityDiscoveryWidgetTestActivity.newIntent(this@LoginActivity)
                                )
                            },
                            onUserProfileClick = {
                                startActivity(
                                    AmityUserProfilePageActivity.newIntent(
                                        this@LoginActivity,
                                        AmityCoreClient.getUserId()
                                    )
                                )
                            },
                            onVisitorUsageLimitClick = {
                                startActivity(
                                    AmityVisitorUsageLimitPageActivity.newIntent(this@LoginActivity)
                                )
                            },
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
        MODULE_FLAGS,
    }
}
