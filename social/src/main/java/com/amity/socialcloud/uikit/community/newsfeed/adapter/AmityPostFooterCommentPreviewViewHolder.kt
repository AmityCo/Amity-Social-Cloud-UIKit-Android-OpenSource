package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.common.toPagedList
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterCommentPreviewBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostFooterItem
import io.reactivex.rxjava3.subjects.PublishSubject

private const val MAXIMUM_COMMENTS_TO_SHOW = 2

class AmityPostFooterCommentPreviewViewHolder(
    private val binding: AmityItemPostFooterCommentPreviewBinding,
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val postEngagementClickPublisher: PublishSubject<PostEngagementClickEvent>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>
) : AmityPostFooterViewHolder(binding.root) {

    private val adapter = AmityPreviewCommentAdapter(userClickPublisher, commentContentClickPublisher, commentEngagementClickPublisher, commentOptionClickPublisher)

    override fun bind(data: AmityBasePostFooterItem, position: Int) {
        binding.executePendingBindings()
        val footerData = data as AmityBasePostFooterItem.COMMENT_PREVIEW
        adapter.isReadOnly = footerData.isReadOnly
        val commentList = footerData.post.getLatestComments()
        if (commentList.isNotEmpty()) {
            if(commentList.size > MAXIMUM_COMMENTS_TO_SHOW) {
                binding.viewAllCommentContainer.visibility = View.VISIBLE
                binding.viewAllCommentContainer.setOnClickListener {
                    postEngagementClickPublisher.onNext(PostEngagementClickEvent.Comment(footerData.post))
                }
            } else {
                binding.viewAllCommentContainer.visibility = View.GONE
            }
            val commentToDisplay = commentList.subList(0, Math.min(commentList.size, MAXIMUM_COMMENTS_TO_SHOW))
            submitComments(commentToDisplay.toPagedList(commentToDisplay.size))
        } else {
            binding.viewAllCommentContainer.visibility = View.GONE
            binding.rvCommentFooter.visibility = View.GONE
        }
    }

    private fun createAdapter() {
        binding.rvCommentFooter.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvCommentFooter.adapter = adapter
        binding.rvCommentFooter.visibility = View.GONE
    }

    private fun submitComments(commentList: PagedList<AmityComment>) {
        createAdapter()
        adapter.submitList(commentList)

        if (adapter.itemCount > 0) {
            binding.rvCommentFooter.visibility = View.VISIBLE
        } else {
            binding.rvCommentFooter.visibility = View.GONE
        }
    }

}