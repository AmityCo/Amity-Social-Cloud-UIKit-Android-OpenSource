package com.amity.socialcloud.uikit.community.members

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R

class AmityCommunityMemberSettingsActivity : AmityBaseToolbarFragmentContainerActivity() {

    companion object {
        private const val COMMUNITY_ID = "COMMUNITY_ID"
        private const val IS_MEMBER = "IS_MEMBER"

        fun newIntent(context: Context, id: String, isMember: Boolean): Intent =
            Intent(context, AmityCommunityMemberSettingsActivity::class.java).apply {
                putExtra(COMMUNITY_ID, id)
                putExtra(IS_MEMBER, isMember)
            }
    }

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_members_capital))
    }

    override fun getContentFragment(): Fragment {
        return AmityCommunityMemberSettingsFragment.newInstance(
            intent?.getStringExtra(COMMUNITY_ID) ?: ""
        )
            .isMember(intent?.getBooleanExtra(IS_MEMBER, false) ?: false)
            .build()
    }
}