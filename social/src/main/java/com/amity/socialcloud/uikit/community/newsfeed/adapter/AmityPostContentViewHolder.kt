package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.text.Spannable
import android.text.SpannableString
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.views.text.AmityExpandableTextView
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.events.PollVoteClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityMentionClickableSpan
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

open class AmityPostContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal var postContentClickPublisher = PublishSubject.create<PostContentClickEvent>()
    internal var pollVoteClickPublisher = PublishSubject.create<PollVoteClickEvent>()
    internal var showFullContent = false

    open fun bind(post: AmityPost) {

    }

    internal fun setPostText(data: AmityPost, showCompleteText: Boolean) {
        val tvPost = itemView.findViewById<AmityExpandableTextView>(R.id.tvFeed)
        setPostTextToTextView(tvPost, data, showCompleteText)
    }
    
    internal fun setPostTextToTextView(tvPost: AmityExpandableTextView, data: AmityPost, showCompleteText: Boolean) {
        tvPost.text = getHighlightTextUserMentions(data)
        if (showCompleteText) {
            tvPost.showCompleteText()
            tvPost.tag = tvPost.getVisibleLineCount()
        }

        if (tvPost.tag != tvPost.getVisibleLineCount()) {
            tvPost.forceLayout()
            tvPost.tag = tvPost.getVisibleLineCount()
        }

        tvPost.isVisible = tvPost.text.isNotEmpty()

        tvPost.setExpandOnlyOnReadMoreClick(true)

        tvPost.setOnClickListener {
            if (tvPost.isReadMoreClicked()) {
                tvPost.showCompleteText()
                tvPost.tag = tvPost.getVisibleLineCount()
            } else {
                postContentClickPublisher.onNext(PostContentClickEvent.Text(data))
            }

        }
    }

    private fun getHighlightTextUserMentions(post: AmityPost): SpannableString {
        val postText = (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
        val spannable = SpannableString(postText)
        if (spannable.isNotEmpty() && post.getMetadata() != null) {
            val mentionUserIds = post.getMentionees().map { (it as? AmityMentionee.USER)?.getUserId() }
            val mentionedUsers = AmityMentionMetadataGetter(post.getMetadata()!!).getMentionedUsers()
            val mentions = mentionedUsers.filter { mentionUserIds.contains(it.getUserId()) }
            mentions.forEach { mentionUserItem ->
                try {
                    spannable.setSpan(AmityMentionClickableSpan(mentionUserItem.getUserId()),
                        mentionUserItem.getIndex(),
                        mentionUserItem.getIndex().plus(mentionUserItem.getLength()).inc(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                } catch (exception: IndexOutOfBoundsException) {
                    Timber.e("AmityPostContentViewHolder", "Highlight text user mentions crashes")
                }
            }
        }
        return spannable
    }
}