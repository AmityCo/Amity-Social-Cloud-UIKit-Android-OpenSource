package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.core.permission.AmityRoles
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.common.readableFeedPostTime
import com.amity.socialcloud.uikit.common.components.setImageUrl
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemBasePostHeaderBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostOptionClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostHeaderItem
import com.amity.socialcloud.uikit.user.UserType.*
import com.amity.socialcloud.uikit.user.labelSuffix
import com.amity.socialcloud.uikit.user.labelTextColor
import com.amity.socialcloud.uikit.user.userMeta
import com.amity.socialcloud.uikit.user.userType
import io.reactivex.subjects.PublishSubject

class AmityPostHeaderViewHolder(
    itemView: View,
    private val userClickPublisher: PublishSubject<AmityUser>,
    private val communityClickPublisher: PublishSubject<AmityCommunity>,
    private val postOptionClickPublisher: PublishSubject<PostOptionClickEvent>
) : RecyclerView.ViewHolder(itemView) {

    private val context = itemView.context
    private val binding = AmityItemBasePostHeaderBinding.bind(itemView)

    fun bind(data: AmityBasePostHeaderItem) {
        setupView(data)
        setupListener(data.post)
    }

    private fun setupView(data: AmityBasePostHeaderItem) {
        val post = data.post
        renderAvatar(post)
        renderCreatorName(post)
        renderEditBadge(post)
        renderTimeStamp(post)
        renderTarget(post, data.showTarget)
        renderPostOption(data.showOptions)
    }

    private fun renderAvatar(post: AmityPost) {
        val avatarURL = post.getPostedUser()?.getAvatar()?.getUrl(AmityImage.Size.SMALL)
        setImageUrl(
            binding.avatarView,
            avatarURL,
            ContextCompat.getDrawable(
                binding.avatarView.context,
                R.drawable.amity_ic_default_profile_large
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun renderCreatorName(post: AmityPost) {
        val postedUser = post.getPostedUser()
        val banIcon = if (postedUser?.isGlobalBan() == true) {
            ContextCompat.getDrawable(itemView.context, R.drawable.amity_ic_ban)
        } else {
            null
        }
        val type = post.getPostedUser()?.userMeta()?.userType() ?: Client

        val suffix = type.labelSuffix(itemView.context) ?: ""
        binding.userName.text = postedUser?.getDisplayName() + suffix
        binding.userName.setTextColor(type.labelTextColor(itemView.context))
        binding.userName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, banIcon, null)
    }

    private fun renderEditBadge(post: AmityPost) {
        if (post.isEdited()) {
            binding.tvEdited.visibility = View.VISIBLE
        } else {
            binding.tvEdited.visibility = View.GONE
        }
    }

    private fun renderTimeStamp(post: AmityPost) {
        binding.feedPostTime.text = post.getCreatedAt()?.millis?.readableFeedPostTime(context) ?: ""
    }

    private fun renderPostOption(showOptions: Boolean) {
        if (showOptions) {
            binding.btnFeedAction.visibility = View.VISIBLE
        } else {
            binding.btnFeedAction.visibility = View.GONE
        }
    }

    private fun renderTarget(post: AmityPost, showTarget: Boolean) {
        val target = post.getTarget()
        val isTargetingOwnFeed = if (target is AmityPost.Target.USER) target.getUser()
            ?.getUserId() ?: "" == post.getPostedUserId() else false
        val shouldShowTarget = showTarget && !isTargetingOwnFeed
        var arrowIcon: Drawable? = null
        if (shouldShowTarget) {
            arrowIcon = ContextCompat.getDrawable(context, R.drawable.amity_ic_arrow)
            val text = when (target) {
                is AmityPost.Target.COMMUNITY -> {
                    target.getCommunity()?.getDisplayName()?.trim() ?: ""
                }
                is AmityPost.Target.USER -> {
                    target.getUser()?.getDisplayName()?.trim() ?: ""
                }
                else -> {
                    ""
                }
            }
            binding.communityName.text = text
            binding.communityName.visibility = View.VISIBLE
            binding.postedInLabel.visibility = View.VISIBLE

        } else {
            arrowIcon = null
            binding.communityName.text = ""
            binding.communityName.visibility = View.GONE
            binding.postedInLabel.visibility = View.GONE
        }
        val isOfficial = (post.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()?.isOfficial() ?: false
        val officialBadgeIcon = if (isOfficial) {
            ContextCompat.getDrawable(context, R.drawable.amity_ic_verified)
        } else {
            null
        }
        // Noom customisation: arrow is disabled
        arrowIcon = null
        binding.communityName.setCompoundDrawablesWithIntrinsicBounds(arrowIcon, null, officialBadgeIcon, null)
    }

    private fun isCommunityModerator(roles: AmityRoles?): Boolean {
        return roles?.any {
            it == AmityConstants.MODERATOR_ROLE || it == AmityConstants.COMMUNITY_MODERATOR_ROLE
        } ?: false
    }

    private fun setupListener(post: AmityPost) {
        binding.avatarView.setOnClickListener {
            post.getPostedUser()?.let {
                userClickPublisher.onNext(it)
            }
        }

        binding.userName.setOnClickListener {
            post.getPostedUser()?.let {
                userClickPublisher.onNext(it)
            }
        }

        binding.communityName.setOnClickListener {
            val target = post.getTarget()
            when (target) {
                is AmityPost.Target.COMMUNITY -> {
                    target.getCommunity()?.let {
                        communityClickPublisher.onNext(it)
                    }
                }
                is AmityPost.Target.USER -> {
                    target.getUser()?.let {
                        userClickPublisher.onNext(it)
                    }
                }
                else -> {}
            }
        }

        binding.btnFeedAction.setOnClickListener {
            postOptionClickPublisher.onNext(PostOptionClickEvent(post))
        }

    }

}
