package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityStoryTargetPickerFragment

class AmityStoryTargetPickerActivity : AmityBaseToolbarFragmentContainerActivity() {

    private lateinit var storyCreationType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        storyCreationType =
            intent.getStringExtra(EXTRA_STORY_CREATION_TYPE) ?: STORY_CREATION
        super.onCreate(savedInstanceState)
    }

    override fun getContentFragment(): Fragment {
        return AmityStoryTargetPickerFragment.newInstance().build(storyCreationType)
    }

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_cross)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_story_to))
    }

    override fun leftIconClick() {
        this.finish()
    }

    class AmityStoryTargetPickerActivityContract : ActivityResultContract<CreationType, String?>() {

        override fun createIntent(context: Context, creationType: CreationType): Intent {
            return Intent(context, AmityStoryTargetPickerActivity::class.java).apply {
                putExtra(EXTRA_STORY_CREATION_TYPE, creationType.getName())
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return if (resultCode == Activity.RESULT_OK) ""
            else null
        }
    }

    sealed class CreationType {
        abstract fun getName(): String

        object STORY : CreationType() {
            override fun getName(): String = STORY_CREATION
        }
    }
}

private const val EXTRA_STORY_CREATION_TYPE = "EXTRA_POST_CREATION_TYPE"
const val STORY_CREATION = "STORY_CREATION"