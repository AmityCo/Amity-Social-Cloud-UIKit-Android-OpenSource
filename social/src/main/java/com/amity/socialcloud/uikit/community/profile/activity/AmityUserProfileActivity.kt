package com.amity.socialcloud.uikit.community.profile.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.profile.fragment.AmityUserProfilePageFragment

class AmityUserProfileActivity : AmityBaseToolbarFragmentContainerActivity() {

    companion object {
        private const val EXTRA_PARAM_USER_ID = "USER_ID"

        fun newIntent(context: Context, id: String) =
            Intent(context, AmityUserProfileActivity::class.java).apply {
                putExtra(EXTRA_PARAM_USER_ID, id)
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
        val userId = intent.extras!!.getString(EXTRA_PARAM_USER_ID)!!
        return AmityUserProfilePageFragment.newInstance(userId).build(this)
    }

}