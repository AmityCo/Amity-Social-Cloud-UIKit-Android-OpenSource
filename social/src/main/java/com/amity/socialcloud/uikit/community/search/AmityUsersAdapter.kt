package com.amity.socialcloud.uikit.community.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemGlobalSearchUserBinding
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener

class AmityUsersAdapter(private val context: Context, private val listener: AmityUserClickListener):
        AmityBaseRecyclerViewPagingDataAdapter<AmityUser>(DIFF_CALLBACK) {
    
    override fun getLayoutId(position: Int, obj: AmityUser?): Int = R.layout.amity_item_global_search_user
    
    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder =
            AmityUserViewHolder(context, view, listener)
    
    class AmityUserViewHolder(private val context: Context, itemView: View, private val listener: AmityUserClickListener):
        RecyclerView.ViewHolder(itemView), Binder<AmityUser> {

        private val binding = AmityItemGlobalSearchUserBinding.bind(itemView)

        override fun bind(data: AmityUser?, position: Int) {
            binding.amityUser = data
            if (data != null) {
                binding.ivAvatar.setOnClickListener {
                    listener.onClickUser(data)
                }
                binding.tvDisplayName.setOnClickListener {
                    listener.onClickUser(data)
                }
                val banIcon = if (data.isGlobalBan()) {
                    ContextCompat.getDrawable(context, R.drawable.amity_ic_ban)
                } else {
                    null
                }
                binding.tvDisplayName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, banIcon, null)
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AmityUser>() {

            override fun areItemsTheSame(oldItem: AmityUser, newItem: AmityUser): Boolean =
                oldItem.getUserId() == newItem.getUserId()

            override fun areContentsTheSame(oldItem: AmityUser, newItem: AmityUser): Boolean =
                oldItem.getDisplayName() == newItem.getDisplayName() &&
                        oldItem.getAvatar()?.getUrl() == newItem.getAvatar()?.getUrl()
        }
    }
}