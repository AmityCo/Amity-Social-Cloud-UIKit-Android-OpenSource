package com.amity.socialcloud.uikit.chat.recent.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityItemRecentMessageBinding
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.utils.AmityDateUtils
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class AmityRecentChatViewHolder(
    itemView: View,
    private val listener: AmityRecentChatItemClickListener?
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewPagingDataAdapter.Binder<AmityChannel> {

    private val binding: AmityItemRecentMessageBinding? = DataBindingUtil.bind(itemView)

    private val memberCount: TextView = itemView.findViewById(R.id.tvMemberCount)
    private val name: TextView = itemView.findViewById(R.id.tvDisplayName)
    private val avatar: ShapeableImageView = itemView.findViewById(R.id.ivAvatar)
    private val unreadCount: TextView = itemView.findViewById(R.id.tvUnreadCount)

    override fun bind(data: AmityChannel?, position: Int) {
        if (data != null) {
            if (data.getDisplayName().isNotEmpty()) {
                name.text = data.getDisplayName()
            } else {
                name.text = itemView.context.getString(R.string.amity_anonymous)
            }
            setUpAvatarView(data)
            setupUnreadCount(data)
            binding?.tvTime?.text = AmityDateUtils.getMessageTime(data.getLastActivity().millis)
            memberCount.text =
                String.format(
                    itemView.context.getString(R.string.amity_member_count),
                    data.getMemberCount()
                )
            itemView.setOnClickListener {
                listener?.onRecentChatItemClick(data.getChannelId())
            }
/*            avatar.setOnClickListener {
                itemView.showSnackBar("Avatar clicked", Snackbar.LENGTH_SHORT)
            }*/
        }

    }

    private fun setUpAvatarView(data: AmityChannel) {
        val defaultAvatar: Int = when (data.getChannelType()) {
            AmityChannel.Type.STANDARD -> {
                //setupNameView(data)
                R.drawable.amity_ic_default_avatar_group_chat
            }
            AmityChannel.Type.PRIVATE -> {
                //setupNameView(data)
                R.drawable.amity_ic_default_avatar_private_community_chat
            }
            AmityChannel.Type.CONVERSATION -> {
                R.drawable.amity_ic_default_avatar_direct_chat
            }
            else -> {
                R.drawable.amity_ic_default_avatar_publc_community_chat
            }
        }

        avatar.setBackgroundColor(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(itemView.context, R.color.amityColorPrimary),
                AmityColorShade.SHADE3
            )
        )

        Glide.with(itemView.context)
            .load(data.getAvatar()?.getUrl(AmityImage.Size.MEDIUM))
            .placeholder(defaultAvatar)
            .centerCrop()
            .into(avatar)
    }

    private fun setupNameView(data: AmityChannel) {
        var leftDrawable = R.drawable.amity_ic_community_public
        if (data.getChannelType() == AmityChannel.Type.PRIVATE)
            leftDrawable = R.drawable.amity_ic_community_private
        val rightDrawable = 0
//        if (data.verified)
//            rightDrawable = R.drawable.amity_ic_verified
        name.text = data.getDisplayName()
        name.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawable, 0);
    }

    private fun setupUnreadCount(data: AmityChannel) {
        if (data.getUnreadCount() > 0) {
            unreadCount.visibility = View.VISIBLE
            unreadCount.text = data.getUnreadCount().toString()
        } else {
            unreadCount.visibility = View.GONE
        }
    }
}