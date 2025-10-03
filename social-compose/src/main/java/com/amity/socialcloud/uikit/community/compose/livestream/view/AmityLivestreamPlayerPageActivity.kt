package com.amity.socialcloud.uikit.community.compose.livestream.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.social.post.AmityPost

class AmityLivestreamPlayerPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PARAM_POST, AmityPost::class.java)
        }
        else{
            intent.getParcelableExtra(EXTRA_PARAM_POST) as? AmityPost
        }


        setContent {
            if (post != null) {
                AmityLivestreamPlayerPage(
                    modifier = Modifier,
                    post = post,
                )
            }
        }
    }

    companion object {
        private const val EXTRA_PARAM_POST = "EXTRA_PARAM_POST"

        fun newIntent(
            context: Context,
            post: AmityPost,
        ): Intent {
            return Intent(
                context,
                AmityLivestreamPlayerPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_POST, post)
            }
        }
    }
}