package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.events.*
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityBasePostFooterItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

open class AmityPostFooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
	
	open fun bind(data: AmityBasePostFooterItem, position: Int) {

	}
	
}
