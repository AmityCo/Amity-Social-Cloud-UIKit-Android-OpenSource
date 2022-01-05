package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.uikit.community.databinding.AmityItemDeletedRootCommentBinding
import com.amity.socialcloud.uikit.community.databinding.AmityItemFullCommentBinding
import com.amity.socialcloud.uikit.community.newsfeed.diffutil.PostCommentDiffUtil
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.viewholder.DeletedCommentViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.viewholder.FullCommentViewHolder
import io.reactivex.subjects.PublishSubject
import java.util.*


class AmityFullCommentAdapter(
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>,
    var isReadOnly: Boolean = false,
) : PagedListAdapter<AmityComment, RecyclerView.ViewHolder>(PostCommentDiffUtil()) {

    private val FULL_COMMENT_TYPE = Random().nextInt()
    private val DELETE_COMMENT_TYPE = Random().nextInt()
    private val loaderMap = hashMapOf<String, AmityCommentReplyLoader>()

    override fun getItemViewType(position: Int): Int {
        val comment = getItem(position)
        return if (comment?.isDeleted() ?: true) {
            DELETE_COMMENT_TYPE
        } else {
            FULL_COMMENT_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            FULL_COMMENT_TYPE -> {
                val itemBinding = AmityItemFullCommentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
                )
                return FullCommentViewHolder(itemBinding, userClickPublisher, commentContentClickPublisher, commentEngagementClickPublisher, commentOptionClickPublisher)
            }
            else -> {
                val itemBinding = AmityItemDeletedRootCommentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
                )
                return DeletedCommentViewHolder(itemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FullCommentViewHolder) {
            val comment = getItem(position)
            comment?.let {
                var loader = loaderMap.get(it.getCommentId())
                if(loader == null) {
                    loader = AmityCommentReplyLoader(it)
                    loaderMap.put(it.getCommentId(), loader)
                }
                holder.bind(comment, loader, isReadOnly)
            }

        }
    }

}