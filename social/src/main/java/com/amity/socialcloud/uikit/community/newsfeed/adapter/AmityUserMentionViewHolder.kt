package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemUserMentionBinding
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention

class AmityUserMentionViewHolder(itemView: View, private val listener: AmityUserMentionListener) :
    RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewPagedAdapter.Binder<AmityUser> {

    private val itemBiding: AmityItemUserMentionBinding? = DataBindingUtil.bind(itemView)

    override fun bind(data: AmityUser?, position: Int) {

        data?.let { userItem ->
            val banIcon = if (userItem.isGlobalBan()) {
                ContextCompat.getDrawable(itemView.context, R.drawable.amity_ic_ban)
            } else {
                null
            }
            itemBiding?.apply {
                user = userItem
                userMention = AmityUserMention(userItem)
                isGlobalBan = userItem.isGlobalBan()
                textviewDisplayname.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    banIcon,
                    null
                )
                clickListener = listener
            }
        }
    }

    interface AmityUserMentionListener {
        fun onClickUserMention(userMention: AmityUserMention)
    }

}