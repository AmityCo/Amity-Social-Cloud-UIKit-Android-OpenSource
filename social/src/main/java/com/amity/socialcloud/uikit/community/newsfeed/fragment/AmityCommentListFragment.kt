package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.comment.AmityCommentSortOption
import com.amity.socialcloud.uikit.common.base.createFragmentViewModel
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.dialog.AmityAlertDialogFragment
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommentListBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityEditCommentActivity
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityFullCommentAdapter
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityCommentRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommentItemListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityCommentListViewModel
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityGlobalFeedViewModel
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AmityCommentListFragment : RxFragment() {

    lateinit var binding: AmityFragmentCommentListBinding
    private val userClickPublisher = PublishSubject.create<AmityUser>()
    private val commentEngagementClickPublisher =
        PublishSubject.create<CommentEngagementClickEvent>()
    private val commentContentClickPublisher = PublishSubject.create<CommentContentClickEvent>()
    private val commentOptionClickPublisher = PublishSubject.create<CommentOptionClickEvent>()
    private val adapter = AmityFullCommentAdapter(
        userClickPublisher,
        commentContentClickPublisher,
        commentEngagementClickPublisher,
        commentOptionClickPublisher
    )
    lateinit var viewModel: AmityCommentListViewModel
    private val commentDisposer = UUID.randomUUID().toString()

    private var commentEditContract =
        registerForActivityResult(AmityEditCommentActivity.AmityEditCommentContract()) { comment ->
            // no need for this if update comes from Flowable
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentCommentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AmityCommentListViewModel::class.java)
        setupCommentList()
        observeEvents()
    }

    override fun onPause() {
        sendReactionRequests()
        super.onPause()
    }

    private fun setupCommentList() {
        adapter.isReadOnly = viewModel.isReadOnly
        binding.recyclerViewComment.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewComment.adapter = adapter
        binding.recyclerViewComment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    sendReactionRequests()
                }
            }
        })
        refresh()
    }

    private fun refresh() {
        AmitySocialClient.newCommentRepository()
            .getComments()
            .post(viewModel.postId)
            .parentId(null)
            .sortBy(AmityCommentSortOption.LAST_CREATED)
            .build()
            .getPagingData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                CoroutineScope(Dispatchers.IO).launch {
                    adapter.submitData(it)
                }
            }
            .untilLifecycleEnd(this, commentDisposer)
            .subscribe()
    }

    private fun observeEvents() {
        userClickPublisher.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                AmitySocialUISettings.globalUserClickListener.onClickUser(this, it)
            }
            .untilLifecycleEnd(this)
            .subscribe()

        commentEngagementClickPublisher.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                when (it) {
                    is CommentEngagementClickEvent.Reaction -> {
                        viewModel.commentReactionEventMap.put(it.comment.getCommentId(), it)
                    }
                    is CommentEngagementClickEvent.Reply -> {
                        viewModel.commentItemListener.onClickReply(it.comment, this)
                    }
                }
            }
            .untilLifecycleEnd(this)
            .subscribe()

        commentOptionClickPublisher.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                showCommentOptions(it.comment)
            }
            .untilLifecycleEnd(this)
            .subscribe()

        viewModel.refreshEvents
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                refresh()
            }
            .untilLifecycleEnd(this)
            .subscribe()

    }

    private fun showCommentOptions(comment: AmityComment) {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        bottomSheet.show(viewModel.getCommentOptionMenuItems(
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
        viewModel.reportComment(comment, {
            view?.showSnackBar(getString(R.string.amity_report_sent))
        }, {})
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun unReportComment(comment: AmityComment) {
        viewModel.unReportComment(comment, {
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
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG);
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
        deleteConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG);
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

    private fun deleteComment(commentId: String) {
        viewModel.deleteComment(
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

    private fun sendReactionRequests() {
        sendCommentReactionRequests()
    }

    private fun sendCommentReactionRequests() {
        val reactionEvents = viewModel.commentReactionEventMap.values
        reactionEvents.forEach {
            val isAdding = it.isAdding
            val isReactedByMe = it.comment.getMyReactions().contains(AmityConstants.POST_REACTION)
            if (isAdding && !isReactedByMe) {
                viewModel.addCommentReaction(comment = it.comment)
                    .untilLifecycleEnd(this)
                    .subscribe()
            } else if (!isAdding && isReactedByMe) {
                viewModel.removeCommentReaction(comment = it.comment)
                    .untilLifecycleEnd(this)
                    .subscribe()
            }
        }
        viewModel.commentReactionEventMap.clear()
    }


    class Builder internal constructor(private val postId: String) {

        private var isReadOnly: Boolean = false
        private var commentListRefreshEvents = Flowable.never<AmityCommentRefreshEvent>()
        private var commentItemListener: AmityCommentItemListener =
            object : AmityCommentItemListener {
                override fun onClickReply(comment: AmityComment, fragment: Fragment) {
                    // to refer to global as default
                }
            }

        internal fun readOnlyMode(isEnable: Boolean): Builder {
            return apply { this.isReadOnly = isEnable }
        }

        internal fun commentListRefreshEvents(commentListRefreshEvents: Flowable<AmityCommentRefreshEvent>): Builder {
            return apply { this.commentListRefreshEvents = commentListRefreshEvents }
        }

        internal fun commentItemListener(commentItemListener: AmityCommentItemListener): Builder {
            return apply { this.commentItemListener = commentItemListener }
        }

        fun build(activity: AppCompatActivity): AmityCommentListFragment {
            val fragment = AmityCommentListFragment()
            val viewModel = ViewModelProvider(activity).get(AmityCommentListViewModel::class.java)
            viewModel.postId = postId
            viewModel.refreshEvents = commentListRefreshEvents
            viewModel.commentItemListener = commentItemListener
            viewModel.isReadOnly = isReadOnly
            return fragment
        }
    }

    companion object {

        fun newInstance(postId: String): Builder {
            return Builder(postId)
        }
    }


}