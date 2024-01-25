package com.amity.socialcloud.uikit.community.setting.postreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.uikit.common.base.AmityBaseActivity
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.community.BR
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityPostReviewSettingsBinding

class AmityPostReviewSettingsActivity :
    AmityBaseActivity<AmityActivityPostReviewSettingsBinding, AmityPostReviewSettingsViewModel>(),
    AmityToolBarClickListener {

    companion object {
        private const val COMMUNITY_ID = "COMMUNITY_ID"

        fun newIntent(context: Context, communityId: String): Intent =
            Intent(context, AmityPostReviewSettingsActivity::class.java).apply {
                putExtra(COMMUNITY_ID, communityId)
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpToolbar()
        loadFragment()
    }

    private fun setUpToolbar() {
        getViewDataBinding().postReviewToolbar.setLeftDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.amity_ic_arrow_back
            )
        )
        getViewDataBinding().postReviewToolbar.setClickListener(this)

        val titleToolbar = getString(R.string.amity_post_review)
        getViewDataBinding().postReviewToolbar.setLeftString(titleToolbar)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(getViewDataBinding().postReviewToolbar)
    }

    private fun loadFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        var fragment: AmityPostReviewSettingsFragment? = null

        intent.getStringExtra(COMMUNITY_ID)?.let { communityId ->
            fragment = AmityPostReviewSettingsFragment.newInstance(communityId).build(this)
        }

        if (fragment != null) {
            fragmentTransaction.replace(R.id.fragmentContainer, fragment!!)
            fragmentTransaction.commit()
        }
    }

    override fun getLayoutId(): Int = R.layout.amity_activity_post_review_settings

    override fun getViewModel(): AmityPostReviewSettingsViewModel =
        ViewModelProvider(this).get(AmityPostReviewSettingsViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun leftIconClick() {
        this.finish()
    }

    override fun rightIconClick() {
        TODO("Not yet implemented")
    }
}