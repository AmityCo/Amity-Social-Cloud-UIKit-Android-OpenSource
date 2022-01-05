package com.amity.socialcloud.uikit.community.newsfeed.adapter

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment

abstract class AmityBasePostAttachmentAdapter() : AmityBaseRecyclerViewAdapter<AmityFileAttachment>() {


    fun submitList(newList: List<AmityFileAttachment>) {
        setItems(newList, DiffCallback(list, newList))
    }

    class DiffCallback(
        private val oldList: List<AmityFileAttachment>,
        private val newList: List<AmityFileAttachment>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].uri == newList[newItemPosition].uri
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id &&
                    oldList[oldItemPosition].name == newList[newItemPosition].name &&
                    oldList[oldItemPosition].uploadState == newList[newItemPosition].uploadState &&
                    oldList[oldItemPosition].progress == newList[newItemPosition].progress &&
                    oldList[oldItemPosition].size == newList[newItemPosition].size &&
                    oldList[oldItemPosition].uri == newList[newItemPosition].uri &&
                    oldList[oldItemPosition].readableSize == newList[newItemPosition].readableSize &&
                    oldList[oldItemPosition].mimeType == newList[newItemPosition].mimeType
        }
    }


}