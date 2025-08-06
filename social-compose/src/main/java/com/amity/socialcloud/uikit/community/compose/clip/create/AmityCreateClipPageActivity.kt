package com.amity.socialcloud.uikit.community.compose.clip.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

class AmityCreateClipPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val targetId = intent.getStringExtra(EXTRA_PARAM_CLIP_TARGET_ID) ?: ""
        val targetType =
            intent.getSerializableExtra(EXTRA_PARAM_CLIP_TARGET_TYPE) as AmityPostTargetType

        setContent {
            AmityCreateClipPage(
                targetId = targetId,
                targetType = targetType,
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_CLIP_TARGET_ID = "clip_target_id"
        private const val EXTRA_PARAM_CLIP_TARGET_TYPE = "clip_target_type"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityPostTargetType = AmityPostTargetType.COMMUNITY,
        ): Intent {
            return Intent(
                context,
                AmityCreateClipPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_CLIP_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_CLIP_TARGET_TYPE, targetType)
            }
        }
    }
}