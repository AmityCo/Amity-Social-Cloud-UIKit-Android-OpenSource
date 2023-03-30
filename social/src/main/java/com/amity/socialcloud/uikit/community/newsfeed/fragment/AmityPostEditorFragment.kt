package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityCreatePostMediaAdapter
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.newsfeed.util.AmityNewsFeedEvents
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_NEWS_FEED_ID
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd

class AmityPostEditorFragment : AmityBaseCreatePostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.postId = arguments?.getString(EXTRA_PARAM_NEWS_FEED_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPostDetails(viewModel.postId!!)
        clearHint()
    }

    override fun showComposeBar() {
        //hide compose bar until sdk support update image and file
        //right now support delete image and file
        hideComposeBar()
    }

    override fun handlePostMenuItemClick() {
        updatePost()
    }

    override fun createPostMediaAdapter(): AmityCreatePostMediaAdapter {
        return AmityCreatePostMediaAdapter(viewModel.postId!!, this)
    }

    override fun setToolBarText() {
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.amity_edit_post)
    }

    override fun getPostMenuText(): String {
        return getString(R.string.amity_save_caps)
    }

    override fun isRightButtonActive(): Boolean {
        return if (isEditMode() && !viewModel.hasUpdateOnPost(
                binding.etPost.text.toString().trim()
            )
        ) {
            false
        } else {
            super.isRightButtonActive()
        }
    }

    private fun updatePost() {
        updatePostMenu(false)
        viewModel.updatePostText(
            postText = binding.etPost.text.toString(),
            userMentions = binding.etPost.getUserMentions(),
            onUpdateSuccess = { post ->
                post?.let {
                    handleEditPostSuccessResponse(it)
                }
            },
            onUpdateFailed = {
                updatePostMenu(true)
                showErrorMessage(it.message)
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun handleEditPostSuccessResponse(post: AmityPost) {
        val resultIntent = Intent("postCreation")
        resultIntent.putExtra(
            EXTRA_PARAM_POST_ID,
            post.getPostId()
        )
        activity?.setResult(Activity.RESULT_OK, resultIntent)
        AmityNewsFeedEvents.newPostCreated = false
        activity?.finish()
    }

    private fun getPostDetails(postId: String) {
        viewModel.getPostDetails(postId = postId,
            onPostLoading = { showLoading() },
            onPostLoaded = {
                hideLoading()
                setUpPostData(it)
            },
            onPostLoadFailed = {
                hideLoading()
                showErrorMessage(it.message)
            })
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun setUpPostData(post: AmityPost) {
        viewModel.preparePostData(post)
        binding.etPost.setText(viewModel.postText)
        viewModel.getPost()?.let {
            setUpTextUserMentions(it)
        }
    }

    private fun setUpTextUserMentions(post: AmityPost) {
        (post.getData() as? AmityPost.Data.TEXT)?.getText()?.let { postText ->
            binding.etPost.setText(postText)
            getMentionedUsersMetadata(post).forEach { mentionItem ->
                val mentionStart = mentionItem.getIndex()
                val mentionEnd = mentionItem.getIndex().plus(mentionItem.getLength()).inc()
                binding.etPost.apply {
                    text.delete(mentionStart, mentionEnd)
                    setSelection(mentionStart)
                    getMentionedUser(mentionItem.getUserId(), post)?.let {
                        insertMentionWithoutToken(AmityUserMention(it))
                    }
                }
            }
        }
    }

    private fun getMentionedUsersMetadata(post: AmityPost): List<AmityMentionMetadata.USER> {
        val mentionedUsers = AmityMentionMetadataGetter(post.getMetadata()!!).getMentionedUsers()
        val mentioneeIds = post.getMentionees().map { (it as? AmityMentionee.USER)?.getUserId() }
        return mentionedUsers.filter { mentioneeIds.contains(it.getUserId()) }
    }

    private fun getMentionedUser(userId: String, post: AmityPost): AmityUser? {
        return post.getMentionees().map { (it as? AmityMentionee.USER)?.getUser() }
            .find { it?.getUserId() == userId }
    }

    class Builder internal constructor() {
        private var postId: String? = null

        fun build(): AmityPostEditorFragment {
            return AmityPostEditorFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_NEWS_FEED_ID, this@Builder.postId)
                }
            }
        }

        internal fun postId(postId: String): Builder {
            this.postId = postId
            return this
        }

    }

    companion object {
        fun newInstance(postId: String): Builder {
            return Builder().postId(postId)
        }
    }

}
