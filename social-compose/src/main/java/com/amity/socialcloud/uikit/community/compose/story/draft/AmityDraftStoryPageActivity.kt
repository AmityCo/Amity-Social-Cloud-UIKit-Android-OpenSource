package com.amity.socialcloud.uikit.community.compose.story.draft

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.social.story.AmityStory

class AmityDraftStoryPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storyTargetId = intent.getStringExtra(EXTRA_PARAM_STORY_TARGET_ID) ?: ""
        val storyTargetType =
            intent.getSerializableExtra(EXTRA_PARAM_STORY_TARGET_TYPE) as AmityStory.TargetType
        val isImage = intent?.getBooleanExtra(EXTRA_IS_IMAGE, false) ?: false
        val fileUri = intent?.getStringExtra(EXTRA_FILE_URI) ?: ""

        val exoPlayer = ExoPlayer.Builder(this).build()
        if (!isImage) {
            exoPlayer.apply {
                addMediaItem(MediaItem.fromUri(fileUri))
                prepare()
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                playWhenReady = true
            }
        }

        setContent {
            AmityDraftStoryPage(
                targetId = storyTargetId,
                targetType = storyTargetType,
                isImage = isImage,
                fileUri = Uri.parse(fileUri),
                exoPlayer = exoPlayer,
                onCreateSuccess = {
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                },
                onCloseClicked = {
                    finish()
                }
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_STORY_TARGET_ID = "story_target_id"
        private const val EXTRA_PARAM_STORY_TARGET_TYPE = "story_target_type"
        private const val EXTRA_IS_IMAGE = "is_image"
        private const val EXTRA_FILE_URI = "file_uri"

        fun newIntent(
            context: Context,
            targetId: String,
            targetType: AmityStory.TargetType = AmityStory.TargetType.COMMUNITY,
            isImage: Boolean,
            fileUri: String,
        ): Intent {

            return Intent(
                context,
                AmityDraftStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_STORY_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_STORY_TARGET_TYPE, targetType)
                putExtra(EXTRA_IS_IMAGE, isImage)
                putExtra(EXTRA_FILE_URI, fileUri)
            }
        }
    }
}