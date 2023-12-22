package com.amity.socialcloud.uikit.community.compose.story.draft

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity

@UnstableApi
class AmityDraftStoryPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val community: AmityCommunity = intent?.getParcelableExtra(EXTRA_PARAM_COMMUNITY)!!
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
                communityId = community.getCommunityId(),
                communityAvatarUrl = community.getAvatar()?.getUrl() ?: "",
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

        const val EXTRA_PARAM_COMMUNITY = "community"
        const val EXTRA_IS_IMAGE = "IS_IMAGE"
        const val EXTRA_FILE_URI = "FILE_URI"

        fun newIntent(
            context: Context,
            community: AmityCommunity,
            isImage: Boolean,
            fileUri: String,
        ): Intent {

            return Intent(
                context,
                AmityDraftStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, community)
                putExtra(EXTRA_IS_IMAGE, isImage)
                putExtra(EXTRA_FILE_URI, fileUri)
            }
        }
    }
}