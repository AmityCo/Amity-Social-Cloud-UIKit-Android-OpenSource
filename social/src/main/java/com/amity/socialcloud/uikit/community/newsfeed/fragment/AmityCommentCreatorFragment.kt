package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityCommentViewModel

class AmityCommentCreatorFragment : AmityCommentBaseFragment() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == ID_MENU_ITEM_COMMENT) {
            updateCommentMenu(false)
            addComment()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getCommentMenuText(): String {
        return getString(R.string.amity_post_caps)
    }

    private fun addComment() {
        val addComment = viewModel.addComment(binding.etPostComment.getTextCompose(),
            binding.etPostComment.getUserMentions(),
            onSuccess = {
                activity?.setResult(AppCompatActivity.RESULT_OK)
                backPressFragment()
            },
            onError = {
                if (AmityError.from(it) == AmityError.BAN_WORD_FOUND) {
                    // TODO: 21/2/23 delete a comment
//                    viewModel.deleteComment().subscribe()
                }
                updateCommentMenu(true)
                if (viewModel.getReply() != null) {
                    view?.showSnackBar(getString(R.string.amity_add_reply_error_message))
                } else {
                    view?.showSnackBar(getString(R.string.amity_add_comment_error_message))
                }
            })
            .subscribe()
        addComment.let { disposable.add(addComment) }
    }

    class Builder internal constructor() {
        private var postId: String = ""
        private var commentText: String? = null
        private var reply: AmityComment? = null

        fun build(activity: AppCompatActivity): AmityCommentCreatorFragment {
            val fragment = AmityCommentCreatorFragment()
            fragment.viewModel = ViewModelProvider(activity).get(AmityCommentViewModel::class.java)
            fragment.viewModel.setPost(postId)
            fragment.viewModel.setCommentData(commentText ?: "")
            fragment.viewModel.setReplyTo(reply)
            return fragment
        }

        internal fun setPostId(postId: String): Builder {
            this.postId = postId
            return this
        }

        internal fun setReplyTo(reply: AmityComment): Builder {
            this.reply = reply
            return this
        }

        fun prefilledText(text: String): Builder {
            this.commentText = text
            return this
        }
    }

    class CommentTargetPicker internal constructor() {
        fun onPost(postId: String): Builder {
            return Builder().setPostId(postId)
        }

        fun onComment(parent: AmityComment): Builder {
            val postId = (parent.getReference() as AmityComment.Reference.POST).getPostId()
            return Builder().setPostId(postId)
                .setReplyTo(parent)
        }
    }

    companion object {

        fun newInstance(): CommentTargetPicker {
            return CommentTargetPicker()
        }
    }
}