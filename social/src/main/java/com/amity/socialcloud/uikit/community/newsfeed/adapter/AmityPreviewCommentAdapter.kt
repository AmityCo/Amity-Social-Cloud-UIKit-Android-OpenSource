package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.databinding.AmityItemDeletedRootCommentBinding
import com.amity.socialcloud.uikit.community.databinding.AmityItemPreviewCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.diffutil.PostCommentDiffUtil
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.viewholder.DeletedCommentViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.viewholder.PreviewCommentViewHolder
import io.reactivex.subjects.PublishSubject
import java.util.*


class AmityPreviewCommentAdapter(
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>,
    var isReadOnly: Boolean = false,
) : PagedListAdapter<AmityComment, RecyclerView.ViewHolder>(PostCommentDiffUtil()) {

    private val PREVIEW_COMMENT_TYPE = Random().nextInt()
    private val DELETE_COMMENT_TYPE = Random().nextInt()

    override fun getItemViewType(position: Int): Int {
        val comment = getItem(position)
        return if (comment?.isDeleted() != false) {
            DELETE_COMMENT_TYPE
        } else {
            PREVIEW_COMMENT_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            PREVIEW_COMMENT_TYPE -> {
                val itemBinding = AmityItemPreviewCommentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
                )
                return PreviewCommentViewHolder(itemBinding, userClickPublisher, commentContentClickPublisher, commentEngagementClickPublisher, commentOptionClickPublisher)
            }
            else -> {
                val itemBinding = AmityItemDeletedRootCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DeletedCommentViewHolder(itemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PreviewCommentViewHolder) {
            holder.bind(getItem(position), isReadOnly)
        }
    }

}