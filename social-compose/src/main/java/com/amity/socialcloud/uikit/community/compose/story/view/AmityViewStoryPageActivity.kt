package com.amity.socialcloud.uikit.community.compose.story.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper


class AmityViewStoryPageActivity : AppCompatActivity() {

    private val behavior by lazy {
        AmitySocialBehaviorHelper.viewStoryPageBehavior
    }

    private val createStory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            finishAfterTransition()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            val slideEnter = Slide(Gravity.BOTTOM)
            slideEnter.excludeTarget(android.R.id.statusBarBackground, true)
            slideEnter.excludeTarget(android.R.id.navigationBarBackground, true)
            enterTransition = slideEnter

            val slideExit = Slide(Gravity.TOP)
            slideExit.excludeTarget(android.R.id.statusBarBackground, true)
            slideExit.excludeTarget(android.R.id.navigationBarBackground, true)
            exitTransition = slideExit
        }

        val isGlobalTarget = intent.getBooleanExtra(EXTRA_PARAM_IS_GLOBAL_TARGET, false)
        val storyTargetId = intent.getStringExtra(EXTRA_PARAM_STORY_TARGET_ID) ?: ""
        val storyTargetType =
            intent.getSerializableExtra(EXTRA_PARAM_STORY_TARGET_TYPE) as AmityStory.TargetType

        setContent {
            val context = LocalContext.current

            if (isGlobalTarget) {
                AmityViewGlobalStoryPage(
                    storyTargetId = storyTargetId,
                    onClose = { finishAfterTransition() },
                    navigateToCreateStoryPage = {
                        behavior.goToCreateStoryPage(
                            context = context,
                            launcher = createStory,
                            targetId = storyTargetId,
                            targetType = storyTargetType,
                        )
                    },
                    navigateToCommunityProfilePage = { community ->
                        behavior.goToCommunityProfilePage(
                            context = context,
                            community = community
                        )
                    }
                )
            } else {
                val exoPlayer = remember { ExoPlayer.Builder(context).build() }

                AmityViewStoryPage(
                    targetId = storyTargetId,
                    targetType = storyTargetType,
                    exoPlayer = exoPlayer,
                    isSingleTarget = true,
                    onClose = { finishAfterTransition() },
                    lastSegmentReached = { finishAfterTransition() },
                    navigateToCreateStoryPage = {
                        behavior.goToCreateStoryPage(
                            context = context,
                            launcher = createStory,
                            targetId = storyTargetId,
                            targetType = storyTargetType,
                        )
                    }
                )

                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                        AmityStoryVideoPlayerHelper.clear()
                    }
                }
            }
        }
    }

    companion object {

        private const val EXTRA_PARAM_IS_GLOBAL_TARGET = "is_global_target"
        private const val EXTRA_PARAM_STORY_TARGET_ID = "story_target_id"
        private const val EXTRA_PARAM_STORY_TARGET_TYPE = "story_target_type"

        fun newIntent(
            context: Context,
            isGlobalFeed: Boolean = false,
            targetId: String,
            targetType: AmityStory.TargetType,
        ): Intent {

            return Intent(
                context,
                AmityViewStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_IS_GLOBAL_TARGET, isGlobalFeed)
                putExtra(EXTRA_PARAM_STORY_TARGET_ID, targetId)
                putExtra(EXTRA_PARAM_STORY_TARGET_TYPE, targetType)
            }
        }
    }
}