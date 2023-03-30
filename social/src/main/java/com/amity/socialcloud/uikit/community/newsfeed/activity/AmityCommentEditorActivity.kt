package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.base.AmityBaseToolbarFragmentContainerActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityCommentEditorFragment

const val EXTRA_PARAM_COMMENT: String = "Comment"
const val EXTRA_PARAM_COMMENT_TEXT: String = "Comment_Text"
const val EXTRA_PARAM_COMMENT_REPLY_TO: String = "Comment_Reply_To"

class AmityEditCommentActivity : AmityBaseToolbarFragmentContainerActivity() {
    private val TAG = AmityEditCommentActivity::class.java.canonicalName

    override fun initToolbar() {
        getToolBar()?.setLeftDrawable(ContextCompat.getDrawable(this, R.drawable.amity_ic_cross))
        val comment: AmityComment? = intent.getParcelableExtra(EXTRA_PARAM_COMMENT)
        val isReply = comment?.getParentId()?.isNotEmpty() == true

        if (isReply) {
            getToolBar()?.setLeftString(getString(R.string.amity_edit_reply))
        } else {
            getToolBar()?.setLeftString(getString(R.string.amity_edit_comment))
        }
    }

    override fun getContentFragment(): Fragment {
        val comment: AmityComment = intent.getParcelableExtra(EXTRA_PARAM_COMMENT)!!
        return AmityCommentEditorFragment.newInstance(comment).build(this)
    }


    class AmityEditCommentContract : ActivityResultContract<AmityComment, AmityComment?>() {
        override fun createIntent(context: Context, input: AmityComment): Intent {
            val intent = Intent(context, AmityEditCommentActivity::class.java)
            intent.putExtra(EXTRA_PARAM_COMMENT, input)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): AmityComment? {
            val data = intent?.getParcelableExtra<AmityComment>(EXTRA_PARAM_COMMENT)
            return if (resultCode == Activity.RESULT_OK) data
            else null
        }
    }
}