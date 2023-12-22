package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityLiveStreamPostCreatorFragment


class AmityLiveStreamPostCreatorActivity : AmityBaseToolbarFragmentContainerActivity() {


    override fun initToolbar() {
        getToolBar()?.visibility = View.GONE
    }

    override fun getContentFragment(): Fragment {
        val communityId = intent.getStringExtra(EXTRA_PARAM_COMMUNITY_ID) ?: ""
        return AmityLiveStreamPostCreatorFragment.newInstance()
            .communityId(communityId)
            .build()
    }

    class AmityCreateLiveStreamPostActivityContract :
        ActivityResultContract<String?, String?>() {
        override fun createIntent(context: Context, communityId: String?): Intent {
            return Intent(context, AmityLiveStreamPostCreatorActivity::class.java).apply {
                putExtra(EXTRA_PARAM_COMMUNITY_ID, communityId)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            val createdPostId = intent?.getStringExtra(EXTRA_PARAM_POST_ID)
            return if (resultCode == Activity.RESULT_OK) createdPostId
            else null
        }
    }
}

const val EXTRA_PARAM_COMMUNITY_ID = "EXTRA_PARAM_COMMUNITY_ID"
const val EXTRA_PARAM_POST_ID = "EXTRA_PARAM_POST_ID"
