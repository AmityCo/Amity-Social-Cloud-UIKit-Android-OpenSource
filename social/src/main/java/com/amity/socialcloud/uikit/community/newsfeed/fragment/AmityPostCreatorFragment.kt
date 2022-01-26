package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityPostAttachmentOptionItem
import com.amity.socialcloud.uikit.community.newsfeed.util.AmityNewsFeedEvents
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ATTACHMENT_OPTIONS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmityPostCreatorFragment : AmityBaseCreatePostFragment() {

    override fun handlePostMenuItemClick() {
        view?.let(AmityAndroidUtil::hideKeyboard)
        createPost()
    }

    override fun setToolBarText() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = getToolbarTitleForCreatePost()
    }

    override fun getPostMenuText(): String {
        return getString(R.string.amity_post_caps)
    }

    override fun onClickNegativeButton() {

    }

    private fun getToolbarTitleForCreatePost(): String {
        if (viewModel.community != null)
            return viewModel.community!!.getDisplayName()
        return getString(R.string.amity_my_timeline)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etPost.requestFocus()
    }

    private fun createPost() {
        if (isLoading) {
            return
        }
        isLoading = true
        updatePostMenu(false)
        val ekoPostSingle = viewModel.createPost(binding.etPost.getTextCompose(),
            binding.etPost.getUserMentions())

        val disposable = ekoPostSingle
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { post ->
                viewModel.checkReviewingPost(
                    feedTye = post.getFeedType(),
                    showDialog = { showPendingPostsDialog(post) },
                    closePage = { handleCreatePostSuccessResponse(post) },
                )
            }
            .doOnError {
                updatePostMenu(true)
                isLoading = false
                showErrorMessage(it.message)
            }
            .subscribe()
        compositeDisposable.add(disposable)
    }
    
    private fun showPendingPostsDialog (post: AmityPost) {
        AmityAlertDialogUtil.showDialog(requireContext(),
            getString(R.string.amity_create_post_pending_post_title_dialog),
            getString(R.string.amity_create_post_pending_post_message_dialog),
            getString(R.string.amity_ok),
            negativeButton = null,
            cancelable = false
        ) { dialog, which ->
            AmityAlertDialogUtil.checkConfirmDialog(
                isPositive = which,
                confirmed = {
                    dialog.dismiss()
                    handleCreatePostSuccessResponse(post)
                })
        }
    }


    private fun handleCreatePostSuccessResponse(post: AmityPost) {
        val resultIntent = Intent("postCreation")
        resultIntent.putExtra(
            EXTRA_PARAM_POST_ID,
            post.getPostId()
        )
        activity?.setResult(Activity.RESULT_OK, resultIntent)
        AmityNewsFeedEvents.newPostCreated = true
        activity?.finish()
    }

    class Builder internal constructor() {
        private var communityId: String? = null
        private var postCreationOptions: ArrayList<AmityPostAttachmentOptionItem>? = null

        fun build(): AmityPostCreatorFragment {
            return AmityPostCreatorFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_COMMUNITY_ID, this@Builder.communityId)
                    putParcelableArrayList(
                        EXTRA_PARAM_POST_ATTACHMENT_OPTIONS,
                        this@Builder.postCreationOptions
                    )
                }
            }
        }

        internal fun onMyFeed(): Builder {
            return this
        }

        internal fun onCommunityFeed(communityId: String): Builder {
            this.communityId = communityId
            return this
        }

        fun allowPostAttachments(postAttachments: List<AmityPostAttachmentOptionItem>): Builder {
            this.postCreationOptions = ArrayList(postAttachments)
            return this
        }
    }

    class PostTargetPicker internal constructor() {

        fun onMyFeed(): Builder {
            return Builder().onMyFeed()
        }

        fun onCommunityFeed(communityId: String): Builder {
            return Builder().onCommunityFeed(communityId)
        }
    }

    companion object {
        fun newInstance(): PostTargetPicker {
            return PostTargetPicker()
        }
    }
}