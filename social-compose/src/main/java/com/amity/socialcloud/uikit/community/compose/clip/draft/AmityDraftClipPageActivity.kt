package com.amity.socialcloud.uikit.community.compose.clip.draft

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

class AmityDraftClipPageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clipTargetId = intent.getStringExtra(EXTRA_PARAM_CLIP_TARGET_ID) ?: ""
        val clipTargetType = intent.getSerializableExtra(EXTRA_PARAM_CLIP_TARGET_TYPE) as AmityPostTargetType
        val media = intent?.getParcelableExtra<Uri>(EXTRA_MEDIA_TYPE) ?: return

        setContent {
            AmityDraftClipPage(
                targetId = clipTargetId,
                targetType = clipTargetType,
                clipUrl = media
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_CLIP_TARGET_ID = "clip_target_id"
        private const val EXTRA_PARAM_CLIP_TARGET_TYPE = "clip_target_type"
        private const val EXTRA_MEDIA_TYPE = "media_type"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityPostTargetType= AmityPostTargetType.COMMUNITY,
            media: Uri,
        ): Intent {

            return Intent(
                context,
                AmityDraftClipPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_CLIP_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_CLIP_TARGET_TYPE, targetType)
                putExtra(EXTRA_MEDIA_TYPE, media)
            }
        }
    }
}