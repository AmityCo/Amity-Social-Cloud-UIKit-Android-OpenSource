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
        val mentionMetadata = intent.getStringExtra(EXTRA_MENTION_METADATA)
        val mentionedUserIds = intent.getStringArrayListExtra(EXTRA_MENTIONED_USER_IDS).orEmpty()

        setContent {
            AmityChatMessageFullTextPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                displayName = displayName,
                text = text,
                mentionMetadata = mentionMetadata,
                mentionedUserIds = mentionedUserIds,
                onBack = { finish() },
            )
        }
    }

    companion object {
        private const val EXTRA_DISPLAY_NAME = "EXTRA_DISPLAY_NAME"
        private const val EXTRA_TEXT = "EXTRA_TEXT"
        private const val EXTRA_MENTION_METADATA = "EXTRA_MENTION_METADATA"
        private const val EXTRA_MENTIONED_USER_IDS = "EXTRA_MENTIONED_USER_IDS"

        fun newIntent(
            context: Context,
            displayName: String,
            text: String,
            mentionMetadata: String? = null,
            mentionedUserIds: List<String> = emptyList(),
        ): Intent {
            return Intent(context, AmityChatMessageFullTextPageActivity::class.java).apply {
                putExtra(EXTRA_DISPLAY_NAME, displayName)
                putExtra(EXTRA_TEXT, text)
                putExtra(EXTRA_MENTION_METADATA, mentionMetadata)
                putStringArrayListExtra(EXTRA_MENTIONED_USER_IDS, ArrayList(mentionedUserIds))
            }
        }
    }
}
