package com.amity.socialcloud.uikit.community.compose.story.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.model.social.story.AmityStory

class AmityCreateStoryPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val targetId = intent.getStringExtra(EXTRA_PARAM_STORY_TARGET_ID) ?: ""
        val targetType =
            intent.getSerializableExtra(EXTRA_PARAM_STORY_TARGET_TYPE) as AmityStory.TargetType

        setContent {
            AmityCreateStoryPage(
                targetId = targetId,
                targetType = targetType,
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_STORY_TARGET_ID = "story_target_id"
        private const val EXTRA_PARAM_STORY_TARGET_TYPE = "story_target_type"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityStory.TargetType = AmityStory.TargetType.COMMUNITY,
        ): Intent {
            return Intent(
                context,
                AmityCreateStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_STORY_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_STORY_TARGET_TYPE, targetType)
            }
        }
    }
}