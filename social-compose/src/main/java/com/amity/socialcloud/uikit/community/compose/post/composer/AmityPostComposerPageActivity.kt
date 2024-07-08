package com.amity.socialcloud.uikit.community.compose.post.composer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat

class AmityPostComposerPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val options: AmityPostComposerOptions =
            intent?.extras?.getParcelable(EXTRA_OPTIONS) ?: return

        setContent {
            AmityPostComposerPage(
                options = options,
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
            )
        }
    }

    companion object {
        private const val EXTRA_OPTIONS = "options"

        fun newIntent(
            context: Context,
            options: AmityPostComposerOptions,
        ): Intent {
            return Intent(
                context,
                AmityPostComposerPageActivity::class.java
            ).apply {
                putExtra(EXTRA_OPTIONS, options)
            }
        }
    }
}