package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.common.common.readableFeedPostTime
import com.amity.socialcloud.uikit.community.R


class AmityPostReplyDeletedViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewPagedAdapter.Binder<AmityComment> {

    private val deletionTime: TextView = itemView.findViewById(R.id.tvDeletionTime)

    override fun bind(data: AmityComment?, position: Int) {
        data?.let {
            deletionTime.text = data.getEditedAt()?.millis?.readableFeedPostTime(itemView.context)
        }
    }
}