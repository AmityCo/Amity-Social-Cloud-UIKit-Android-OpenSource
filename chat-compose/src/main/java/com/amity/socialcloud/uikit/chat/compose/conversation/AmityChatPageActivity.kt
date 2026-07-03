package com.amity.socialcloud.uikit.chat.compose.conversation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier

class AmityChatPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID) ?: return finish()
        val jumpToMessageId = intent.getStringExtra(EXTRA_JUMP_TO_MESSAGE_ID)

        setContent {
            AmityChatPage(
                modifier = Modifier
                    .statusBarsPadding(),
                channelId = channelId,
                jumpToMessageId = jumpToMessageId,
            )
        }
    }

    companion object {
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val EXTRA_JUMP_TO_MESSAGE_ID = "EXTRA_JUMP_TO_MESSAGE_ID"

        fun newIntent(context: Context, channelId: String, jumpToMessageId: String? = null): Intent {
            return Intent(context, AmityChatPageActivity::class.java).apply {
                putExtra(EXTRA_CHANNEL_ID, channelId)
                if (jumpToMessageId != null) putExtra(EXTRA_JUMP_TO_MESSAGE_ID, jumpToMessageId)
            }
        }
    }
}
