package com.amity.socialcloud.uikit.sample.liveChat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.uikit.sample.R
import com.amity.socialcloud.uikit.sample.databinding.AmityActivityLiveChatBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityLiveChatActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    lateinit var binding: AmityActivityLiveChatBinding
    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelId = intent.getStringExtra("CHANNEL_ID") ?: ""
        val subChannelId = intent.getStringExtra("SUB_CHANNEL_ID") ?: ""
        binding = AmityActivityLiveChatBinding.inflate(this.layoutInflater, null, false)
        setContentView(binding.root)
        
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player
        binding.playerView.useController = false
        
        // Prepare the media source.
        val videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4"
        val mediaSource = MediaItem.fromUri(videoUrl)
        
        // Prepare the player with the source.
        player?.setMediaItem(mediaSource)
        
        // Set the repeat mode to loop the video indefinitely
        player?.repeatMode = REPEAT_MODE_ALL
        
        player?.prepare()
        player?.playWhenReady = true
        
        val messageListFragment = AmityLiveChatBottomSheetFragment.newInstance(
            channelId = channelId,
            subChannelId = subChannelId
        ).build()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, messageListFragment, messageListFragment.tag)
        transaction.addToBackStack(messageListFragment.tag)
        transaction.commit()
        
        AmityChatClient.newChannelRepository()
            .joinChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {  }
            .subscribe()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    
    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }
    
    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }
    
    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}