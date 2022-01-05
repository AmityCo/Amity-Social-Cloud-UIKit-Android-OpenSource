package com.amity.socialcloud.uikit.community.setting.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R

class AmityUserSettingsActivity : AmityBaseToolbarFragmentContainerActivity() {
    private lateinit var currentUser: AmityUser

    override fun onCreate(savedInstanceState: Bundle?) {
        currentUser = intent.extras?.getParcelable(EXTRA_PARAM_USER)!!
        super.onCreate(savedInstanceState)
    }

    override fun initToolbar() {
        getToolBar()?.apply {
            setLeftDrawable(
                ContextCompat.getDrawable(
                    this@AmityUserSettingsActivity,
                    R.drawable.amity_ic_arrow_back
                )
            )
            setLeftString(
                getString(R.string.amity_settings)
            )
        }
    }

    override fun getContentFragment(): Fragment {
        return AmityUserSettingsFragment.newInstance(currentUser).build(this)
    }

    companion object {

        private const val EXTRA_PARAM_USER = "USER"

        fun newIntent(context: Context, amityUser: AmityUser) =
            Intent(context, AmityUserSettingsActivity::class.java).apply {
                putExtra(EXTRA_PARAM_USER, amityUser)
            }
    }
}