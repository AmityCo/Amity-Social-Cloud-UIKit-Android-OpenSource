package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.databinding.AmityItemDeletedReplyCommentBinding
import com.amity.socialcloud.uikit.community.databinding.AmityItemReplyCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.diffutil.PostCommentDiffUtil
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.viewholder.DeletedReplyCommentViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.viewholder.ReplyCommentViewHolder
import io.reactivex.subjects.PublishSubject
import java.util.*

class AmityReplyCommentAdapter(
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>,
    var isReadOnly: Boolean = false
) : PagedListAdapter<AmityComment, RecyclerView.ViewHolder>(PostCommentDiffUtil()) {

    private val REPLY_COMMENT_TYPE = Random().nextInt()
    private val DELETED_REPLY_COMMENT_TYPE = Random().nextInt()

    override fun getItemViewType(position: Int): Int {
        val comment = getItem(position)
        return if (comment?.isDeleted() ?: true) {
            DELETED_REPLY_COMMENT_TYPE
        } else {
            REPLY_COMMENT_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            REPLY_COMMENT_TYPE -> {
                val itemBinding = AmityItemReplyCommentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
                )
                return ReplyCommentViewHolder(itemBinding, userClickPublisher, commentContentClickPublisher, commentEngagementClickPublisher, commentOptionClickPublisher)
            }
            else -> {
                val itemBinding = AmityItemDeletedReplyCommentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
                )
                return DeletedReplyCommentViewHolder(itemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ReplyCommentViewHolder) {
            holder.bind(getItem(position), isReadOnly)
        }
    }

}