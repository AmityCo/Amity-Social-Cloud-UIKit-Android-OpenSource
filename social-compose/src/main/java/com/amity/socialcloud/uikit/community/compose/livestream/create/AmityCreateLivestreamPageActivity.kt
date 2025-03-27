package com.amity.socialcloud.uikit.community.compose.livestream.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost

class AmityCreateLivestreamPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        val targetId = intent.getStringExtra(EXTRA_PARAM_TARGET_ID) ?: ""
        val targetType =
            intent.getSerializableExtra(EXTRA_PARAM_TARGET_TYPE) as AmityPost.TargetType

        val targetCommunity =
            intent.getParcelableExtra(EXTRA_PARAM_TARGET_COMMUNITY) as AmityCommunity?

        setContent {
            AmityCreateLivestreamPage(
                modifier = Modifier,
                targetId = targetId,
                targetType = targetType,
                targetCommunity = targetCommunity
            )
        }
    }

    companion object {

        const val EXTRA_PARAM_TARGET_ID = "target_id"
        const val EXTRA_PARAM_TARGET_TYPE = "target_type"
        const val EXTRA_PARAM_TARGET_COMMUNITY = "target_community"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityPost.TargetType,
            community: AmityCommunity? = null,
        ): Intent {
            return Intent(
                context,
                AmityCreateLivestreamPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_TARGET_TYPE, targetType)
                putExtra(EXTRA_PARAM_TARGET_COMMUNITY, community)
            }
        }
    }

}