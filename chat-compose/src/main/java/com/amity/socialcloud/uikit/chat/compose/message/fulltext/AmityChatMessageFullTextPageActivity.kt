package com.amity.socialcloud.uikit.chat.compose.message.fulltext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

class AmityChatMessageFullTextPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val displayName = intent.getStringExtra(EXTRA_DISPLAY_NAME) ?: ""
        val text = intent.getStringExtra(EXTRA_TEXT) ?: return finish()

        setContent {
            AmityChatMessageFullTextPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                displayName = displayName,
                text = text,
                onBack = { finish() },
            )
        }
    }

    companion object {
        private const val EXTRA_DISPLAY_NAME = "EXTRA_DISPLAY_NAME"
        private const val EXTRA_TEXT = "EXTRA_TEXT"

        fun newIntent(context: Context, displayName: String, text: String): Intent {
            return Intent(context, AmityChatMessageFullTextPageActivity::class.java).apply {
                putExtra(EXTRA_DISPLAY_NAME, displayName)
                putExtra(EXTRA_TEXT, text)
            }
        }
    }
}
