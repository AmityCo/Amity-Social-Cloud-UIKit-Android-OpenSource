package com.amity.socialcloud.uikit.community.mycommunity.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter


abstract class AmityBaseMyCommunityPreviewItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewPagedAdapter.Binder<AmityCommunity>
