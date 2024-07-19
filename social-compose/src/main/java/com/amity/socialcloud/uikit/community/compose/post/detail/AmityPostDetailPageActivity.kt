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
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle

class AmityPostDetailPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val id = intent.getStringExtra(EXTRA_PARAM_POST_ID) ?: ""
        val category = intent.getStringExtra(EXTRA_PARAM_POST_CATEGORY) ?: "GENERAL"
        val hideTarget = intent.getBooleanExtra(EXTRA_PARAM_HIDE_TARGET, true)

        setContent {
            AmityPostDetailPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                id = id,
                style = AmityPostContentComponentStyle.DETAIL,
                category = AmityPostCategory.fromString(category),
                hideTarget = hideTarget,
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_POST_ID = "post_id"
        private const val EXTRA_PARAM_POST_CATEGORY = "post_category"
        private const val EXTRA_PARAM_HIDE_TARGET = "hide_target"

        fun newIntent(
            context: Context,
            id: String,
            category: AmityPostCategory = AmityPostCategory.GENERAL,
            hideTarget: Boolean = false,
        ): Intent {
            return Intent(
                context,
                AmityPostDetailPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_POST_ID, id)
                putExtra(EXTRA_PARAM_POST_CATEGORY, category.name)
                putExtra(EXTRA_PARAM_HIDE_TARGET, hideTarget)
            }
        }
    }
}