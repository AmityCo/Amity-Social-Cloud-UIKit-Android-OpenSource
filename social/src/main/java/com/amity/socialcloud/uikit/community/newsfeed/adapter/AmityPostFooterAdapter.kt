package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterCommentPreviewBinding
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterPostEngagementBinding
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterPostReviewBinding
import com.amity.socialcloud.uikit.community.newsfeed.diffutil.PostCommentDiffUtil
import com.amity.socialcloud.uikit.community.newsfeed.events.*
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostFooterItem
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*


class AmityPostFooterAdapter(
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val postEngagementClickPublisher: PublishSubject<PostEngagementClickEvent>,
    private val postReviewClickPublisher: PublishSubject<PostReviewClickEvent>,
    private val commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
    private val commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
    private val commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>
) : RecyclerView.Adapter<AmityPostFooterViewHolder>() {

    private val list: ArrayList<AmityBasePostFooterItem> = arrayListOf()
    private val POST_ENGAGEMENT = Random().nextInt()
    private val COMMENT_PREVIEW = Random().nextInt()
    private val POST_REVIEW = Random().nextInt()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmityPostFooterViewHolder {
        return when (viewType) {
            POST_ENGAGEMENT -> {
                val itemBinding = DataBindingUtil.inflate<AmityItemPostFooterPostEngagementBinding>(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.amity_item_post_footer_post_engagement,
                    parent,
                    false
                )
                AmityPostFooterPostEngagementViewHolder(itemBinding, postEngagementClickPublisher)
            }
            COMMENT_PREVIEW -> {
                val itemBinding = AmityItemPostFooterCommentPreviewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
                )
                AmityPostFooterCommentPreviewViewHolder(
                    itemBinding,
                    userClickPublisher,
                    postEngagementClickPublisher,
                    commentContentClickPublisher,
                    commentEngagementClickPublisher,
                    commentOptionClickPublisher
                )
            }
            POST_REVIEW -> {
                val itemBinding = AmityItemPostFooterPostReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AmityPostFooterPostReviewViewHolder(itemBinding, postReviewClickPublisher)
            }
            else -> {
                val itemBinding = DataBindingUtil.inflate<AmityItemPostFooterPostEngagementBinding>(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.amity_item_post_footer_post_engagement,
                    parent,
                    false
                )
                AmityPostFooterPostEngagementViewHolder(
                    itemBinding,
                    postEngagementClickPublisher,
                )
            }
        }
    }

    override fun onBindViewHolder(holder: AmityPostFooterViewHolder, position: Int) {
        holder.bind(list.get(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        return when (list.get(position)) {
            is AmityBasePostFooterItem.POST_ENGAGEMENT -> {
                POST_ENGAGEMENT
            }
            is AmityBasePostFooterItem.COMMENT_PREVIEW -> {
                COMMENT_PREVIEW
            }
            is AmityBasePostFooterItem.POST_REVIEW -> {
                POST_REVIEW
            }
        }
    }

    fun submitList(newList: List<AmityBasePostFooterItem>) {
        setItems(newList, DiffCallback(list, newList))
    }

    fun setItems(listItems: List<AmityBasePostFooterItem>, diffCallBack: DiffUtil.Callback) {
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        list.clear()
        list.addAll(listItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class DiffCallback(
        private val oldList: List<AmityBasePostFooterItem>,
        private val newList: List<AmityBasePostFooterItem>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return (oldItem is AmityBasePostFooterItem.POST_ENGAGEMENT
                    && newItem is AmityBasePostFooterItem.POST_ENGAGEMENT
                    && oldItem.post.getPostId() == newItem.post.getPostId())
                    ||
                    (oldItem is AmityBasePostFooterItem.COMMENT_PREVIEW
                            && newItem is AmityBasePostFooterItem.COMMENT_PREVIEW
                            && oldItem.post.getPostId() == newItem.post.getPostId())
                    ||
                    (oldItem is AmityBasePostFooterItem.POST_REVIEW
                            && newItem is AmityBasePostFooterItem.POST_REVIEW
                            && oldItem.post.getPostId() == newItem.post.getPostId())

        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return when (oldItem) {
                is AmityBasePostFooterItem.POST_ENGAGEMENT -> {
                    val castedNewItem = newItem as AmityBasePostFooterItem.POST_ENGAGEMENT
                    oldItem.post.getCommentCount() == castedNewItem.post.getCommentCount()
                            && oldItem.post.getMyReactions().contains(AmityConstants.POST_REACTION) == castedNewItem.post.getMyReactions().contains(AmityConstants.POST_REACTION)
                            && oldItem.isReadOnly == castedNewItem.isReadOnly
                }
                is AmityBasePostFooterItem.COMMENT_PREVIEW -> {
                    val castedNewItem = newItem as AmityBasePostFooterItem.COMMENT_PREVIEW
                    PostCommentDiffUtil().areChildrenTheSame(oldItem.post.getLatestComments(), castedNewItem.post.getLatestComments())
                            && oldItem.isReadOnly == castedNewItem.isReadOnly
                }
                is AmityBasePostFooterItem.POST_REVIEW -> {
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
