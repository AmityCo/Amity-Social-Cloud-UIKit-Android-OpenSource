package com.amity.socialcloud.uikit.chat.compose.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme

class AmityEditGroupMemberPermissionsPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID) ?: run {
            finish()
            return
        }

        setContent {
            AmityComposeTheme {
                AmityEditGroupMemberPermissionsPage(
                    modifier = Modifier
                        .statusBarsPadding()
                        .systemBarsPadding(),
                    channelId = channelId
                )
            }
        }
    }

    companion object {
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        fun newIntent(context: Context, channelId: String): Intent {
            return Intent(context, AmityEditGroupMemberPermissionsPageActivity::class.java).apply {
                putExtra(EXTRA_CHANNEL_ID, channelId)
            }
        }
    }
}
