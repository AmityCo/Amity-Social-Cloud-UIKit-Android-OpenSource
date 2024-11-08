package com.amity.socialcloud.uikit.community.compose.post.composer.poll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost


class AmityPollPostComposerPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val targetId = intent.getStringExtra(EXTRA_PARAM_TARGET_ID) ?: ""
        val targetType =
            intent.getSerializableExtra(EXTRA_PARAM_TARGET_TYPE) as AmityPost.TargetType

        val targetCommunity =
            intent.getParcelableExtra(EXTRA_PARAM_TARGET_COMMUNITY) as AmityCommunity?

        setContent {
            AmityPollPostComposerPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                targetId = targetId,
                targetType = targetType,
                targetCommunity = targetCommunity

            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_TARGET_ID = "target_id"
        private const val EXTRA_PARAM_TARGET_TYPE = "target_type"
        private const val EXTRA_PARAM_TARGET_COMMUNITY = "target_community"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityPost.TargetType,
            community: AmityCommunity? = null,
        ): Intent {
            return Intent(
                context,
                AmityPollPostComposerPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_TARGET_TYPE, targetType)
                putExtra(EXTRA_PARAM_TARGET_COMMUNITY, community)
            }
        }
    }

}