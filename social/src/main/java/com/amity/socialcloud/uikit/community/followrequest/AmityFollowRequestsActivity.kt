package com.amity.socialcloud.uikit.community.followrequest

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R

class AmityFollowRequestsActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_follow_requests))
        showToolbarDivider()
    }

    override fun getContentFragment(): Fragment {
        val userId = intent.extras?.getString(USER_ID)
        return AmityFollowRequestsFragment.newInstance(userId!!).build(this)
    }

    companion object {
        private const val USER_ID = "USER_ID"

        fun newIntent(context: Context, userID: String) =
            Intent(context, AmityFollowRequestsActivity::class.java).apply {
                putExtra(USER_ID, userID)
            }
    }
}