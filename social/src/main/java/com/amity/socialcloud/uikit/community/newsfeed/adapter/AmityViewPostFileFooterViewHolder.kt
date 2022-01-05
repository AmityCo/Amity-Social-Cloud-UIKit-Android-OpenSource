package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment

class AmityViewPostFileFooterViewHolder(
    itemView: View,
    private val loadMoreClickListener: AmityPostViewFileAdapter.ILoadMoreFilesClickListener?,
    private val newsFeed: AmityPost?
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewAdapter.IBinder<AmityFileAttachment> {
    private val tvLoadMoreFiles = itemView.findViewById<TextView>(R.id.tvLoadMoreFiles)

    override fun bind(data: AmityFileAttachment?, position: Int) {
        tvLoadMoreFiles.setOnClickListener {
            if (loadMoreClickListener != null && newsFeed != null) {
                loadMoreClickListener.loadMoreFiles(newsFeed)
            }
        }
    }

}
