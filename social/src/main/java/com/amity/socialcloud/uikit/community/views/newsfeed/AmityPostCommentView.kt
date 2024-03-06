package com.amity.socialcloud.uikit.community.views.newsfeed

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.role.AmityRoles
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableFeedPostTime
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCommentNewsFeedBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.ReactionCountClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityMentionClickableSpan
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class AmityPostCommentView : ConstraintLayout {

    private lateinit var binding: AmityItemCommentNewsFeedBinding
    private var userClickPublisher = PublishSubject.create<AmityUser>()
    private var commentContentClickPublisher = PublishSubject.create<CommentContentClickEvent>()
    private var commentEngagementClickPublisher =
        PublishSubject.create<CommentEngagementClickEvent>()
    private var commentOptionClickPublisher = PublishSubject.create<CommentOptionClickEvent>()
    private var reactionCountClickPublisher = PublishSubject.create<ReactionCountClickEvent>()

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.amity_item_comment_news_feed, this, true)
    }

    fun setComment(comment: AmityComment, post: AmityPost? = null, isReadOnly: Boolean? = false) {
        binding.avatarUrl = comment.getCreator()?.getAvatar()?.getUrl(AmityImage.Size.SMALL)
        binding.tvUserName.text =
            comment.getCreator()?.getDisplayName() ?: context.getString(R.string.amity_anonymous)
        binding.tvCommentTime.text = comment.getCreatedAt().millis.readableFeedPostTime(context)
        binding.edited = comment.isEdited()
        binding.isFailed = comment.getState() == AmityComment.State.FAILED
        binding.isReplyComment = !comment.getParentId().isNullOrEmpty()

        val banIcon = if (comment.getCreator()?.isGlobalBan() == true) {
            ContextCompat.getDrawable(context, R.drawable.amity_ic_ban)
        } else {
            null
        }
        binding.tvUserName.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            banIcon,
            null
        )

        setText(comment)
        renderModBadge(comment)
        val isReactedByMe = comment.getMyReactions().contains(AmityConstants.POST_REACTION)
        setUpLikeView(
            isReactedByMe,
            comment.getReactionCount(),
            comment
        )
        setReadOnlyMode(isReadOnly!!)
        setViewListeners(comment, post)
    }

    private fun renderModBadge(comment: AmityComment) {
        val roles =
            (comment.getTarget() as? AmityComment.Target.COMMUNITY)?.getCreatorMember()?.getRoles()
        if (isCommunityModerator(roles)) {
            binding.tvCommentBy.visibility = View.VISIBLE
        } else {
            binding.tvCommentBy.visibility = View.GONE
        }
    }

    private fun isCommunityModerator(roles: AmityRoles?): Boolean {
        return roles?.any {
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
        } ?: false
    }

    private fun setViewListeners(comment: AmityComment, post: AmityPost?) {

        binding.ivAvatar.setOnClickListener {
            comment.getCreator()?.let {
                userClickPublisher.onNext(it)
            }
        }
        binding.tvUserName.setOnClickListener {
            comment.getCreator()?.let {
                userClickPublisher.onNext(it)
            }
        }

        binding.tvPostComment.setOnClickListener {
            if (binding.tvPostComment.isReadMoreClicked()) {
                binding.tvPostComment.showCompleteText()
            } else {
                commentContentClickPublisher.onNext(CommentContentClickEvent.Text(comment, post))
            }
        }

        binding.layoutCommentItem.setOnClickListener {
            commentContentClickPublisher.onNext(CommentContentClickEvent.Text(comment, post))
        }

        binding.reply.setOnClickListener {
            commentEngagementClickPublisher.onNext(CommentEngagementClickEvent.Reply(comment, post))
        }

        binding.btnCommentAction.setOnClickListener {
            commentOptionClickPublisher.onNext(CommentOptionClickEvent(comment))
        }

        binding.ivCommentSyncFailed.setOnClickListener {
            commentOptionClickPublisher.onNext(CommentOptionClickEvent(comment))
        }

        binding.tvNumberOfReactions.setOnClickListener {
            reactionCountClickPublisher.onNext(ReactionCountClickEvent.Comment(comment))
        }
    }

    private fun setUpLikeView(isReactedByMe: Boolean, reactionCount: Int, comment: AmityComment) {
        refreshLikeView(isReactedByMe)
        setLikeClickListener(isReactedByMe, reactionCount, comment)
    }

    private fun refreshLikeView(isLike: Boolean) {
        binding.cbLike.isChecked = isLike
        setLikeCheckboxText()
    }

    private fun setLikeCheckboxText() {
        if (binding.cbLike.isChecked) {
            binding.cbLike.setText(R.string.amity_liked)
        } else {
            binding.cbLike.setText(R.string.amity_like)
        }
    }

    private fun setLikeClickListener(
        isReactedByMe: Boolean,
        reactionCount: Int,
        comment: AmityComment
    ) {
        val convertedValue = !isReactedByMe
        binding.cbLike.setOnClickListener {
            var displayReactionCount = reactionCount + 1
            var reactionEvent = CommentEngagementClickEvent.Reaction(comment, true)
            if (!convertedValue) {
                displayReactionCount = Math.max(reactionCount - 1, 0)
                reactionEvent = CommentEngagementClickEvent.Reaction(comment, false)
            }
            commentEngagementClickPublisher.onNext(reactionEvent)
            setNumberOfReactions(displayReactionCount)
            setUpLikeView(convertedValue, displayReactionCount, comment)
        }
    }


    fun setEventPublishers(
        userClickPublisher: PublishSubject<AmityUser>,
        commentContentClickPublisher: PublishSubject<CommentContentClickEvent>,
        commentEngagementClickPublisher: PublishSubject<CommentEngagementClickEvent>,
        commentOptionClickPublisher: PublishSubject<CommentOptionClickEvent>,
        reactionCountClickPublisher: PublishSubject<ReactionCountClickEvent>
    ) {
        this.userClickPublisher = userClickPublisher
        this.commentContentClickPublisher = commentContentClickPublisher
        this.commentEngagementClickPublisher = commentEngagementClickPublisher
        this.commentOptionClickPublisher = commentOptionClickPublisher
        this.reactionCountClickPublisher = reactionCountClickPublisher
    }

    private fun handleBottomSpace() {
        binding.addBottomSpace =
            binding.readOnly != null && binding.readOnly!!
    }

    private fun setReadOnlyMode(isReadOnly: Boolean) {
        binding.readOnly = isReadOnly
        handleBottomSpace()
    }

    private fun setText(comment: AmityComment) {
        binding.tvPostComment.text = getHighlightTextUserMentions(comment)
        setNumberOfReactions(comment.getReactionCount())
    }

    private fun setNumberOfReactions(reactionCount: Int) {
        binding.reactionCountLayout.visibility = if (reactionCount > 0) View.VISIBLE else View.GONE
        binding.tvNumberOfReactions.text = reactionCount.readableNumber()
    }

    private fun getHighlightTextUserMentions(comment: AmityComment): SpannableString {
        val commentText = (comment.getData() as? AmityComment.Data.TEXT)?.getText() ?: ""
        val spannable = SpannableString(commentText)
        if (spannable.isNotEmpty() && comment.getMetadata() != null) {
            val mentionUserIds =
                comment.getMentionees().map { (it as? AmityMentionee.USER)?.getUserId() }
            val mentionedUsers =
                AmityMentionMetadataGetter(comment.getMetadata()!!).getMentionedUsers()
            val mentions = mentionedUsers.filter { mentionUserIds.contains(it.getUserId()) }
            mentions.forEach { mentionUserItem ->
                try {
                    spannable.setSpan(
                        AmityMentionClickableSpan(mentionUserItem.getUserId()),
                        mentionUserItem.getIndex(),
                        mentionUserItem.getIndex().plus(mentionUserItem.getLength()).inc(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } catch (exception: IndexOutOfBoundsException) {
                    Timber.e("AmityPostCommentView", "Highlight text user mentions crashes")
                }
            }
        }
        return spannable
    }
}