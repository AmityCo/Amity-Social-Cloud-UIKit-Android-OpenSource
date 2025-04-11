package com.amity.socialcloud.uikit.chat.messages.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.messages.viewHolder.*
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityMessageListViewModel
import com.amity.socialcloud.uikit.common.components.AmityMessageListListener
import com.amity.socialcloud.uikit.common.utils.AmityDateUtils

class AmityMessagePagingAdapter(
    private val vmChat: AmityMessageListViewModel,
    private val viewHolderListener: CustomViewHolderListener?,
    private val messageListListener: AmityMessageListListener?,
    context: Context
) : PagingDataAdapter<AmityMessage, AmityChatMessageBaseViewHolder>(diffCallBack),
    AmityAudioPlayListener, AmityMessageItemListener {

    private val messageUtil = AmityMessageItemUtil()
    private val audioMessageHelper = AmityAudioMessageHelper(context)

    var firstCompletelyVisibleItem = 0
    var latestReadSegment = 0

    fun pauseAndResetPlayer() {
        audioMessageHelper.pauseAndResetPlayer()
    }

    fun releaseMediaPlayer() {
        audioMessageHelper.releaseMediaPlayer()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmityChatMessageBaseViewHolder {
        return messageUtil.getViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            viewType,
            viewHolderListener,
            this,
            this
        )
    }

    override fun getItemViewType(position: Int): Int {
        return messageUtil.getMessageType(getItem(position))
    }

    override fun onBindViewHolder(holder: AmityChatMessageBaseViewHolder, position: Int) {
        val ekoMessage = getItem(position)
        holder.setItem(ekoMessage)

        handleSenderVisibility(holder, position)
        handleDateVisibility(holder, position)

        if (position==0 && ekoMessage != null && latestReadSegment < ekoMessage.getSegment()) {
            ekoMessage.markRead()
            latestReadSegment = ekoMessage.getSegment()
        }

        if (ekoMessage?.getMessageId() == audioMessageHelper.playingMsgId) {
            if (holder is AmityAudioMsgSenderViewHolder) {
                audioMessageHelper.setPlayingAmityAudioHolder(holder)
            } else if (holder is AmityAudioMsgReceiverViewHolder) {
                audioMessageHelper.setPlayingAmityAudioHolder(holder)
            }
        }
    }

    override fun onViewRecycled(holder: AmityChatMessageBaseViewHolder) {
        super.onViewRecycled(holder)
        if (holder.itemBaseViewModel.amityMessage?.getMessageId() == audioMessageHelper.playingMsgId) {
            audioMessageHelper.setPlayingAmityAudioHolder(null)
        }
    }

    override fun playAudio(vh: AmityAudioMsgBaseViewHolder) {
        audioMessageHelper.playAudio(vh)
    }

    override fun messageDeleted(msgId: String) {
        if (msgId == audioMessageHelper.playingMsgId) {
            audioMessageHelper.audioMessageDeleted()
        }
    }

    override fun onMessageItemClicked(position: Int) {
        messageListListener?.onMessageClicked(position)
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityMessage>() {

            override fun areItemsTheSame(oldItem: AmityMessage, newItem: AmityMessage): Boolean =
                oldItem.getMessageId() == newItem.getMessageId()

            override fun areContentsTheSame(oldItem: AmityMessage, newItem: AmityMessage): Boolean {
                return oldItem.isDeleted() == newItem.isDeleted()
                        && oldItem.getCreatedAt() == newItem.getCreatedAt()
                        && oldItem.getEditedAt() == newItem.getEditedAt()
                        && oldItem.getState() == newItem.getState()
                        && oldItem.getFlagCount() == newItem.getFlagCount()
            }
        }
    }

    private fun handleSenderVisibility(holder: AmityChatMessageBaseViewHolder, position: Int) {
        val currItem = getItem(position)

        val prevItem = try {
            getItem(position - 1)
        } catch (e: IndexOutOfBoundsException) {
            null
        }

        val currentName = currItem?.getCreator()?.getDisplayName() ?: ""
        val prevName = prevItem?.getCreator()?.getDisplayName() ?: ""

        when {
            prevName.isBlank() -> {
                holder.itemBaseViewModel.isSenderVisible.set(true)
            }
            else -> holder.itemBaseViewModel.isSenderVisible.set(currentName != prevName)
        }
    }

    private fun handleDateVisibility(holder: AmityChatMessageBaseViewHolder, position: Int) {
        val currItem = peek(position)
        val currDate = AmityDateUtils.getRelativeDate(currItem?.getCreatedAt()?.millis ?: 0)

        val nextItem = try {
            peek(position + 1)
        } catch (e: IndexOutOfBoundsException) {
            null
        }
        val nextDate = if (nextItem == null) {
            ""
        } else {
            AmityDateUtils.getRelativeDate(nextItem.getCreatedAt().millis)
        }

        when {
            nextItem == null -> holder.itemBaseViewModel.isDateVisible.set(true)
            currDate != nextDate -> holder.itemBaseViewModel.isDateVisible.set(true)
            else -> holder.itemBaseViewModel.isDateVisible.set(false)
        }

        if (firstCompletelyVisibleItem in 0 until itemCount) {
            val firstItem = getItem(firstCompletelyVisibleItem)
            val date = AmityDateUtils.getRelativeDate(firstItem?.getCreatedAt()?.millis ?: 0)

            vmChat.stickyDate.set(date)
        }
    }

    interface CustomViewHolderListener {

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ): AmityChatMessageBaseViewHolder?
    }
}