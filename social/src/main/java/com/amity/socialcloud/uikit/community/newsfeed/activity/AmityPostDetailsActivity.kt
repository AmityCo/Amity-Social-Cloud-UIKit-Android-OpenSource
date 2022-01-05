package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityPostDetailFragment
import com.amity.socialcloud.uikit.community.newsfeed.util.AmityTimelineType
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_TIMELINE_TYPE

class AmityPostDetailsActivity :
    AmityBaseToolbarFragmentContainerActivity() {
    
    companion object {
        fun newIntent(
            context: Context,
            postId: String,
            timelineType: AmityTimelineType? = null,
            comment: AmityComment? = null
        ): Intent =
            Intent(context, AmityPostDetailsActivity::class.java).apply {
                putExtra(EXTRA_PARAM_POST_ID, postId)
                putExtra(EXTRA_PARAM_TIMELINE_TYPE, timelineType)
                putExtra(EXTRA_PARAM_COMMENT, comment)
            }
    }
    
    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
    }
    
    override fun getContentFragment(): Fragment {
        val postId =  intent.getStringExtra(EXTRA_PARAM_POST_ID) ?: ""
        return AmityPostDetailFragment.newInstance(postId).build()
    }
    
    override fun leftIconClick() {
        this.finish()
    }
    
}
