package com.amity.socialcloud.uikit.community.detailpage

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R

class AmityCommunityPageActivity :
    AmityBaseToolbarFragmentContainerActivity() {

    companion object {
        private const val COMMUNITY = "COMMUNITY"
        private const val IS_CREATE_COMMUNITY = "IS_CREATE_COMMUNITY"

        fun newIntent(context: Context, community: AmityCommunity, isCreateCommunity: Boolean = false): Intent {
            return Intent(context, AmityCommunityPageActivity::class.java).apply {
                putExtra(COMMUNITY, community)
                putExtra(IS_CREATE_COMMUNITY, isCreateCommunity)
            }
        }
    }

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.amity_ic_arrow_back
            )
        )
    }

    override fun getContentFragment(): Fragment {
        val amityCommunity: AmityCommunity = intent?.extras?.getParcelable(COMMUNITY)!!
        return AmityCommunityPageFragment
            .newInstance(amityCommunity)
            .createCommunitySuccess(intent?.extras?.getBoolean(IS_CREATE_COMMUNITY) ?: false)
            .build(this)
    }
}