package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityPostTargetPickerFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityStoryTargetPickerFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.POST_CREATION_TYPE_GENERIC
import com.amity.socialcloud.uikit.community.newsfeed.fragment.POST_CREATION_TYPE_LIVE_STREAM
import com.amity.socialcloud.uikit.community.newsfeed.fragment.POST_CREATION_TYPE_POLL
import com.amity.socialcloud.uikit.community.newsfeed.fragment.STORY_CREATION_TYPE_COMMUNITY
import kotlinx.parcelize.Parcelize

class AmityTargetSelectionPageActivity : AmityBaseToolbarFragmentContainerActivity() {

    private lateinit var type: AmityTargetSelectionPageType

    override fun onCreate(savedInstanceState: Bundle?) {
        type =
            AmityTargetSelectionPageType.enumOf(intent.getStringExtra(EXTRA_TARGET_SELECTION_TYPE))
        super.onCreate(savedInstanceState)
    }

    override fun getContentFragment(): Fragment {
        return when (type) {
            AmityTargetSelectionPageType.STORY -> {
                AmityStoryTargetPickerFragment.newInstance().build(type.key)
            }

            else -> {
                AmityPostTargetPickerFragment.newInstance().build(type.key)
            }
        }
    }

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_cross)
        )
        getToolBar()?.setLeftString(
            getString(
                if (type == AmityTargetSelectionPageType.STORY) R.string.amity_story_to
                else R.string.amity_post_to
            )
        )
    }

    override fun leftIconClick() {
        this.finish()
    }


    class AmityTargetSelectionPageActivityContract :
        ActivityResultContract<AmityTargetSelectionPageType, String?>() {

        override fun createIntent(context: Context, input: AmityTargetSelectionPageType): Intent {
            return Intent(context, AmityTargetSelectionPageActivity::class.java).apply {
                putExtra(EXTRA_TARGET_SELECTION_TYPE, input.key)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return if (resultCode == Activity.RESULT_OK) ""
            else null
        }
    }
}

private const val EXTRA_TARGET_SELECTION_TYPE = "EXTRA_TARGET_SELECTION_TYPE"

@Parcelize
enum class AmityTargetSelectionPageType(val key: String) : Parcelable {
    POST(POST_CREATION_TYPE_GENERIC),
    POLL(POST_CREATION_TYPE_POLL),
    LIVESTREAM(POST_CREATION_TYPE_LIVE_STREAM),
    STORY(STORY_CREATION_TYPE_COMMUNITY);

    companion object {
        fun enumOf(value: String?): AmityTargetSelectionPageType {
            return values().find { it.key == value } ?: POST
        }
    }
}