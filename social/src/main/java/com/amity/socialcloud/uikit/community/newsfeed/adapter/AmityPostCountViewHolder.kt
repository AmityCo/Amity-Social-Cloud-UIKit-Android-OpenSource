package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter

class AmityPostCountViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
	AmityBaseRecyclerViewAdapter.IBinder<AmityPost> {
	
	override fun bind(data: AmityPost?, position: Int) {
	
	}
	
}
