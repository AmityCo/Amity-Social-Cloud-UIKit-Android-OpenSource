package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.newsfeed.events.PollVoteClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostContentItem
import com.amity.socialcloud.uikit.feed.settings.AmityDefaultPostViewHolders
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class AmityPostContentAdapter(
    private val postContentClickPublisher: PublishSubject<PostContentClickEvent>,
    private val pollVoteClickPublisher: PublishSubject<PollVoteClickEvent>
) : RecyclerView.Adapter<AmityPostContentViewHolder>() {

    private val postList = ArrayList<AmityBasePostContentItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmityPostContentViewHolder {
        val viewHolder = AmitySocialUISettings.getViewHolder(viewType)
        val view =
            LayoutInflater.from(parent.context).inflate(viewHolder.getLayoutId(), parent, false)
        val holder = viewHolder.createViewHolder(view)
        holder.postContentClickPublisher = postContentClickPublisher
        holder.pollVoteClickPublisher = pollVoteClickPublisher
        return holder
    }

    override fun onBindViewHolder(holder: AmityPostContentViewHolder, position: Int) {
        val item = postList[position]
        holder.showFullContent = item.showFullContent
        holder.bind(item.post)
    }

    override fun getItemViewType(position: Int): Int {
        return postList[position].post.let { ekoPost ->
            if (!ekoPost.getChildren().isNullOrEmpty()) {
                when (ekoPost.getChildren().first().getData()) {
                    is AmityPost.Data.IMAGE -> {
                        AmityDefaultPostViewHolders.imageViewHolder.getDataType().hashCode()
                    }
                    is AmityPost.Data.FILE -> {
                        AmityDefaultPostViewHolders.fileViewHolder.getDataType().hashCode()
                    }
                    is AmityPost.Data.VIDEO -> {
                        AmityDefaultPostViewHolders.videoViewHolder.getDataType().hashCode()
                    }
                    is AmityPost.Data.POLL -> {
                        AmityDefaultPostViewHolders.pollViewHolder.getDataType().hashCode()
                    }
                    is AmityPost.Data.LIVE_STREAM -> {
                        AmityDefaultPostViewHolders.livestreamViewHolder.getDataType().hashCode()
                    }
                    else -> {
                        AmityDefaultPostViewHolders.textViewHolder.getDataType().hashCode()
                    }
                }
            } else {
                when (ekoPost.getData()) {
                    is AmityPost.Data.TEXT -> {
                        AmityDefaultPostViewHolders.textViewHolder.getDataType().hashCode()
                    }
                    is AmityPost.Data.CUSTOM -> {
                        (ekoPost.getData() as AmityPost.Data.CUSTOM).getDataType().hashCode()
                    }
                    else -> {
                        AmityDefaultPostViewHolders.unknownViewHolder.getDataType().hashCode()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = postList.size

    fun submitList(newList: List<AmityBasePostContentItem>) {
        setItems(newList, DiffCallback(postList, newList))
    }

    fun setItems(listItems: List<AmityBasePostContentItem>, diffCallBack: DiffUtil.Callback) {
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        postList.clear()
        postList.addAll(listItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getPostContentClickEvents(): Flowable<PostContentClickEvent> {
        return postContentClickPublisher.toFlowable(BackpressureStrategy.BUFFER)
    }

    class DiffCallback(
        private val oldList: List<AmityBasePostContentItem>,
        private val newList: List<AmityBasePostContentItem>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].post.getPostId() == newList[newItemPosition].post.getPostId()
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem =  newList[newItemPosition]
            return (oldItem.post.getEditedAt() == newItem.post.getEditedAt()
                    && oldItem.showFullContent == newItem.showFullContent)
        }
    }
}