package com.amity.socialcloud.uikit.community.compose.livestream.room.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.livestream.view.AmityLivestreamDeclinedPage
import com.amity.socialcloud.uikit.community.compose.livestream.room.util.AmityPipHostActivity

class AmityRoomPlayerPageActivity : AmityPipHostActivity() {

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerPipControls()

        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PARAM_POST, AmityPost::class.java)
        }
        else{
            intent.getParcelableExtra(EXTRA_PARAM_POST) as? AmityPost
        }
        val fromInvitation = intent.getBooleanExtra(FROM_INVITATION, false)
        currentPostId = post?.getPostId()

        // Tapping the same livestream post again while it is playing in a floating window
        // must expand that window, not start a second playback session. Handled here rather
        // than at the call sites so it also covers navigation an integrator adds.
        if (post != null && expandExistingSessionFor(post.getPostId())) {
            finish()
            return
        }


        setContent {
            if (post != null) {
                AmityRoomPlayerPage(
                    modifier = Modifier,
                    post = post,
                    fromInvitation = fromInvitation,
                    isInPipMode = isInPipMode,
                )
            } else {
                AmityLivestreamDeclinedPage(
                    onOkClick = {
                        closePageWithResult(Activity.RESULT_OK)
                    }
                )
            }
        }
    }

    companion object {
        private const val EXTRA_PARAM_POST = "EXTRA_PARAM_POST"
        private const val FROM_INVITATION = "FROM_INVITATION"


        fun newIntent(
            context: Context,
            post: AmityPost?,
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
