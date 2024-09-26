package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityCommentCreatorFragment
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID

class AmityCommentCreatorActivity : AmityBaseToolbarFragmentContainerActivity() {

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_cross))
        val replyTo: AmityComment? = intent.getParcelableExtra(EXTRA_PARAM_COMMENT_REPLY_TO)

        if (replyTo != null) {
            getToolBar()?.setLeftString(getString(R.string.amity_reply_to))
        }else {
            getToolBar()?.setLeftString( getString(R.string.amity_add_comment))
        }
    }

    override fun getContentFragment(): Fragment {
        val replyTo: AmityComment? = intent.getParcelableExtra(EXTRA_PARAM_COMMENT_REPLY_TO)
        val commentText: String? = intent.getStringExtra(EXTRA_PARAM_COMMENT_TEXT)
        val postId: String = intent?.getStringExtra(EXTRA_PARAM_POST_ID)!!
        val fragmentBuilder =  if (replyTo != null) AmityCommentCreatorFragment.newInstance().onComment(replyTo)
                                else AmityCommentCreatorFragment.newInstance().onPost(postId)
        commentText?.let {
            fragmentBuilder.prefilledText(it)
        }

        return fragmentBuilder.build(this)
    }

    class AmityAddCommentContract : ActivityResultContract<Bundle?, Boolean?>() {

        override fun createIntent(context: Context, input: Bundle?): Intent {
            val postId: String? = input?.getString(EXTRA_PARAM_POST_ID)
            val commentText = input?.getString(EXTRA_PARAM_COMMENT_TEXT)
            val replyTo: AmityComment? = input?.getParcelable(EXTRA_PARAM_COMMENT_REPLY_TO)
            val intent = Intent(context, AmityCommentCreatorActivity::class.java)
            intent.putExtra(EXTRA_PARAM_POST_ID, postId)
            intent.putExtra(EXTRA_PARAM_COMMENT_TEXT, commentText)
            intent.putExtra(EXTRA_PARAM_COMMENT_REPLY_TO, replyTo)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) true
            else null
        }

    }
}