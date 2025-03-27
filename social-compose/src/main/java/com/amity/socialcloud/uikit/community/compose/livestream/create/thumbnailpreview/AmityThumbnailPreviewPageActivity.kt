package com.amity.socialcloud.uikit.community.compose.livestream.create.thumbnailpreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class AmityThumbnailPreviewPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaUri = intent.getStringExtra(EXTRA_PARAM_MEDIA_URI) ?: ""

        setContent {
            AmityThumbnailPreviewPage(
                mediaUri = mediaUri
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_MEDIA_URI = "media_uri"

        fun newIntent(
            context: Context,
            mediaUri: String,
        ): Intent {
            return Intent(
                context,
                AmityThumbnailPreviewPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_MEDIA_URI, mediaUri)
            }
        }
    }
}