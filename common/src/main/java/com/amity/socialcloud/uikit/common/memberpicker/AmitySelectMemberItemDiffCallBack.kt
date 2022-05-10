package com.amity.socialcloud.uikit.common.memberpicker

import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem

class AmitySelectMemberItemDiffCallBack(
    private val oldList: List<AmitySelectMemberItem>,
    private val newList: List<AmitySelectMemberItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].name == newList[newItemPosition].name &&
                oldList[oldItemPosition].subTitle == newList[newItemPosition].subTitle

}