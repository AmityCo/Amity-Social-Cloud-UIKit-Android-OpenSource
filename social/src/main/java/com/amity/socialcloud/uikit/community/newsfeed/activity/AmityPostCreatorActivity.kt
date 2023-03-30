package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.explore.activity.EXTRA_PARAM_COMMUNITY
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityPostCreatorFragment
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID

class AmityPostCreatorActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        val community = intent?.getParcelableExtra<AmityCommunity>(EXTRA_PARAM_COMMUNITY)
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_cross))
        getToolBar()?.setLeftString(
            community?.getDisplayName() ?: getString(
                R.string.amity_my_timeline
            )
        )
    }

    override fun getContentFragment(): Fragment {
        return intent?.getStringExtra(EXTRA_PARAM_COMMUNITY)?.let { communityId ->
            return AmityPostCreatorFragment.newInstance()
                    .onCommunityFeed(communityId)
                    .build()
        } ?: kotlin.run {
            AmityPostCreatorFragment.newInstance()
                    .onMyFeed()
                    .build()
        }
    }

    class AmityCreateCommunityPostActivityContract :
        ActivityResultContract<String, String?>() {
        override fun createIntent(context: Context, communityId: String?): Intent {
            return Intent(context, AmityPostCreatorActivity::class.java).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, communityId)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            val data = intent?.getStringExtra(EXTRA_PARAM_POST_ID)
            return if (resultCode == Activity.RESULT_OK && data != null) data
            else null
        }
    }
}