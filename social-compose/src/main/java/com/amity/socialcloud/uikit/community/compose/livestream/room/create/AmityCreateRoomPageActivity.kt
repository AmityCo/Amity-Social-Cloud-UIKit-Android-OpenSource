package com.amity.socialcloud.uikit.community.compose.livestream.room.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.post.AmityPost

class AmityCreateRoomPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val targetId = intent.getStringExtra(EXTRA_PARAM_TARGET_ID) ?: ""
        val targetType =
            intent.getSerializableExtra(EXTRA_PARAM_TARGET_TYPE) as AmityPost.TargetType

        val targetCommunity =
            intent.getParcelableExtra(EXTRA_PARAM_TARGET_COMMUNITY) as AmityCommunity?

        val postId =
            intent.getStringExtra(EXTRA_PARAM_POST_ID) ?: ""

        setContent {
            AmityCreateRoomPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                targetId = targetId,
                targetType = targetType,
                targetCommunity = targetCommunity,
                postId = postId,
            )
        }
    }

    companion object {

        const val EXTRA_PARAM_TARGET_ID = "target_id"
        const val EXTRA_PARAM_TARGET_TYPE = "target_type"
        const val EXTRA_PARAM_TARGET_COMMUNITY = "target_community"
        const val EXTRA_PARAM_POST_ID = "post_id"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityPost.TargetType,
            community: AmityCommunity? = null,
            postId: String? = null,
        ): Intent {
            return Intent(
                context,
                AmityCreateRoomPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_TARGET_TYPE, targetType)
                putExtra(EXTRA_PARAM_TARGET_COMMUNITY, community)
                putExtra(EXTRA_PARAM_POST_ID, postId)
            }
        }

        fun newIntentFromEvent(
            context: Context,
            event: AmityEvent,
        ): Intent {
            val community = event.getTargetCommunity()
            return Intent(
                context,
                AmityCreateRoomPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_TARGET_ID, community?.getCommunityId())
                putExtra(EXTRA_PARAM_TARGET_TYPE, AmityPost.TargetType.COMMUNITY)
                putExtra(EXTRA_PARAM_TARGET_COMMUNITY, community)
                putExtra(EXTRA_PARAM_POST_ID, event.getPostId())
            }
        }
    }

}