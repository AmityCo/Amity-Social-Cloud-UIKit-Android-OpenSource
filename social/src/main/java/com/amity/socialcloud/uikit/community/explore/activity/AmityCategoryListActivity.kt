package com.amity.socialcloud.uikit.community.explore.activity

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.social.community.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.explore.fragments.AmityCategoryListFragment
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener

const val EXTRA_PARAM_COMMUNITY = "community"

class AmityCategoryListActivity :
    AmityBaseToolbarFragmentContainerActivity(), AmityCategoryItemClickListener {

    override fun initToolbar() {
        showToolbarDivider()
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_category))
    }

    override fun getContentFragment(): Fragment {
        val fragment = AmityCategoryListFragment.newInstance().build(this)
        fragment.setCategoryItemClickListener(this)
        return fragment
    }

    override fun onCategorySelected(category: AmityCommunityCategory) {
        val intent = AmityCategoryCommunityListActivity.newIntent(this, category)
        startActivity(intent)
    }

}