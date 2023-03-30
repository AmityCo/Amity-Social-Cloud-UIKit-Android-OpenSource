package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.feed.AmityFeedType
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityCommunityFeedFragment
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID

const val PARAM_COMMUNITY_ID = "PARAM_COMMUNITY_ID"

class AmityCommunityReviewingFeedActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back))
    }

    override fun leftIconClick() {
        this.finish()
    }

    override fun getContentFragment(): Fragment {
        val communityId = intent?.getStringExtra(EXTRA_PARAM_POST_ID) ?: ""
        return AmityCommunityFeedFragment.newInstance(communityId)
            .feedType(AmityFeedType.REVIEWING)
            .build(this)
    }

    class AmityCommunityReviewingFeedActivityContract : ActivityResultContract<String, Boolean>() {
        override fun createIntent(context: Context, communityId: String): Intent {
            return Intent(context, AmityCommunityReviewingFeedActivity::class.java).apply {
                putExtra(PARAM_COMMUNITY_ID, communityId)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }

}