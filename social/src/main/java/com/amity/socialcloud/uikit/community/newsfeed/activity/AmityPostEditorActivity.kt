package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityPostEditorFragment
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID

class AmityPostEditorActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_cross))
        getToolBar()?.setLeftString(getString(R.string.amity_edit_post))
    }

    override fun getContentFragment(): Fragment {
        return intent?.getStringExtra(EXTRA_PARAM_POST_ID)?.let { postId ->
            AmityPostEditorFragment
                .newInstance(postId)
                .build()
        } ?: kotlin.run {
            Fragment()
        }
    }

    class AmityEditPostActivityContract : ActivityResultContract<AmityPost, String?>() {
        override fun createIntent(context: Context, input: AmityPost): Intent {
            return Intent(context, AmityPostEditorActivity::class.java).apply {
                putExtra(EXTRA_PARAM_POST_ID, input?.getPostId())
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            val data = intent?.getStringExtra(EXTRA_PARAM_POST_ID)
            return if (resultCode == Activity.RESULT_OK && data != null) data
            else null
        }
    }
}