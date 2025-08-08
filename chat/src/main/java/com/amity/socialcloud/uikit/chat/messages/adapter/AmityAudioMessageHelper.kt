package com.amity.socialcloud.uikit.chat.messages.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.messages.viewHolder.AmityAudioMsgBaseViewHolder
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.utils.AmityDateUtils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient

class AmityAudioMessageHelper(
    private val context: Context
) {

    private val TAG = "AmityAudioMessageHelper"

    var playingMsgId = "-1"
        private set

    private var playingAmityAudioHolder: AmityAudioMsgBaseViewHolder? = null
    private val uiUpdateHandler by lazy { Handler(Looper.getMainLooper()) }

    private val uAmpAudioAttributes: AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

    private val exoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
        }
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val okHttpDataSourceFactory: OkHttpDataSource.Factory by lazy {
        OkHttpDataSource.Factory(client).setUserAgent("USER_AGENT")
    }

    fun setPlayingAmityAudioHolder(holder: AmityAudioMsgBaseViewHolder?) {
        this.playingAmityAudioHolder = holder
        if (holder == null) {
            updateNotPlayingState()
        } else {
            updatePlayingState()
        }
    }

    fun playAudio(vh: AmityAudioMsgBaseViewHolder) {
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
                val url: String =
                    playingAmityAudioHolder?.audioMsgBaseViewModel?.audioUrl?.get() ?: ""
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

    fun audioMessageDeleted() {
        playingAmityAudioHolder?.audioMsgBaseViewModel?.isPlaying?.set(false)
        playingAmityAudioHolder = null
        resetMediaPlayer()
        uiUpdateHandler.removeCallbacks(updateSeekBar)
    }

    private fun resetMediaPlayer() {
        playingMsgId = "-1"
        try {
            exoPlayer.pause()
        } catch (e: IllegalStateException) {
            Log.e(TAG, "resetMediaPlayer: ${e.localizedMessage}")
        }
        exoPlayer.clearMediaItems()
        exoPlayer.stop()
    }

    fun pauseAndResetPlayer() {
        playingAmityAudioHolder?.audioMsgBaseViewModel?.isPlaying?.set(false)
        resetMediaPlayer()
        uiUpdateHandler.removeCallbacks(updateSeekBar)
    }

    fun releaseMediaPlayer() {
        exoPlayer.release()
    }

    private val exoPlayerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    updatePlayingState()
                }
                Player.STATE_ENDED -> {
                    exoPlayer.seekTo(0)
                    updateNotPlayingState()
                    playingAmityAudioHolder?.audioMsgBaseViewModel?.duration?.set("0:00")
                    exoPlayer.clearMediaItems()
                    exoPlayer.stop()
                }
                else -> super.onPlaybackStateChanged(playbackState)
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.e(TAG, "onPlayerError: ${error.printStackTrace()}")
            playingAmityAudioHolder?.audioMsgBaseViewModel?.buffering?.set(false)
            playingAmityAudioHolder?.itemView?.findViewById<View>(android.R.id.content)
                ?.showSnackBar(
                    context.getString(R.string.amity_playback_error),
                    Snackbar.LENGTH_SHORT
                )
        }
    }

    private val updateSeekBar = object : Runnable {
        override fun run() {
            val timeElapsed = exoPlayer.currentPosition
            playingAmityAudioHolder?.audioMsgBaseViewModel?.duration?.set(
                AmityDateUtils.getFormattedTimeForChat(timeElapsed.toInt())
            )
            uiUpdateHandler.postDelayed(this, 400L)
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
}