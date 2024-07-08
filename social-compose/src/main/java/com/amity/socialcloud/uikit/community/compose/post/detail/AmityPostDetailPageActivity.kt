package com.amity.socialcloud.uikit.community.compose.post.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

class AmityPostDetailPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val postId = intent.getStringExtra(EXTRA_PARAM_POST_ID) ?: ""

        setContent {
            AmityPostDetailPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                postId = postId,
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_POST_ID = "post_id"

        fun newIntent(
            context: Context,
            postId: String
        ): Intent {

            return Intent(
                context,
                AmityPostDetailPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_POST_ID, postId)
            }
        }
    }
}