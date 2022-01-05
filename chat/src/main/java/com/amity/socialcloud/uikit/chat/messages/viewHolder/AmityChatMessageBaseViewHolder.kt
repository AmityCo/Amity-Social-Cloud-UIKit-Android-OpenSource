package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityChatMessageBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityDateUtils

abstract class AmityChatMessageBaseViewHolder(
    itemView: View,
    val itemBaseViewModel: AmityChatMessageBaseViewModel
) : RecyclerView.ViewHolder(itemView) {

    abstract fun setMessage(message: AmityMessage)

    fun setItem(item: AmityMessage?) {
        itemBaseViewModel.amityMessage = item
        itemBaseViewModel.msgTime.set(item?.getCreatedAt()?.toString("hh:mm a"))
        itemBaseViewModel.editedAt.set(item?.getEditedAt()?.toString("hh:mm a"))
        itemBaseViewModel.msgDate.set(
            AmityDateUtils.getRelativeDate(
                item?.getCreatedAt()?.millis ?: 0
            )
        )
        itemBaseViewModel.isDeleted.set(item?.isDeleted() ?: false)
        itemBaseViewModel.isFailed.set(item?.getState() == AmityMessage.State.FAILED)
        if (item != null) {
            itemBaseViewModel.sender.set(getSenderName(item))
            itemBaseViewModel.isSelf.set(item.getUserId() == AmityCoreClient.getUserId())

            val difference = item.getEditedAt().millis - item.getCreatedAt().millis
            itemBaseViewModel.isEdited.set(difference / 1000 > 1)
            setMessage(item)
        }
    }

    private fun getSenderName(item: AmityMessage): String {
        return if (item.getUserId() == AmityCoreClient.getUserId()) {
            "ME"
        } else {
            item.getUser()?.getDisplayName() ?: itemView.context.getString(R.string.amity_anonymous)
        }
    }
}