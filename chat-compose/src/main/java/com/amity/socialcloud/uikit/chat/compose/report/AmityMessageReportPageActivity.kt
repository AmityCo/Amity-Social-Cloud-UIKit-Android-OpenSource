package com.amity.socialcloud.uikit.chat.compose.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

class AmityMessageReportPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val messageId = intent.getStringExtra(EXTRA_MESSAGE_ID) ?: return finish()

        setContent {
            AmityMessageReportPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                messageId = messageId,
            )
        }
    }

    companion object {
        private const val EXTRA_MESSAGE_ID = "EXTRA_MESSAGE_ID"

        fun newIntent(context: Context, messageId: String): Intent {
            return Intent(context, AmityMessageReportPageActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE_ID, messageId)
            }
        }
    }
}
