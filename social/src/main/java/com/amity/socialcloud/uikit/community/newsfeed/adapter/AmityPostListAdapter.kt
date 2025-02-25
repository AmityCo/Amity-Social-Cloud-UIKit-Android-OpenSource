package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PollVoteClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostReviewClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.ReactionCountClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityPostListAdapter(
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val communityClickPublisher: PublishSubject<AmityCommunity>,
    private val postEngagementClickPublisher: PublishSubject<PostEngagementClickEvent>,
    private val postContentClickPublisher: PublishSubject<PostContentClickEvent>,
    private val postOptionClickPublisher: PublishSubject<PostOptionClickEvent>,
    private val postReviewClickPublisher: PublishSubject<PostReviewClickEvent>,
    private val pollVoteClickPublisher: PublishSubject<PollVoteClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>,
    private val reactionCountClickPublisher: PublishSubject<ReactionCountClickEvent>
) : PagingDataAdapter<AmityBasePostItem, AmityPostViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityPostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.amity_item_base_post, parent, false)
        return AmityPostViewHolder(
            view,
            userClickPublisher,
            communityClickPublisher,
            postEngagementClickPublisher,
            postContentClickPublisher,
            postOptionClickPublisher,
            postReviewClickPublisher,
            pollVoteClickPublisher,
            commentEngagementClickPublisher,
            commentContentClickPublisher,
            commentOptionClickPublisher,
            reactionCountClickPublisher
        )
    }

    override fun onBindViewHolder(holder: AmityPostViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<AmityBasePostItem>() {
            override fun areItemsTheSame(
                oldItem: AmityBasePostItem,
                newItem: AmityBasePostItem
            ): Boolean {
                return oldItem.post.getPostId() == newItem.post.getPostId()
            }


            override fun areContentsTheSame(
                oldItem: AmityBasePostItem,
                newItem: AmityBasePostItem
            ): Boolean {
                // Do not change
                // Delegate diff check to lower level adapters (header, content, footer)
                return false
            }

        }
    }
}