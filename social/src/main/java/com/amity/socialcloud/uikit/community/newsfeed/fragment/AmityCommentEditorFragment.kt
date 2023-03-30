package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.dialog.AmityAlertDialogFragment
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.activity.EXTRA_PARAM_COMMENT
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityCommentViewModel

class AmityCommentEditorFragment :
    AmityCommentBaseFragment(), AmityAlertDialogFragment.IAlertDialogActionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observeComment(onResult = {
            setUpTextUserMentions(it)
        })?.subscribe()
    }

    override fun getCommentMenuText(): String {
        return getString(R.string.amity_save_caps)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == ID_MENU_ITEM_COMMENT) {
            updateCommentMenu(false)
            updateComment()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpTextUserMentions(comment: AmityComment) {
        (comment.getData() as? AmityComment.Data.TEXT)?.getText()?.let { commentText ->
            binding.etPostComment.setText(commentText)
            getMentionedUsersMetadata(comment).forEach { mentionItem ->
                val mentionStart = mentionItem.getIndex()
                val mentionEnd = mentionItem.getIndex().plus(mentionItem.getLength()).inc()
                binding.etPostComment.apply {
                    text.delete(mentionStart, mentionEnd)
                    setSelection(mentionStart)
                    getMentionedUser(mentionItem.getUserId(), comment)?.let {
                        insertMentionWithoutToken(AmityUserMention(it))
                    }
                }
            }
        }
    }

    private fun getMentionedUsersMetadata(comment: AmityComment): List<AmityMentionMetadata.USER> {
        val mentionedUsers = AmityMentionMetadataGetter(comment.getMetadata()!!).getMentionedUsers()
        val mentioneeIds = comment.getMentionees().map { (it as? AmityMentionee.USER)?.getUserId() }
        return mentionedUsers.filter { mentioneeIds.contains(it.getUserId()) }
    }

    private fun getMentionedUser(userId: String, comment: AmityComment): AmityUser? {
        return comment.getMentionees().map { (it as? AmityMentionee.USER)?.getUser() }
            .find { it?.getUserId() == userId }
    }

    private fun updateComment() {
        viewModel.updateComment(binding.etPostComment.getTextCompose(),
            binding.etPostComment.getUserMentions(),
            onSuccess = { comment ->
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_PARAM_COMMENT, comment)
                activity?.setResult(AppCompatActivity.RESULT_OK, resultIntent)
                backPressFragment()
            }, onError = {
                updateCommentMenu(true)
                val isReply = viewModel.getComment()?.getParentId()?.isNotEmpty() == true
                if (isReply) {
                    view?.showSnackBar(getString(R.string.amity_update_reply_error_message))
                } else {
                    view?.showSnackBar(getString(R.string.amity_update_comment_error_message))
                }
            })
            .subscribe()
    }

    class Builder internal constructor() {
        private var comment: AmityComment? = null

        fun build(activity: AppCompatActivity): AmityCommentEditorFragment {
            val fragment = AmityCommentEditorFragment()
            fragment.viewModel = ViewModelProvider(activity).get(AmityCommentViewModel::class.java)
            fragment.viewModel.setComment(comment)

            return fragment
        }

        internal fun setComment(comment: AmityComment): Builder {
            this.comment = comment
            return this
        }

    }

    companion object {

        fun newInstance(comment: AmityComment): Builder {
            return Builder().setComment(comment)
        }
    }
}