package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityPollPostCreatorFragment
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID

class AmityPollPostCreatorActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_cross))
        getToolBar()?.setLeftString(getString(R.string.amity_my_timeline))
    }

    override fun getContentFragment(): Fragment {
        return AmityPollPostCreatorFragment.newInstance()
            .apply {
                intent.getStringExtra(EXTRA_PARAM_COMMUNITY_ID)?.let {
                    onCommunityFeed(it)
                } ?: run {
                    onMyFeed()
                }
            }.build()
    }

    class AmityPollCreatorActivityContract : ActivityResultContract<String, String>() {
        override fun createIntent(context: Context, communityId: String?): Intent {
            return Intent(context, AmityPollPostCreatorActivity::class.java).apply {
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