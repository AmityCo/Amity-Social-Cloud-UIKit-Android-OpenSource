package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostFileItemClickListener

const val MAX_ITEM_TO_DISPLAY = 5

class AmityPostViewFileAdapter() : AmityBasePostAttachmentAdapter() {

    private var loadMoreFilesClickListener: ILoadMoreFilesClickListener? = null
    private var fileItemClickListener: AmityPostFileItemClickListener? = null
    private var newsFeed: AmityPost? = null
    private var collapsible: Boolean = false

    constructor(fileItemClickListener: AmityPostFileItemClickListener?) : this() {
        this.fileItemClickListener = fileItemClickListener
    }

    constructor(
        loadMoreFilesClickListener: ILoadMoreFilesClickListener?,
        fileItemClickListener: AmityPostFileItemClickListener?,
        newsFeed: AmityPost,
        collapsible: Boolean
    ) : this() {
        this.collapsible = collapsible
        this.loadMoreFilesClickListener = loadMoreFilesClickListener
        this.fileItemClickListener = fileItemClickListener
        this.newsFeed = newsFeed
    }

    override fun getLayoutId(position: Int, obj: AmityFileAttachment?): Int {
        return if (collapsible && position == MAX_ITEM_TO_DISPLAY) {
            R.layout.amity_item_footer_view_post_file
        } else {
            R.layout.amity_item_view_post_file
        }
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.amity_item_footer_view_post_file) {
            AmityViewPostFileFooterViewHolder(view, loadMoreFilesClickListener, newsFeed)
        } else {
            AmityBasePostAttachmentViewHolder(view, fileItemClickListener)
        }
    }

    /* Max allowed item to display is file*/
    override fun getItemCount(): Int {
        return if (collapsible && list.size > MAX_ITEM_TO_DISPLAY) {
            MAX_ITEM_TO_DISPLAY + 1
        } else {
            super.getItemCount()
        }
    }

    interface ILoadMoreFilesClickListener {
        fun loadMoreFiles(post: AmityPost)
    }
}