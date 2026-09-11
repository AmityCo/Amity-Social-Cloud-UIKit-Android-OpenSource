package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.livestream.room.util.AmityPipHostActivity

/**
 * Hosts [AmityVideoPlayerPage] full-screen (asDialog = false) for RECORDED livestream rooms so
 * the playback can enter Picture-in-Picture. Regular video posts keep using the in-place Dialog
 * form of [AmityVideoPlayerPage] and are therefore excluded from PiP.
 *
 * PiP wiring mirrors AmityRoomPlayerPageActivity.
 */
class AmityVideoPlayerPageActivity : AmityPipHostActivity() {

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerPipControls()

        val post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PARAM_POST, AmityPost::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PARAM_POST) as? AmityPost
        }
        val recordedUrls = intent.getStringArrayListExtra(EXTRA_PARAM_RECORDED_URLS) ?: arrayListOf()
        currentPostId = post?.getPostId()

        // Re-tapping the same recorded livestream while it plays in a floating window must
        // expand that window rather than start a second playback session.
        if (post != null && expandExistingSessionFor(post.getPostId())) {
            finish()
            return
        }
        val childPosts = post?.getChildren() ?: emptyList()
        val selectedFileId = childPosts.firstOrNull()?.getPostId() ?: ""

        setContent {
            AmityVideoPlayerPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                childPosts = childPosts,
                selectedFileId = selectedFileId,
                showMenuButton = true,
                recordedUrls = recordedUrls,
                asDialog = false,
                isInPipMode = isInPipMode,
                pipOwnerPostId = currentPostId,
                onDismiss = { finish() },
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_POST = "EXTRA_PARAM_POST"
        private const val EXTRA_PARAM_RECORDED_URLS = "EXTRA_PARAM_RECORDED_URLS"

        fun newIntent(
            context: Context,
            post: AmityPost,
            recordedUrls: List<String>,
        ): Intent {
            return Intent(context, AmityVideoPlayerPageActivity::class.java).apply {
                putExtra(EXTRA_PARAM_POST, post)
                putStringArrayListExtra(EXTRA_PARAM_RECORDED_URLS, ArrayList(recordedUrls))
            }
        }
    }
}
