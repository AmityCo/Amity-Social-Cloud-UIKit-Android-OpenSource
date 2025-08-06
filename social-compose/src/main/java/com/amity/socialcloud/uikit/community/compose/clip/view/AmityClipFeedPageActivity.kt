package com.amity.socialcloud.uikit.community.compose.clip.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.compose.clip.view.util.SharedClipFeedStore

class AmityClipFeedPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

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

        val type =
            intent.getParcelableExtra<AmityClipFeedPageType>(EXTRA_PARAM_CLIP_FEED_PAGE_TYPE)
                ?: return

        setContent {
            AmityClipFeedPage(type = type)
        }
    }

    companion object {

        private const val EXTRA_PARAM_CLIP_FEED_PAGE_TYPE = "clip_feed_page_type"

        fun newIntent(
            context: Context,
            type: AmityClipFeedPageType
        ): Intent {

            return Intent(
                context,
                AmityClipFeedPageActivity::class.java
            )
                .apply {
                putExtra(EXTRA_PARAM_CLIP_FEED_PAGE_TYPE, type)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            SharedClipFeedStore.clear()
        }
    }
}