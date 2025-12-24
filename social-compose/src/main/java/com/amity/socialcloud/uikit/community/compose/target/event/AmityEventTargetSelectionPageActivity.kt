package com.amity.socialcloud.uikit.community.compose.target.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

class AmityEventTargetSelectionPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmityEventTargetSelectionPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
            )
        }
    }

    companion object {
        fun newIntent(
            context: Context,
        ): Intent {
            return Intent(
                context,
                AmityEventTargetSelectionPageActivity::class.java
            )
        }
    }
}
