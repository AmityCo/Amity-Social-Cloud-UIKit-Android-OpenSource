package com.amity.socialcloud.uikit.community.newsfeed.activity

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityCustomPostRankingFeedFragment

class AmityCustomPostRankingFeedActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back))
    }

    override fun getContentFragment(): Fragment {
        return AmityCustomPostRankingFeedFragment.newInstance().build(this)
    }
}