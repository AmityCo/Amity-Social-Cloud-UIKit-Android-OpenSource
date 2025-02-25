package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.common.common.loadImage
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCommunitySelectionListBinding
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostCommunitySelectionListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityCreatePostCommunitySelectionAdapter(
    private val disposables: CompositeDisposable,
    private val listener: AmityCreatePostCommunitySelectionListener
) :
    AmityBaseRecyclerViewPagingDataAdapter<AmityCommunity>(diffCallBack) {

    private val allowToCreateMap = mutableMapOf<String, Boolean>()

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityCommunity>() {

            override fun areItemsTheSame(
                oldItem: AmityCommunity,
                newItem: AmityCommunity
            ): Boolean = oldItem.getCommunityId() == newItem.getCommunityId()

            override fun areContentsTheSame(
                oldItem: AmityCommunity,
                newItem: AmityCommunity
            ): Boolean = oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
                    && oldItem.getChannelId() == newItem.getChannelId()
                    && oldItem.getCommunityId() == newItem.getCommunityId()
                    && oldItem.getCreatedAt() == newItem.getCreatedAt()
                    && oldItem.getDescription() == newItem.getDescription()
                    && oldItem.getDisplayName() == newItem.getDisplayName()
                    && oldItem.getMemberCount() == newItem.getMemberCount()
                    && oldItem.getPostCount() == newItem.getPostCount()
                    && oldItem.getUpdatedAt() == newItem.getUpdatedAt()
                    && oldItem.getCreatorId() == newItem.getCreatorId()
                    && oldItem.isJoined() == newItem.isJoined()
                    && oldItem.isOfficial() == newItem.isOfficial()
                    && oldItem.isPublic() == newItem.isPublic()
        }
    }

    override fun getLayoutId(position: Int, obj: AmityCommunity?): Int =
        R.layout.amity_item_community_selection_list

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return AmityCommunityViewHolder(view, allowToCreateMap, disposables, listener)
    }

    class AmityCommunityViewHolder(
        itemView: View,
        private val allowToCreateMap: MutableMap<String, Boolean>,
        private val disposables: CompositeDisposable,
        private val listener: AmityCreatePostCommunitySelectionListener?
    ) :
        RecyclerView.ViewHolder(itemView), Binder<AmityCommunity> {

        private val binding: AmityItemCommunitySelectionListBinding? =
            DataBindingUtil.bind(itemView)

        override fun bind(data: AmityCommunity?, position: Int) {
            if (data != null) {
                if (!allowToCreateMap.containsKey(data.getCommunityId())) {
                    if (data.getPostSettings() != AmityCommunityPostSettings.ADMIN_CAN_POST_ONLY) {
                        allowToCreateMap[data.getCommunityId()] = true
                    } else {
                        handlePermissionCheck(data)
                    }
                }
                binding?.community = data
                if (allowToCreateMap[data.getCommunityId()] == false) {
                    setupEmptyView()
                } else {
                    setupContentView(data)
                }
            }
        }

        private fun handlePermissionCheck(community: AmityCommunity) {
            AmityCoreClient
                .hasPermission(AmityPermission.CREATE_PRIVILEGED_POST)
                .atCommunity(community.getCommunityId())
                .check()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isAllow ->
                    if (!allowToCreateMap.containsKey(community.getCommunityId())
                        || allowToCreateMap[community.getCommunityId()] != isAllow
                    ) {
                        allowToCreateMap[community.getCommunityId()] = isAllow
                        if (!isAllow) {
                            setupEmptyView()
                        } else {
                            setupContentView(community)
                        }
                    }
                }
                .doOnError { }
                .subscribe()
                .let(disposables::add)
        }

        private fun setupContentView(data: AmityCommunity) {
            itemView.visibility = View.VISIBLE
            itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            itemView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            setupCommunityNameView(data)
            setupCommunityImageView(data)
            itemView.setOnClickListener { listener?.onClickCommunity(data, position) }
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

        private fun setupEmptyView() {
            itemView.visibility = View.GONE
            itemView.layoutParams.height = 0
            itemView.layoutParams.width = 0
        }

    }

}