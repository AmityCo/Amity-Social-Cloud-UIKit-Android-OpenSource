package com.amity.socialcloud.uikit.sample

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityPostCreatorFragment
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityPostAttachmentOptionItem
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ATTACHMENT_OPTIONS

class AmityPostCreatorWithOptionsActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_cross))
        getToolBar()?.setLeftString(getString(R.string.amity_my_timeline))
    }

    override fun getContentFragment(): Fragment {
        val allowedAttachments = intent.getParcelableArrayListExtra<AmityPostAttachmentOptionItem>(
            EXTRA_PARAM_POST_ATTACHMENT_OPTIONS
        )?.toList()

        val postCreatorBuilder = intent.getStringExtra(EXTRA_PARAM_COMMUNITY_ID)?.let {
            AmityPostCreatorFragment.newInstance()
                .onCommunityFeed(it)
        } ?: kotlin.run {
            AmityPostCreatorFragment.newInstance()
                .onMyFeed()
        }

        return allowedAttachments?.let {
            postCreatorBuilder
                .allowPostAttachments(it)
                .build()
        } ?: kotlin.run {
            postCreatorBuilder
                .build()
        }
    }

    companion object {
        fun navigate(
            context: Context,
            communityId: String? = null,
            attachmentOptions: ArrayList<AmityPostAttachmentOptionItem>
        ) {
            val intent = Intent(context, AmityPostCreatorWithOptionsActivity::class.java).apply {
                putExtra(EXTRA_PARAM_COMMUNITY_ID, communityId)
                putParcelableArrayListExtra(EXTRA_PARAM_POST_ATTACHMENT_OPTIONS, attachmentOptions)
            }
            context.startActivity(intent)
        }
    }
}