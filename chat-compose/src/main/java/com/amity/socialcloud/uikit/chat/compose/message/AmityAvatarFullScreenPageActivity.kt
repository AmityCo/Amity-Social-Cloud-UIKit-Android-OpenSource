package com.amity.socialcloud.uikit.chat.compose.message

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.chat.compose.live.elements.AmityAvatarFullScreenContent

class AmityAvatarFullScreenPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // The viewer is edge-to-edge black; without this the window paints the theme's light
        // background for a frame before Compose draws.
        window.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)

        setContent {
            AmityAvatarFullScreenContent(
                avatarUrl = avatarUrl,
                onDismiss = { finish() },
            )
        }
    }

    companion object {
        private const val EXTRA_AVATAR_URL = "EXTRA_AVATAR_URL"

        fun newIntent(context: Context, avatarUrl: String?): Intent {
            return Intent(context, AmityAvatarFullScreenPageActivity::class.java).apply {
                putExtra(EXTRA_AVATAR_URL, avatarUrl)
            }
        }
    }
}
