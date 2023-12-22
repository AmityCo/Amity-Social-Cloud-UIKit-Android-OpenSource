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
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityPostTargetPickerFragment

class AmityPostTargetPickerActivity : AmityBaseToolbarFragmentContainerActivity() {


    private lateinit var postCreationType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        postCreationType =
            intent.getStringExtra(EXTRA_POST_CREATION_TYPE) ?: POST_CREATION_TYPE_GENERIC
        super.onCreate(savedInstanceState)
    }

    override fun getContentFragment(): Fragment {
        return AmityPostTargetPickerFragment.newInstance().build(postCreationType)
    }

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_cross)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_post_to))
    }

    override fun leftIconClick() {
        this.finish()
    }

    class AmityPostTargetPickerActivityContract : ActivityResultContract<CreationType, String?>() {

        override fun createIntent(context: Context, creationType: CreationType): Intent {
            return Intent(context, AmityPostTargetPickerActivity::class.java).apply {
                putExtra(EXTRA_POST_CREATION_TYPE, creationType.getName())
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            return if (resultCode == Activity.RESULT_OK) ""
            else null
        }
    }

    sealed class CreationType {
        abstract fun getName(): String

        object GENERIC : CreationType() {
            override fun getName(): String = POST_CREATION_TYPE_GENERIC
        }

        object LIVE_STREAM : CreationType() {
            override fun getName(): String = POST_CREATION_TYPE_LIVE_STREAM
        }

        object POLL : CreationType() {
            override fun getName(): String = POST_CREATION_TYPE_POLL
        }

        object STORY : CreationType() {
            override fun getName(): String = STORY_CREATION
        }
    }
}

private const val EXTRA_POST_CREATION_TYPE = "EXTRA_POST_CREATION_TYPE"
const val POST_CREATION_TYPE_GENERIC = "POST_CREATION_TYPE_GENERIC"
const val POST_CREATION_TYPE_LIVE_STREAM = "POST_CREATION_TYPE_LIVE_STREAM"
const val POST_CREATION_TYPE_POLL = "POST_CREATION_TYPE_LIVE_POLL"