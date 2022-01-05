package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostImageActionListener
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia

class AmityCreatePostMediaAdapter(private val listener: AmityCreatePostImageActionListener) :
    AmityBaseRecyclerViewAdapter<PostMedia>(), IListItemChangeListener {

    private var editingPostId: String? = null

    constructor(editingPostId: String, listener: AmityCreatePostImageActionListener) : this(listener) {
        this.editingPostId = editingPostId
    }

    override fun getLayoutId(position: Int, obj: PostMedia?): Int {
        return R.layout.amity_item_create_post_image
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return AmityCreatePostImageViewHolder(editingPostId, view, listener, this)
    }

    fun submitList(newList: List<PostMedia>) {
        setItems(newList, DiffCallback(list, newList))
    }

    class DiffCallback(
        private val oldList: List<PostMedia>,
        private val newList: List<PostMedia>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].url == newList[newItemPosition].url
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].url == newList[newItemPosition].url
                    && oldList[oldItemPosition].id == newList[newItemPosition].id
                    && oldList[oldItemPosition].uploadState == newList[newItemPosition].uploadState
                    && oldList[oldItemPosition].currentProgress == newList[newItemPosition].currentProgress
        }
    }

    override fun itemCount(): Int {
        return itemCount
    }


}

interface IListItemChangeListener {
    fun itemCount(): Int
}