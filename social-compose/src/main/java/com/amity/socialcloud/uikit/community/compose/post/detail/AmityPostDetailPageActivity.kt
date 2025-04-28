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
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityLivestreamDeletedPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityLivestreamTerminatedPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle

class AmityPostDetailPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val id = intent.getStringExtra(EXTRA_PARAM_POST_ID) ?: ""
        val category = intent.getStringExtra(EXTRA_PARAM_POST_CATEGORY) ?: "GENERAL"
        val hideTarget = intent.getBooleanExtra(EXTRA_PARAM_HIDE_TARGET, true)
        val showLivestreamPostExceeded =
            intent.getBooleanExtra(EXTRA_PARAM_SHOW_LIVESTREAM_POST_EXCEEDED, false)

        val commentId = intent.getStringExtra(EXTRA_PARAM_COMMENT_ID)
        val parentId = intent.getStringExtra(EXTRA_PARAM_PARENT_ID)

        setContent {
            AmityPostDetailPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                id = id,
                style = AmityPostContentComponentStyle.DETAIL,
                category = AmityPostCategory.fromString(category),
                hideTarget = hideTarget,
                showLivestreamPostExceeded = showLivestreamPostExceeded,
                commentId = commentId,
                parentId = parentId,
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VIEW_LIVESTREAM && resultCode == RESULT_OK) {
            val data = data?.getStringExtra(EXTRA_PARAM_LIVESTREAM_ERROR_TYPE) ?: ""
            val errorType = LivestreamErrorScreenType.fromString(data)

            when (errorType) {
                LivestreamErrorScreenType.TERMINATED -> {
                    startActivity(
                        AmityLivestreamTerminatedPageActivity.newIntent(
                            context = this,
                            screenType = LivestreamScreenType.WATCH
                        )
                    )
                    finish()
                }

                LivestreamErrorScreenType.DELETED -> {
                    startActivity(AmityLivestreamDeletedPageActivity.newIntent(context = this))
                    finish()
                }

                else -> {}
            }
        }
    }

    companion object {
        private const val EXTRA_PARAM_POST_ID = "post_id"
        private const val EXTRA_PARAM_POST_CATEGORY = "post_category"
        private const val EXTRA_PARAM_HIDE_TARGET = "hide_target"
        private const val EXTRA_PARAM_SHOW_LIVESTREAM_POST_EXCEEDED =
            "show_livestream_post_exceeded"
        const val EXTRA_PARAM_LIVESTREAM_ERROR_TYPE = "livestream_deleted"
        const val REQUEST_CODE_VIEW_LIVESTREAM = 11010
        private const val EXTRA_PARAM_COMMENT_ID = "comment_id"
        private const val EXTRA_PARAM_PARENT_ID = "parent_id"

        fun newIntent(
            context: Context,
            id: String,
            category: AmityPostCategory = AmityPostCategory.GENERAL,
            hideTarget: Boolean = false,
            showLivestreamPostExceeded: Boolean = false,
            commentId: String? = null,
            parentId : String? = null,
        ): Intent {
            return Intent(
                context,
                AmityPostDetailPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_POST_ID, id)
                putExtra(EXTRA_PARAM_POST_CATEGORY, category.name)
                putExtra(EXTRA_PARAM_HIDE_TARGET, hideTarget)
                putExtra(EXTRA_PARAM_SHOW_LIVESTREAM_POST_EXCEEDED, showLivestreamPostExceeded)
                putExtra(EXTRA_PARAM_COMMENT_ID, commentId)
                putExtra(EXTRA_PARAM_PARENT_ID, parentId)
            }
        }
    }
}