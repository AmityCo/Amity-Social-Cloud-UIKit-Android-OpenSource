package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.events.*
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostItem
import com.amity.socialcloud.uikit.community.newsfeed.view.AmityBasePostView
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityPostViewHolder constructor(
    itemView: View,
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val communityClickPublisher: PublishSubject<AmityCommunity>,
    private val postEngagementClickPublisher: PublishSubject<PostEngagementClickEvent>,
    private val postContentClickPublisher: PublishSubject<PostContentClickEvent>,
    private val postOptionClickPublisher: PublishSubject<PostOptionClickEvent>,
    private val postReviewClickPublisher: PublishSubject<PostReviewClickEvent>,
    private val pollVoteClickPublisher: PublishSubject<PollVoteClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewAdapter.IBinder<AmityBasePostItem> {

    private val headerAdapter = AmityPostHeaderAdapter(
        userClickPublisher,
        communityClickPublisher,
        postOptionClickPublisher
    )

    private val contentAdapter = AmityPostContentAdapter(
        postContentClickPublisher,
        pollVoteClickPublisher
    )

    /*
        ConcatAdapter with headerAdapter contentAdapter and footerAdapter
        has an issue with re-rendering footer's post engagement on re-binding a viewHolder.

        Adding a dummyAdapter is the hotfix.

        To remove the dummyAdapter after finding the proper solution
     */
    private val dummyAdapter = AmityPostDummyAdapter()

    private val footerAdapter = AmityPostFooterAdapter(
        userClickPublisher,
        postEngagementClickPublisher,
        postReviewClickPublisher,
        commentContentClickPublisher,
        commentEngagementClickPublisher,
        commentOptionClickPublisher
    )

    private var concatAdapter: ConcatAdapter? = null

    override fun bind(data: AmityBasePostItem?, position: Int) {
        if (data == null) {
            return
        }
        val basePostView = itemView.findViewById<AmityBasePostView>(R.id.basePostView)
        if (concatAdapter == null) {
//            To check if special config needed
//            val config = ConcatAdapter.Config.Builder().apply {
//                this.setIsolateViewTypes(false)
//            }.build()
            concatAdapter = ConcatAdapter(headerAdapter,
                contentAdapter,
                dummyAdapter,
                footerAdapter
            )
            basePostView.layoutManager = LinearLayoutManager(this.itemView.context)
            basePostView.adapter = concatAdapter
        }
        headerAdapter.submitList(data.headerItems)
        contentAdapter.submitList(data.contentItems)
        dummyAdapter.setItems(listOf())
        footerAdapter.submitList(data.footerItems)
    }
}