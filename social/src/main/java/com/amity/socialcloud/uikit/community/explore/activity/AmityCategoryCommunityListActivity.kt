package com.amity.socialcloud.uikit.community.explore.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.explore.fragments.AmityCategoryCommunityListFragment

class AmityCategoryCommunityListActivity : AmityBaseToolbarFragmentContainerActivity() {

    companion object {
        private const val INTENT_CATEGORY = "INTENT_CATEGORY_NAME"

        fun newIntent(context: Context, category: AmityCommunityCategory): Intent =
            Intent(context, AmityCategoryCommunityListActivity::class.java).apply {
                putExtra(INTENT_CATEGORY, category)
            }
    }

    override fun initToolbar() {
        val category: AmityCommunityCategory? = intent.getParcelableExtra(INTENT_CATEGORY)
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        getToolBar()?.setLeftString(category?.getName() ?: "")
        showToolbarDivider()
    }

    override fun getContentFragment(): Fragment {
        val category: AmityCommunityCategory? = intent.getParcelableExtra(INTENT_CATEGORY)
        return category?.let {
            AmityCategoryCommunityListFragment.newInstance(category).build()
        } ?: Fragment()
    }
}