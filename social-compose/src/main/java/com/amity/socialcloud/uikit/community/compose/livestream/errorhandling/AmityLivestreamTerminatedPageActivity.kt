package com.amity.socialcloud.uikit.community.compose.livestream.errorhandling

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType

class AmityLivestreamTerminatedPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenTypeString = intent.getStringExtra(LIVE_STREAM_SCREEN_TYPE) ?: ""
        val screenType =
            LivestreamScreenType.fromString(screenTypeString) ?: LivestreamScreenType.CREATE

        enableEdgeToEdge()

        setContent {
            AmityLivestreamTerminatedPage(livestreamScreenType = screenType)
        }
    }

    companion object {

        private const val LIVE_STREAM_SCREEN_TYPE = "live_stream_screen_type"

        fun newIntent(
            context: Context,
            screenType: LivestreamScreenType
        ): Intent {
            return Intent(
                context,
                AmityLivestreamTerminatedPageActivity::class.java
            ).apply {
                putExtra(LIVE_STREAM_SCREEN_TYPE, screenType.name)
            }
        }
    }
}