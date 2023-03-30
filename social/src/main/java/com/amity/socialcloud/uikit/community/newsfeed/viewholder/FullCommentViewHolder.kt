package com.amity.socialcloud.uikit.community.newsfeed.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.common.toPagedList
import com.amity.socialcloud.uikit.community.databinding.AmityItemFullCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityCommentReplyLoader
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityReplyCommentAdapter
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*


class FullCommentViewHolder(
    private val binding: AmityItemFullCommentBinding,
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>
) : RecyclerView.ViewHolder(binding.root) {

    private val replyDisposer = UUID.randomUUID().toString()
    private val loadMoreStateDisposer = UUID.randomUUID().toString()
    private val loaderDisposer = UUID.randomUUID().toString()
    private val replyAdapter = AmityReplyCommentAdapter(
        userClickPublisher,
        commentContentClickPublisher,
        commentEngagementClickPublisher,
        commentOptionClickPublisher
    )

    fun bind(comment: AmityComment?, loader: AmityCommentReplyLoader, isReadOnly: Boolean) {
        comment?.let {
            binding.viewComment.setComment(comment, null, isReadOnly)
            binding.viewComment.setEventPublishers(
                userClickPublisher,
                commentContentClickPublisher,
                commentEngagementClickPublisher,
                commentOptionClickPublisher
            )
            renderLoadMoreButton(comment, loader)
            renderReplies(loader, isReadOnly)
            loadReplies(loader, true)

        }
    }

    private fun renderReplies(loader: AmityCommentReplyLoader, isReadOnly: Boolean) {
        replyAdapter.isReadOnly = isReadOnly
        binding.recyclerViewReplies.layoutManager =
            LinearLayoutManager(binding.recyclerViewReplies.context)
        binding.recyclerViewReplies.adapter = replyAdapter
        loader.getComments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                replyAdapter.submitList(it.toPagedList(it.size))
            }
            .doOnError {
                // do nothing
            }
            .untilLifecycleEnd(this.itemView, replyDisposer)
            .subscribe()

    }

    private fun renderLoadMoreButton(comment: AmityComment, loader: AmityCommentReplyLoader) {
        binding.viewLoadProgress.visibility = View.GONE
        binding.viewMoreRepliesContainer.setOnClickListener {
            binding.viewLoadProgress.visibility = View.VISIBLE
            loadReplies(loader)
        }
        if (comment.getLatestReplies().isNotEmpty()) {
            binding.viewMoreRepliesContainer.visibility = View.VISIBLE
        } else {
            binding.viewMoreRepliesContainer.visibility = View.GONE
        }
        loader.showLoadMoreButton()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (it) {
                    binding.viewMoreRepliesContainer.visibility = View.VISIBLE
                } else {
                    binding.viewMoreRepliesContainer.visibility = View.GONE
                }
            }
            .doOnError {
                // do nothing
            }
            .untilLifecycleEnd(this.itemView, loadMoreStateDisposer)
            .subscribe()
    }

    private fun loadReplies(loader: AmityCommentReplyLoader, isReload: Boolean = false) {
        loader.load(isReload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                binding.viewLoadProgress.visibility = View.GONE
            }
            .doOnError {
                binding.viewLoadProgress.visibility = View.GONE
            }
            .untilLifecycleEnd(this.itemView, loaderDisposer)
            .subscribe()

    }

}