package com.amity.socialcloud.uikit.community.compose.story.draft

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.model.social.story.AmityStory

class AmityDraftStoryPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storyTargetId = intent.getStringExtra(EXTRA_PARAM_STORY_TARGET_ID) ?: ""
        val storyTargetType =
            intent.getSerializableExtra(EXTRA_PARAM_STORY_TARGET_TYPE) as AmityStory.TargetType
        val mediaType = intent?.getParcelableExtra<AmityStoryMediaType>(EXTRA_MEDIA_TYPE) ?: return

        setContent {
            AmityDraftStoryPage(
                targetId = storyTargetId,
                targetType = storyTargetType,
                mediaType = mediaType,
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_STORY_TARGET_ID = "story_target_id"
        private const val EXTRA_PARAM_STORY_TARGET_TYPE = "story_target_type"
        private const val EXTRA_MEDIA_TYPE = "media_type"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityStory.TargetType = AmityStory.TargetType.COMMUNITY,
            mediaType: AmityStoryMediaType,
        ): Intent {

            return Intent(
                context,
                AmityDraftStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_STORY_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_STORY_TARGET_TYPE, targetType)
                putExtra(EXTRA_MEDIA_TYPE, mediaType)
            }
        }
    }
}