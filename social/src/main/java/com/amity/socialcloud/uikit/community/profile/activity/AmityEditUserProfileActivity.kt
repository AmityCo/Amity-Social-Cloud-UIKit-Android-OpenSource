package com.amity.socialcloud.uikit.community.profile.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.profile.fragment.AmityUserProfileEditorFragment

class AmityEditUserProfileActivity : AmityBaseToolbarFragmentContainerActivity() {

    companion object {
        fun newIntent(context: Context) =
            Intent(context, AmityEditUserProfileActivity::class.java)
    }

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_edit_profile))
    }

    override fun getContentFragment(): Fragment {
        return AmityUserProfileEditorFragment.Builder().build(this)
    }

}