package com.amity.socialcloud.uikit.chat.messages.adapter

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.messages.viewHolder.*
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityMessageListViewModel
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.components.AmityMessageListListener
import com.amity.socialcloud.uikit.common.utils.AmityDateUtils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient

open class AmityMessageListAdapter(
    private val vmChat: AmityMessageListViewModel,
    private val viewHolderListener: CustomViewHolderListener?,
    private val messageListListener: AmityMessageListListener?,
    private val context: Context
) : ListAdapter<AmityMessage, AmityChatMessageBaseViewHolder>(
    MESSAGE_DIFF_CALLBACK
), AmityAudioPlayListener, AmityMessageItemListener {

    private val TAG = "AmityMessageListAdapter"
    private val messageUtil = AmityMessageItemUtil()
    var firstCompletelyVisibleItem = 0
    var playingMsgId = "-1"

    private val uAmpAudioAttributes: AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

    private val exoPlayer by lazy {
        SimpleExoPlayer.Builder(context).build().apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
        }
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
    private val okHttpDataSourceFactory: OkHttpDataSourceFactory by lazy {
        OkHttpDataSourceFactory(client, "USER_AGENT")
    }
    private var playingAmityAudioHolder: AmityAudioMsgBaseViewHolder? = null
    private val uiUpdateHandler by lazy { Handler() }

    companion object {
        private val MESSAGE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<AmityMessage>() {

            override fun areItemsTheSame(oldItem: AmityMessage, newItem: AmityMessage): Boolean =
                oldItem.getMessageId() == newItem.getMessageId()

            override fun areContentsTheSame(oldItem: AmityMessage, newItem: AmityMessage): Boolean {
                return oldItem.isDeleted() == newItem.isDeleted()
                        && oldItem.getEditedAt() == newItem.getEditedAt()
                        && oldItem.getState() == newItem.getState()
                        && oldItem.getFlagCount() == newItem.getFlagCount()
            }
        }
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

    override fun onBindViewHolder(holder: AmityChatMessageBaseViewHolder, position: Int) {
        val ekoMessage = getItem(position)
        holder.setItem(ekoMessage)
        handleDateAndSenderVisibility(holder)
        if (ekoMessage?.getMessageId() == playingMsgId) {
            if (holder is AmityAudioMsgSenderViewHolder) {
                playingAmityAudioHolder = holder
                updatePlayingState()
            } else if (holder is AmityAudioMsgReceiverViewHolder) {
                playingAmityAudioHolder = holder
                updatePlayingState()
            }
        }
    }

    override fun onViewRecycled(holder: AmityChatMessageBaseViewHolder) {
        super.onViewRecycled(holder)
        if (holder.itemBaseViewModel.amityMessage?.getMessageId() == playingMsgId) {
            updateNotPlayingState()
            playingAmityAudioHolder = null
        }
    }

    override fun getItemViewType(position: Int): Int {
        return messageUtil.getMessageType(getItem(position))
    }

    private fun handleDateAndSenderVisibility(holder: AmityChatMessageBaseViewHolder) {
        val listSize = currentList?.size ?: 0
        if (listSize > 0 && holder.adapterPosition == 0) {
            holder.itemBaseViewModel.isDateVisible.set(true)
            holder.itemBaseViewModel.isSenderVisible.set(true)
        } else if (listSize > 0 && holder.adapterPosition < listSize) {
            val currItem = getItem(holder.adapterPosition)
            val currDate = AmityDateUtils.getRelativeDate(currItem?.getCreatedAt()?.millis ?: 0)

            val prevItem = getItem(holder.adapterPosition - 1)
            val prevDate = AmityDateUtils.getRelativeDate(prevItem?.getCreatedAt()?.millis ?: 0)

            if (currDate.isNotBlank() && prevDate.isNotBlank()) {
                holder.itemBaseViewModel.isDateVisible.set(currDate != prevDate)
            } else {
                holder.itemBaseViewModel.isDateVisible.set(false)
            }

            val currentName = currItem?.getUser()?.getDisplayName() ?: ""
            val nextName = prevItem?.getUser()?.getDisplayName() ?: ""
            if (currentName.isBlank() || nextName.isBlank()) {
                holder.itemBaseViewModel.isSenderVisible.set(true)
            } else {
                holder.itemBaseViewModel.isSenderVisible.set(currentName != nextName)
            }
        }

        if (firstCompletelyVisibleItem in 0 until listSize) {
            val firstItem = getItem(firstCompletelyVisibleItem)
            val date = AmityDateUtils.getRelativeDate(firstItem?.getCreatedAt()?.millis ?: 0)
            vmChat.stickyDate.set(date)
        }
    }

    override fun onMessageItemClicked(position: Int) {
        messageListListener?.onMessageClicked(position)
    }

    override fun playAudio(vh: AmityAudioMsgBaseViewHolder) {
        if (!vh.audioMsgBaseViewModel.isPlaying.get()) {
            resetMediaPlayer()
            playingAmityAudioHolder?.audioMsgBaseViewModel?.isPlaying?.set(false)
            playingAmityAudioHolder?.audioMsgBaseViewModel?.buffering?.set(false)
            playingAmityAudioHolder = vh
        }
        exoPlayer.addListener(exoPlayerListener)

        try {
            if (exoPlayer.isPlaying) {
                resetMediaPlayer()
                updateNotPlayingState()
            } else {
                playingMsgId =
                    playingAmityAudioHolder?.audioMsgBaseViewModel?.amityMessage?.getMessageId()
                        ?: "-1"
                playingAmityAudioHolder?.audioMsgBaseViewModel?.buffering?.set(true)
                val url: String = playingAmityAudioHolder?.audioMsgBaseViewModel?.audioUrl?.get() ?: ""
                val mediaItem = MediaItem.fromUri(url.toUri()).buildUpon().build()
                val mediaSource = ProgressiveMediaSource.Factory(okHttpDataSourceFactory)
                    .createMediaSource(mediaItem)
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
        } catch (ex: Exception) {
            Log.e(TAG, "playAudio: ${ex.localizedMessage}")
        }
    }

    override fun messageDeleted(msgId: String) {
        if (msgId == playingMsgId) {
            playingAmityAudioHolder?.audioMsgBaseViewModel?.isPlaying?.set(false)
            playingAmityAudioHolder = null
            resetMediaPlayer()
            uiUpdateHandler.removeCallbacks(updateSeekBar)
        }
    }

    private val exoPlayerListener = object : Player.EventListener {
        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                Player.STATE_READY -> {
                    updatePlayingState()
                }
                Player.STATE_ENDED -> {
                    exoPlayer.seekTo(0)
                    updateNotPlayingState()
                    playingAmityAudioHolder?.audioMsgBaseViewModel?.duration?.set("0:00")
                    exoPlayer.stop(true)
                }
                else -> {
                    super.onPlaybackStateChanged(state)
                }
            }

        }

        override fun onPlayerError(error: ExoPlaybackException) {
            super.onPlayerError(error)
            Log.e(TAG, "onPlayerError: ${error.printStackTrace()}")
            playingAmityAudioHolder?.audioMsgBaseViewModel?.buffering?.set(false)
            playingAmityAudioHolder?.itemView?.findViewById<View>(android.R.id.content)
                ?.showSnackBar(context.getString(R.string.amity_playback_error), Snackbar.LENGTH_SHORT)
        }
    }

    private val updateSeekBar = object : Runnable {
        override fun run() {
            val timeElapsed = exoPlayer.currentPosition
            playingAmityAudioHolder?.audioMsgBaseViewModel?.duration?.set(
                AmityDateUtils.getFormattedTimeForChat(timeElapsed.toInt())
            )
            uiUpdateHandler.postDelayed(this, 500L)
        }
    }

    private fun updatePlayingState() {
        playingAmityAudioHolder?.audioMsgBaseViewModel?.duration?.set(
            AmityDateUtils.getFormattedTimeForChat(exoPlayer.duration.toInt())
        )
        playingAmityAudioHolder?.audioMsgBaseViewModel?.buffering?.set(false)
        playingAmityAudioHolder?.audioMsgBaseViewModel?.isPlaying?.set(true)
        uiUpdateHandler.post(updateSeekBar)
    }

    private fun updateNotPlayingState() {
        playingAmityAudioHolder?.audioMsgBaseViewModel?.isPlaying?.set(false)
        playingAmityAudioHolder?.audioMsgBaseViewModel?.buffering?.set(false)
        uiUpdateHandler.removeCallbacks(updateSeekBar)
    }

    interface CustomViewHolderListener {

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ): AmityChatMessageBaseViewHolder?
    }

    private fun resetMediaPlayer() {
        playingMsgId = "-1"
        try {
            exoPlayer.pause()
        } catch (e: IllegalStateException) {
            Log.e(TAG, "resetMediaPlayer: ${e.localizedMessage}")
        }
        exoPlayer.stop(true)
    }

    internal fun pauseAndResetPlayer() {
        playingAmityAudioHolder?.audioMsgBaseViewModel?.isPlaying?.set(false)
        resetMediaPlayer()
        uiUpdateHandler.removeCallbacks(updateSeekBar)
    }

    internal fun releaseMediaPlayer() {
        exoPlayer.release()
    }
}
