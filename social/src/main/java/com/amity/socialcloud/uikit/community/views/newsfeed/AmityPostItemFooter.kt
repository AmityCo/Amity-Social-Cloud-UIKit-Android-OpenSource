package com.amity.socialcloud.uikit.community.views.newsfeed

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.components.AmityDividerItemDecor
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityFullCommentAdapter
import com.amity.socialcloud.uikit.community.newsfeed.listener.*
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AmityPostItemFooter : ConstraintLayout {

    private lateinit var binding: AmityItemPostFooterBinding
    private var newsFeedCommentAdapter: AmityFullCommentAdapter? = null

    private var commentItemClickListener: AmityPostCommentItemClickListener? = null
    private var shareListener: AmityPostActionShareListener? = null
    private var showMoreActionListener: AmityPostCommentShowMoreActionListener? = null
    private var showAllReplyListener: AmityPostCommentShowAllReplyListener? = null
    private var commentReplyClickListener: AmityPostCommentReplyClickListener? = null
    private var commentToExpand: String? = null
    private var readOnlyView: Boolean = false
    private var showRepliesComment: Boolean = false
    private var postId: String = ""
    var likeListener: AmityPostActionLikeListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AmityItemPostFooterBinding.inflate(inflater, this, true)
        binding.cbShare.setOnClickListener {
            shareListener?.onShareAction()
        }
    }

    private fun setNumberOfComments(commentCount: Int) {
        binding.tvNumberOfComments.visibility = if (commentCount > 0) View.VISIBLE else View.GONE
        binding.tvNumberOfComments.text = context.resources.getQuantityString(
            R.plurals.amity_feed_number_of_comments,
            commentCount,
            commentCount
        )
    }

    fun setPost(post: AmityPost) {

    }

    private fun setUpLikeView(feed: AmityPost) {
        val isLike = feed.getMyReactions().contains("like")
        refreshLikeView(isLike)
        setLikeClickListener(feed)
    }

    private fun setLikeClickListener(feed: AmityPost) {
        binding.cbLike.setOnClickListener {
            val isLike = feed.getMyReactions().contains("like")
            refreshLikeView(!isLike)
            likeListener?.onLikeAction(!isLike)
        }
    }

    private fun refreshLikeView(isLike: Boolean) {
        binding.cbLike.isChecked = isLike
        setLikeCheckboxText()
    }

    private fun isShowShareButton(post: AmityPost): Boolean {
        val targetPost = post.getTarget()
        if (targetPost is AmityPost.Target.USER && targetPost.getUser()
                ?.getUserId() == AmityCoreClient.getUserId()
        ) {
            return AmitySocialUISettings.postSharingSettings.myFeedPostSharingTarget.isNotEmpty()
        } else if (targetPost is AmityPost.Target.USER && targetPost.getUser()
                ?.getUserId() != AmityCoreClient.getUserId()
        ) {
            return AmitySocialUISettings.postSharingSettings.userFeedPostSharingTarget.isNotEmpty()
        } else {
            if (targetPost is AmityPost.Target.COMMUNITY) {
                targetPost.getCommunity()?.let {
                    return if (it.isPublic()) {
                        AmitySocialUISettings.postSharingSettings.publicCommunityPostSharingTarget.isNotEmpty()
                    } else {
                        AmitySocialUISettings.postSharingSettings.privateCommunityPostSharingTarget.isNotEmpty()
                    }
                }
            }
        }
        return false
    }

    private fun setNumberOfLikes(reactionCount: Int) {
        binding.tvNumberOfLikes.visibility = if (reactionCount > 0) View.VISIBLE else View.GONE
        binding.tvNumberOfLikes.text = context.resources.getQuantityString(
            R.plurals.amity_feed_number_of_likes,
            reactionCount,
            reactionCount.readableNumber()
        )
    }

    private fun setLikeCheckboxText() {
        if (binding.cbLike.isChecked) {
            binding.cbLike.setText(R.string.amity_liked)
        } else {
            binding.cbLike.setText(R.string.amity_like)
        }
    }

    fun setItemClickListener(itemClickListener: AmityPostCommentItemClickListener?) {
        this.commentItemClickListener = itemClickListener
    }

    fun setShowAllReplyListener(showAllReplyListener: AmityPostCommentShowAllReplyListener?) {
        this.showAllReplyListener = showAllReplyListener
    }

    fun setShowMoreActionListener(showMoreActionListener: AmityPostCommentShowMoreActionListener?) {
        this.showMoreActionListener = showMoreActionListener
    }

    fun setFeedLikeActionListener(likeListener: AmityPostActionLikeListener) {
        this.likeListener = likeListener
    }

    fun setCommentClickListener(commentReplyClickListener: AmityPostCommentReplyClickListener) {
        this.commentReplyClickListener = commentReplyClickListener
    }

    fun setFeedShareActionListener(shareListener: AmityPostActionShareListener?) {
        this.shareListener = shareListener
    }

    fun setCommentActionListener(
        itemClickListener: AmityPostCommentItemClickListener?,
        showAllReplyListener: AmityPostCommentShowAllReplyListener?,
        showMoreActionListener: AmityPostCommentShowMoreActionListener?,
        commentReplyClickListener: AmityPostCommentReplyClickListener?
    ) {
        this.commentItemClickListener = itemClickListener
        this.showMoreActionListener = showMoreActionListener
        this.showAllReplyListener = showAllReplyListener
        this.commentReplyClickListener = commentReplyClickListener
    }

    private fun createAdapter() {
        newsFeedCommentAdapter = null
        newsFeedCommentAdapter?.setHasStableIds(true)

        val space8 = resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
        val space16 = resources.getDimensionPixelSize(R.dimen.amity_padding_m1)
        val spaceItemDecoration = AmityRecyclerViewItemDecoration(space8, space16, 0, space16)
        val itemDecor = AmityDividerItemDecor(context)
        binding.rvCommentFooter.addItemDecoration(spaceItemDecoration)
        binding.rvCommentFooter.addItemDecoration(itemDecor)
        binding.rvCommentFooter.layoutManager = LinearLayoutManager(context)
        binding.rvCommentFooter.adapter = newsFeedCommentAdapter
        binding.rvCommentFooter.visibility = GONE
        binding.separator2.visibility = GONE
    }

    fun submitComments(commentList: PagingData<AmityComment>, isScrollable: Boolean = false) {
        if (newsFeedCommentAdapter == null) {
            createAdapter()
        }
        CoroutineScope(Dispatchers.IO).launch {
            newsFeedCommentAdapter!!.submitData(commentList)
        }

        if (newsFeedCommentAdapter!!.itemCount > 0) {
            binding.rvCommentFooter.visibility = VISIBLE
            binding.separator2.visibility = VISIBLE
            if (isScrollable) {
                binding.rvCommentFooter.smoothScrollToPosition(0)
            }
        } else {
            binding.rvCommentFooter.visibility = GONE
            binding.separator2.visibility = GONE
        }
    }

    fun setPreExpandComment(commentToExpand: String?) {
        this.commentToExpand = commentToExpand
    }

    fun setShowRepliesComment(showRepliesComment: Boolean) {
        this.showRepliesComment = showRepliesComment
    }

    fun showViewAllComment(isVisible: Boolean) {
        binding.showViewAllComment = isVisible
    }

    fun updateComment(comment: AmityComment) {
    }

}
