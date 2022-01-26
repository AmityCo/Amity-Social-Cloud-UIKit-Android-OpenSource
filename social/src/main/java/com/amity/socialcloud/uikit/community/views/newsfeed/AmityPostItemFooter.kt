package com.amity.socialcloud.uikit.community.views.newsfeed

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.components.AmityDividerItemDecor
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPostFooterBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityFullCommentAdapter
import com.amity.socialcloud.uikit.community.newsfeed.listener.*
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import kotlinx.android.synthetic.main.amity_item_post_footer.view.*


class AmityPostItemFooter : ConstraintLayout {

    private lateinit var mBinding: AmityItemPostFooterBinding
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
        mBinding = AmityItemPostFooterBinding.inflate(inflater, this, true)
        cbShare.setOnClickListener {
            shareListener?.onShareAction()
        }
    }

    private fun setNumberOfComments(commentCount: Int) {
        tvNumberOfComments.visibility = if (commentCount > 0) View.VISIBLE else View.GONE
        tvNumberOfComments.text = context.resources.getQuantityString(
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
        cbLike.setOnClickListener {
            val isLike = feed.getMyReactions().contains("like")
            refreshLikeView(!isLike)
            likeListener?.onLikeAction(!isLike)
        }
    }

    private fun refreshLikeView(isLike: Boolean) {
        cbLike.isChecked = isLike
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
        tvNumberOfLikes.visibility = if (reactionCount > 0) View.VISIBLE else View.GONE
        tvNumberOfLikes.text = context.resources.getQuantityString(
            R.plurals.amity_feed_number_of_likes,
            reactionCount,
            reactionCount.readableNumber()
        )
    }

    private fun setLikeCheckboxText() {
        if (cbLike.isChecked) {
            cbLike.setText(R.string.amity_liked)
        } else {
            cbLike.setText(R.string.amity_like)
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
        rvCommentFooter.addItemDecoration(spaceItemDecoration)
        rvCommentFooter.addItemDecoration(itemDecor)
        rvCommentFooter.layoutManager = LinearLayoutManager(context)
        rvCommentFooter.adapter = newsFeedCommentAdapter
        rvCommentFooter.visibility = GONE
        separator2.visibility = GONE
    }

    fun submitComments(commentList: PagedList<AmityComment>?, isScrollable: Boolean = false) {
        if (newsFeedCommentAdapter == null) {
            createAdapter()
        }
        newsFeedCommentAdapter!!.submitList(commentList)

        if (newsFeedCommentAdapter!!.itemCount > 0) {
            rvCommentFooter.visibility = VISIBLE
            separator2.visibility = VISIBLE
            if (isScrollable) {
                rvCommentFooter.smoothScrollToPosition(0)
            }
        } else {
            rvCommentFooter.visibility = GONE
            separator2.visibility = GONE
        }
    }

    fun setPreExpandComment(commentToExpand: String?) {
        this.commentToExpand = commentToExpand
    }

    fun setShowRepliesComment(showRepliesComment: Boolean) {
        this.showRepliesComment = showRepliesComment
    }

    fun showViewAllComment(isVisible: Boolean) {
        mBinding.showViewAllComment = isVisible
    }

    fun updateComment(comment: AmityComment) {
    }

}
