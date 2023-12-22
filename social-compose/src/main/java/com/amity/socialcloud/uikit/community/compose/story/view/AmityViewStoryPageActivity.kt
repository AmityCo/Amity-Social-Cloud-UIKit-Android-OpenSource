package com.amity.socialcloud.uikit.community.compose.story.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity


@UnstableApi
class AmityViewStoryPageActivity : AppCompatActivity() {

    private val behavior by lazy {
        AmityViewStoryPageBehavior(this)
    }

    private val createStory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val community: AmityCommunity = intent?.getParcelableExtra(EXTRA_PARAM_COMMUNITY)!!

        setContent {
            AmityViewStoryPage(
                community = community,
                onCloseClicked = { finish() },
                navigateToCreateStoryPage = {
                    behavior.goToCreateStoryPage(
                        launcher = createStory,
                        community = community
                    )
                }
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_COMMUNITY = "COMMUNITY"

        fun newIntent(
            context: Context,
            community: AmityCommunity,
        ): Intent {

            return Intent(
                context,
                AmityViewStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, community)
            }
        }
    }
}