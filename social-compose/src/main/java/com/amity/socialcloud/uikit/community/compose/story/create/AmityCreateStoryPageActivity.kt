package com.amity.socialcloud.uikit.community.compose.story.create

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper

@UnstableApi
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

        val community: AmityCommunity = intent?.getParcelableExtra(EXTRA_PARAM_COMMUNITY)!!

        setContent {
            AmityCreateStoryPage(
                onCloseClicked = {
                    finish()
                },
                onMediaSelected = { isImage, uri ->
                    behavior.goToDraftStoryPage(
                        context = this@AmityCreateStoryPageActivity,
                        launcher = draftStory,
                        community = community,
                        isImage = isImage,
                        fileUri = uri
                    )
                }
            )
        }
    }

    companion object {

        const val EXTRA_PARAM_COMMUNITY = "community"

        fun newIntent(
            context: Context,
            community: AmityCommunity
        ): Intent {
            return Intent(
                context,
                AmityCreateStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, community)
            }
        }
    }
}