package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityReactionListFragment

class AmityReactionListActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        getToolBar()?.setLeftString(getString(R.string.amity_reactions))
    }

    override fun getContentFragment(): Fragment {
        val referenceType: AmityReactionReferenceType =
            intent?.getSerializableExtra(REFERENCE_TYPE) as AmityReactionReferenceType
        val referenceId: String = intent?.getStringExtra(REFERENCE_ID) ?: ""

        return AmityReactionListFragment.newInstance(referenceType, referenceId).build(this)
    }

    companion object {

        private const val REFERENCE_TYPE = "REFERENCE_TYPE"
        private const val REFERENCE_ID = "REFERENCE_ID"

        fun newIntent(
            context: Context,
            referenceType: AmityReactionReferenceType,
            referenceId: String
        ): Intent {
            return Intent(
                context,
                AmityReactionListActivity::class.java
            ).apply {
                putExtra(REFERENCE_TYPE, referenceType)
                putExtra(REFERENCE_ID, referenceId)
            }
        }
    }
}