package com.amity.socialcloud.uikit.community.compose.story.create

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper

class AmityCreateStoryPageActivity : AppCompatActivity() {

    private val behavior by lazy {
        AmitySocialBehaviorHelper.createStoryPageBehavior
    }

    private val draftStory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, Intent())
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storyTargetId = intent.getStringExtra(EXTRA_PARAM_STORY_TARGET_ID) ?: ""
        val storyTargetType =
            intent.getSerializableExtra(EXTRA_PARAM_STORY_TARGET_TYPE) as AmityStory.TargetType

        setContent {
            AmityCreateStoryPage(
                onCloseClicked = {
                    finish()
                },
                onMediaSelected = { isImage, uri ->
                    behavior.goToDraftStoryPage(
                        context = this@AmityCreateStoryPageActivity,
                        launcher = draftStory,
                        targetId = storyTargetId,
                        targetType = storyTargetType,
                        isImage = isImage,
                        fileUri = uri
                    )
                }
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