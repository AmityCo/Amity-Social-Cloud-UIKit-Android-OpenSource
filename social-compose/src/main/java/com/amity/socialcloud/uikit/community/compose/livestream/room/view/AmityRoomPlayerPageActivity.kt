package com.amity.socialcloud.uikit.community.compose.livestream.room.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.post.AmityPost

class AmityRoomPlayerPageActivity : AppCompatActivity() {

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PARAM_POST, AmityPost::class.java)
        }
        else{
            intent.getParcelableExtra(EXTRA_PARAM_POST) as? AmityPost
        }
        val fromInvitation = intent.getBooleanExtra(FROM_INVITATION, false)


        setContent {
            if (post != null) {
                AmityRoomPlayerPage(
                    modifier = Modifier,
                    post = post,
                    fromInvitation = fromInvitation,
                )
            }
        }
    }

    companion object {
        private const val EXTRA_PARAM_POST = "EXTRA_PARAM_POST"
        private const val FROM_INVITATION = "FROM_INVITATION"

        fun newIntent(
            context: Context,
            post: AmityPost,
            fromInvitation: Boolean = false,
        ): Intent {
            return Intent(
                context,
                AmityRoomPlayerPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_POST, post)
                putExtra(FROM_INVITATION, fromInvitation)
            }
        }
    }
}