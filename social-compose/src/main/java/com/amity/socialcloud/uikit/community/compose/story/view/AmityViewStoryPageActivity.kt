package com.amity.socialcloud.uikit.community.compose.story.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity


class AmityViewStoryPageActivity : AppCompatActivity() {

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

        val type =
            intent.getParcelableExtra<AmityViewStoryPageType>(EXTRA_PARAM_VIEW_STORY_PAGE_TYPE)
                ?: return

        setContent {
            AmityViewStoryPage(type = type)
        }
    }

    companion object {

        private const val EXTRA_PARAM_VIEW_STORY_PAGE_TYPE = "view_story_page_type"

        fun newIntent(
            context: Context,
            type: AmityViewStoryPageType
        ): Intent {

            return Intent(
                context,
                AmityViewStoryPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_VIEW_STORY_PAGE_TYPE, type)
            }
        }
    }
}