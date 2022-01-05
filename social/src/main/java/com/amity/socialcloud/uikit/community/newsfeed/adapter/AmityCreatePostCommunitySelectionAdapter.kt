package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.common.common.loadImage
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCommunitySelectionListBinding
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostCommunitySelectionListener

class AmityCreatePostCommunitySelectionAdapter(private val listener: AmityCreatePostCommunitySelectionListener) :
    AmityBaseRecyclerViewPagedAdapter<AmityCommunity>(diffCallBack) {

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityCommunity>() {

            override fun areItemsTheSame(oldItem: AmityCommunity, newItem: AmityCommunity): Boolean =
                oldItem.getCommunityId() == newItem.getCommunityId()

            override fun areContentsTheSame(oldItem: AmityCommunity, newItem: AmityCommunity): Boolean =
                oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
                        && oldItem.getChannelId() == newItem.getChannelId()
                        && oldItem.getCommunityId() == newItem.getCommunityId()
                        && oldItem.getCreatedAt() == newItem.getCreatedAt()
                        && oldItem.getDescription() == newItem.getDescription()
                        && oldItem.getDisplayName() == newItem.getDisplayName()
                        && oldItem.getMemberCount() == newItem.getMemberCount()
                        && oldItem.getPostCount() == newItem.getPostCount()
                        && oldItem.getUpdatedAt() == newItem.getUpdatedAt()
                        && oldItem.getUserId() == newItem.getUserId()
                        && oldItem.isJoined() == newItem.isJoined()
                        && oldItem.isOfficial() == newItem.isOfficial()
                        && oldItem.isPublic() == newItem.isPublic()
        }
    }

    override fun getLayoutId(position: Int, obj: AmityCommunity?): Int =
        R.layout.amity_item_community_selection_list

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return AmityCommunityViewHolder(view, listener)
    }

    class AmityCommunityViewHolder(
        itemView: View,
        private val listener: AmityCreatePostCommunitySelectionListener?
    ) :
        RecyclerView.ViewHolder(itemView), Binder<AmityCommunity> {

        private val binding: AmityItemCommunitySelectionListBinding? =
            DataBindingUtil.bind(itemView)

        override fun bind(data: AmityCommunity?, position: Int) {
            if (data != null) {
                binding?.community = data
                setupCommunityNameView(data)
                setupCommunityImageView(data)
                itemView.setOnClickListener { listener?.onClickCommunity(data, position) }
            }
        }

        private fun setupCommunityNameView(data: AmityCommunity) {
            var leftDrawable: Drawable? = null
            var rightDrawable: Drawable? = null
            if (!data.isPublic()) {
                leftDrawable =
                    ContextCompat.getDrawable(itemView.context, R.drawable.amity_ic_lock2)
            }
            if (data.isOfficial()) {
                rightDrawable =
                    ContextCompat.getDrawable(itemView.context, R.drawable.amity_ic_verified)
            }
            binding?.tvCommunityName?.setCompoundDrawablesWithIntrinsicBounds(
                leftDrawable,
                null,
                rightDrawable,
                null
            )
        }

        private fun setupCommunityImageView(data: AmityCommunity) {
            binding?.avCommunityProfile?.loadImage(
                data.getAvatar()?.getUrl(AmityImage.Size.SMALL),
                R.drawable.amity_ic_default_community_avatar_circular
            )
        }

    }

}