package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.AmityFileManager
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.dialog.AmityAlertDialogFragment
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentFeedBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityEditCommentActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityLivestreamVideoPlayerActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostDetailsActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostEditorActivity
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityPostListAdapter
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedLoadStateEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostReviewClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityFeedViewModel
import com.amity.socialcloud.uikit.community.utils.AmityCommunityNavigation
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


abstract class AmityFeedFragment : AmityBaseFragment() {

    private lateinit var adapter: AmityPostListAdapter
    private lateinit var binding: AmityFragmentFeedBinding

    private var isRefreshing = false
    private var isFirstLoad = true
    private var isObservingClickEvent = false

    internal open val showProgressBarOnLaunched = true

    private val emptyStatePublisher = PublishSubject.create<Boolean>()

    private val postEditContact =
        registerForActivityResult(AmityPostEditorActivity.AmityEditPostActivityContract()) {
            // no need for this if update comes from Flowable
        }

    private var commentEditContract =
        registerForActivityResult(AmityEditCommentActivity.AmityEditCommentContract()) { comment ->
            // no need for this if update comes from Flowable
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AmityFragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showProgressBarOnLaunched) {
            binding.progressBar.visibility = View.VISIBLE
        }
        setupFeed()
    }

    abstract fun getViewModel(): AmityFeedViewModel

    override fun onPause() {
        getViewModel().sendPendingReactions()
        super.onPause()
    }

    internal open fun setupFeed() {
        setupFeedRecyclerview()
        setupFeedHeaderView()
        observeClickEvents()
        getFeed { onPageLoaded(it) }
    }

    private fun setupFeedRecyclerview() {
        adapter = getViewModel().createFeedAdapter()
        adapter.addLoadStateListener { loadStates ->
            when (val refreshState = loadStates.mediator?.refresh) {
                is LoadState.NotLoading -> {
                    handleLoadedState(adapter.itemCount, loadStates)
                }
                is LoadState.Error -> {
                    isFirstLoad = false
                    binding.progressBar.visibility = View.GONE
                    handleErrorState(AmityError.from(refreshState.error))
                }
                is LoadState.Loading -> {
                    handleLoadingState()
                }
                else -> {}
            }
        }
        binding.recyclerViewFeed.adapter = adapter
        binding.recyclerViewFeed.layoutManager = LinearLayoutManager(context)
        getItemDecorations().forEach { binding.recyclerViewFeed.addItemDecoration(it) }
        binding.recyclerViewFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_SETTLING) {
                    getViewModel().sendPendingReactions()
                }
            }
        })
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0) {
                    binding.recyclerViewFeed.smoothScrollToPosition(0)
                }
            }
        })

        emptyStatePublisher.toFlowable(BackpressureStrategy.BUFFER)
            .debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .doOnNext { shouldShowEmptyState ->
                if (shouldShowEmptyState) {
                    handleEmptyState(getEmptyView(getInflater()))
                }
            }
            .subscribe()
    }

    private fun setupFeedHeaderView() {
        val views = getFeedHeaderView(getInflater())
        if (views.isEmpty()) {
            binding.headerContainer.visibility = View.GONE
        } else {
            binding.headerContainer.visibility = View.VISIBLE
            binding.headerContainer.removeAllViews()
            views.forEach {
                binding.headerContainer.addView(it)
            }
        }
    }

    internal open fun getFeedHeaderView(inflater: LayoutInflater): List<View> {
        return listOf()
    }

    internal open fun getItemDecorations(): List<RecyclerView.ItemDecoration> {
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(requireContext().resources.getDrawable(R.drawable.amity_feed_item_separator))
        return listOf(dividerItemDecoration)
    }

    private fun handleLoadedState(itemCount: Int, loadStates: CombinedLoadStates) {
        isFirstLoad = false
        binding.emptyViewContainer.visibility = View.GONE
        binding.recyclerViewFeed.visibility = View.VISIBLE
        if (isRefreshing) {
            binding.recyclerViewFeed.scrollToPosition(0)
            isRefreshing = false
        }
        if (loadStates.source.refresh is LoadState.NotLoading
            && loadStates.append.endOfPaginationReached
            && itemCount < 1
        ) {
            if (!emptyStatePublisher.hasComplete()) {
                emptyStatePublisher.onNext(true)
            }
        } else if (loadStates.source.refresh is LoadState.NotLoading
            && loadStates.append.endOfPaginationReached
            && itemCount > 0
        ) {
            if (!emptyStatePublisher.hasComplete()) {
                emptyStatePublisher.onNext(false)
            }
        }
        getViewModel().feedLoadStatePublisher.onNext(AmityFeedLoadStateEvent.LOADED(itemCount))
    }

    private fun getInflater(): LayoutInflater {
        return requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    internal open fun handleErrorState(error: AmityError) {
        getViewModel().feedLoadStatePublisher.onNext(AmityFeedLoadStateEvent.ERROR(error))
    }

    private fun handleLoadingState() {
        getViewModel().feedLoadStatePublisher.onNext(AmityFeedLoadStateEvent.LOADING)
    }

    internal fun handleEmptyState(emptyView: View) {
        if (isFirstLoad) {
            return
        }
        if (binding.emptyViewContainer.childCount == 0) {
            binding.emptyViewContainer.removeAllViews()
            binding.emptyViewContainer.addView(emptyView)
        }
        binding.emptyViewContainer.visibility = View.VISIBLE
        binding.recyclerViewFeed.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    abstract fun getEmptyView(inflater: LayoutInflater): View

    internal fun refresh() {
        isRefreshing = true
        getFeed { onPageLoaded(it) }
    }

    private fun getFeed(onPageLoaded: (posts: PagingData<AmityBasePostItem>) -> Unit) {
        getViewModel().getFeed(onPageLoaded)
            .untilLifecycleEnd(this, getViewModel().feedDisposable)
            .subscribe()
    }

    private fun onPageLoaded(items: PagingData<AmityBasePostItem>) {
        adapter.submitData(lifecycle, items)
    }

    private fun observeClickEvents() {
        if (!isObservingClickEvent) {
            observeRefreshEvents()
            observeUserClickEvents()
            observeCommunityClickEvents()
            observePostEngagementClickEvents()
            observePostContentClickEvents()
            observePostOptionClickEvents()
            observePostReviewClickEvents()
            observePollVoteClickEvents()
            observeCommentEngagementClickEvents()
            observeCommentContentClickEvents()
            observeCommentOptionClickEvents()
            observeReactionCountClickEvents()
            isObservingClickEvent = true
        }
    }

    private fun observeRefreshEvents() {
        getViewModel().getRefreshEvents(
            onReceivedEvent = { refresh() }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observeUserClickEvents() {
        getViewModel().getUserClickEvents(
            onReceivedEvent = { getViewModel().userClickListener.onClickUser(it) }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observeCommunityClickEvents() {
        getViewModel().getCommunityClickEvents(
            onReceivedEvent = {
                getViewModel().communityClickListener.onClickCommunity(it)
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    internal open fun observePostEngagementClickEvents() {
        getViewModel().getPostEngagementClickEvents(
            onReceivedEvent = {
                when (it) {
                    is PostEngagementClickEvent.Reaction -> {
                        getViewModel().postReactionEventMap[it.post.getPostId()] = it
                    }
                    is PostEngagementClickEvent.Comment -> {
                        navigateToPostDetails(it.post.getPostId())
                    }
                    is PostEngagementClickEvent.Sharing -> {
                        showSharingOptions(it.post)
                    }
                }
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    internal open fun observeReactionCountClickEvents() {
        //  do nothing on feed screen
    }

    private fun observePostContentClickEvents() {
        getViewModel().getPostContentClickEvents(
            onReceivedEvent = {
                when (it) {
                    is PostContentClickEvent.Text -> {
                        onClickText(it.post.getPostId())
                    }
                    is PostContentClickEvent.Image -> {
                        onClickImage(it.images, it.selectedPosition)
                    }
                    is PostContentClickEvent.Video -> {
                        onClickVideo(it.parentPostId, it.selectedPosition)
                    }
                    is PostContentClickEvent.File -> {
                        onClickFileItem(it.file)
                    }
                    is PostContentClickEvent.Livestream -> {
                        navigateToStreamPlayer(it.stream)
                    }
                }
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun navigateToStreamPlayer(stream: AmityStream) {
        if (stream.getStatus() == AmityStream.Status.LIVE
            || stream.getStatus() == AmityStream.Status.RECORDED) {
            val livestreamIntent = AmityLivestreamVideoPlayerActivity.newIntent(
                context = requireContext(),
                streamId = stream.getStreamId()
            )
            requireContext().startActivity(livestreamIntent)
        }
    }

    private fun observePostOptionClickEvents() {
        getViewModel()
            .getPostOptionClickEvents(
                onReceivedEvent = {
                    showPostOptions(it.post)
                }
            )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observePostReviewClickEvents() {
        getViewModel()
            .getPostReviewClickEvents(
                onReceivedEvent = {
                    when (it) {
                        is PostReviewClickEvent.ACCEPT -> {
                            approvePost(it.post)
                        }
                        is PostReviewClickEvent.DECLINE -> {
                            declinePost(it.post)
                        }
                    }
                }
            )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observePollVoteClickEvents() {
        getViewModel()
            .getPollVoteClickEvents(
                onReceivedEvent = {
                    getViewModel().vote(pollId = it.pollId, answerIds = it.answerIds)
                }
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observeCommentEngagementClickEvents() {
        getViewModel().getCommentEngagementClickEvents(
            onReceivedEvent = {
                when (it) {
                    is CommentEngagementClickEvent.Reaction -> {
                        getViewModel().commentReactionEventMap.put(it.comment.getCommentId(), it)
                    }
                    is CommentEngagementClickEvent.Reply -> {
                        when (val reference = it.comment.getReference()) {
                            is AmityComment.Reference.POST -> {
                                navigateToPostDetails(reference.getPostId())
                            }
                            else -> {}
                        }
                    }
                }
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observeCommentContentClickEvents() {
        getViewModel().getCommentContentClickEvents(
            onReceivedEvent = {
                when (it) {
                    is CommentContentClickEvent.Text -> {
                        when (val reference = it.comment.getReference()) {
                            is AmityComment.Reference.POST -> {
                                navigateToPostDetails(reference.getPostId())
                            }
                            else -> {}
                        }
                    }
                }
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observeCommentOptionClickEvents() {
        getViewModel().getCommentOptionClickEvents(
            onReceivedEvent = {
                showCommentOptions(it.comment)
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun onClickText(postId: String) {
        navigateToPostDetails(postId)
    }

    private fun onClickImage(images: List<AmityImage>, position: Int) {
        AmityCommunityNavigation.navigateToImagePreview(requireContext(), images, position)
    }

    private fun onClickVideo(parentPostId: String, position: Int) {
        AmityCommunityNavigation.navigateToVideoPreview(requireContext(), parentPostId, position)
    }

    private fun onClickFileItem(file: AmityFile) {
        showDownloadingSnackBar(file.getFileName())
        AmityFileManager.saveFile(requireContext(), file.getUrl() ?: "", file.getFileName())
    }

    open fun navigateToPostDetails(postId: String) {
        val intent = AmityPostDetailsActivity.newIntent(requireContext(), postId, null, null)
        requireContext().startActivity(intent)
    }

    internal fun showSharingOptions(post: AmityPost) {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        bottomSheet.show(getViewModel().getSharingOptionMenuItems(
            post = post,
            shareToMyFeed = {
                bottomSheet.dismiss()
                AmitySocialUISettings.postShareClickListener.shareToMyTimeline(
                    requireContext(),
                    post
                )
            },
            shareToGroupFeed = {
                bottomSheet.dismiss()
                AmitySocialUISettings.postShareClickListener.shareToGroup(
                    requireContext(),
                    post
                )
            },
            shareToExternal = {
                bottomSheet.dismiss()
                AmitySocialUISettings.postShareClickListener.shareToExternal(
                    requireContext(),
                    post
                )
            }
        ))
    }

    private fun showPostOptions(post: AmityPost) {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        bottomSheet.show(getViewModel().getPostOptionMenuItems(
            post = post,
            editPost = {
                bottomSheet.dismiss()
                editPost(post)
            },
            deletePost = {
                bottomSheet.dismiss()
                showDeletePostWarning(post)
            },
            reportPost = {
                bottomSheet.dismiss()
                reportPost(post)
            },
            unReportPost = {
                bottomSheet.dismiss()
                unReportPost(post)
            },
            closePoll = {
                bottomSheet.dismiss()
                showClosePollWarning(post)
            },
            deletePoll = {
                bottomSheet.dismiss()
                showDeletePollWarning(post)
            }
        ))

    }

    private fun editPost(post: AmityPost) {
        postEditContact.launch(post)
    }

    private fun reportPost(post: AmityPost) {
        getViewModel().reportPost(post, {
            view?.showSnackBar(getString(R.string.amity_report_sent))
        }, {})
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun unReportPost(post: AmityPost) {
        getViewModel().unReportPost(post, {
            view?.showSnackBar(getString(R.string.amity_unreport_sent))
        }, {})
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showDeletePostWarning(post: AmityPost) {
        val deleteConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_delete_post_title,
                R.string.amity_delete_post_warning_message,
                R.string.amity_delete, R.string.amity_cancel
            )
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        deleteConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    deletePost(post)
                }

                override fun onClickNegativeButton() {
                    // do nothing
                }
            }
    }

    private fun showClosePollWarning(post: AmityPost) {
        val closeConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_close_poll_title,
                R.string.amity_close_poll_message,
                R.string.amity_close, R.string.amity_cancel
            )
        closeConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        closeConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    val data = post.getChildren()[0].getData() as AmityPost.Data.POLL
                    closePoll(data.getPollId())
                }

                override fun onClickNegativeButton() {

                }
            }
    }

    private fun showDeletePollWarning(post: AmityPost) {
        val deleteConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_delete_poll_title,
                R.string.amity_delete_poll_message,
                R.string.amity_delete_poll, R.string.amity_cancel
            )
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        deleteConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    deletePost(post)
                }

                override fun onClickNegativeButton() {

                }
            }
    }

    open fun deletePost(post: AmityPost) {
        getViewModel().deletePost(
            post = post,
            onSuccess = {
                // do nothing
            },
            onError = {
                // display error
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    open fun closePoll(pollId: String) {
        getViewModel().closePoll(
            pollId = pollId,
            onSuccess = {
                // do nothing
            },
            onError = {
                // display error
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    open fun approvePost(post: AmityPost) {
        getViewModel().approvePost(
            postId = post.getPostId(),
            onAlreadyApproved = {
                showAlreadyReviewedDialog()
            },
            onError = {
                // display error
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    open fun declinePost(post: AmityPost) {
        getViewModel().declinePost(
            postId = post.getPostId(),
            onAlreadyDeclined = {
                showAlreadyReviewedDialog()
            },
            onError = {
                // display error
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showAlreadyReviewedDialog() {
        AmityAlertDialogUtil.showDialog(
            requireContext(),
            getString(R.string.amity_post_approve_error_dialog_title),
            getString(R.string.amity_post_approve_error_dialog_description),
            getString(R.string.amity_ok),
            negativeButton = null,
            cancelable = false
        ) { dialog, which ->
            AmityAlertDialogUtil.checkConfirmDialog(
                isPositive = which,
                confirmed = {
                    dialog.dismiss()
                })
        }
    }

    private fun showCommentOptions(comment: AmityComment) {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        bottomSheet.show(getViewModel().getCommentOptionMenuItems(
            comment = comment,
            editComment = {
                bottomSheet.dismiss()
                editComment(comment)
            },
            deleteComment = {
                bottomSheet.dismiss()
                showDeleteCommentWarning(comment)
            },
            reportComment = {
                bottomSheet.dismiss()
                reportComment(comment)
            },
            unReportComment = {
                bottomSheet.dismiss()
                unReportComment(comment)
            },
            editReply = {
                bottomSheet.dismiss()
                editComment(comment)
            },
            deleteReply = {
                bottomSheet.dismiss()
                showDeleteReplyWarning(comment)
            }
        ))

    }

    private fun editComment(comment: AmityComment) {
        commentEditContract.launch(comment)
    }

    private fun reportComment(comment: AmityComment) {
        getViewModel().reportComment(comment, {
            view?.showSnackBar(getString(R.string.amity_report_sent))
        }, {})
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun unReportComment(comment: AmityComment) {
        getViewModel().unReportComment(comment, {
            view?.showSnackBar(getString(R.string.amity_unreport_sent))
        }, {})
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showDeleteCommentWarning(comment: AmityComment) {
        val deleteConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_delete_comment_title,
                R.string.amity_delete_comment_warning_message,
                R.string.amity_delete, R.string.amity_cancel
            )
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        deleteConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    deleteComment(comment.getCommentId())
                }

                override fun onClickNegativeButton() {
                    // do nothing
                }
            }
    }

    private fun showDeleteReplyWarning(comment: AmityComment) {
        val deleteConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_delete_reply_title,
                R.string.amity_delete_reply_warning_message,
                R.string.amity_delete, R.string.amity_cancel
            )
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG)
        deleteConfirmationDialogFragment.listener =
            object : AmityAlertDialogFragment.IAlertDialogActionListener {
                override fun onClickPositiveButton() {
                    deleteComment(comment.getCommentId())
                }

                override fun onClickNegativeButton() {
                    // do nothing
                }
            }
    }

    internal open fun deleteComment(commentId: String) {
        getViewModel().deleteComment(
            commentId = commentId,
            onSuccess = {
                // do nothing
            },
            onError = {
                // display error
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun showDownloadingSnackBar(fileName: String) {
        val text = (requireContext().getString(R.string.amity_downloading_file)) + " " + fileName
        Snackbar.make(binding.recyclerViewFeed, text, Snackbar.LENGTH_SHORT).show()
    }
}