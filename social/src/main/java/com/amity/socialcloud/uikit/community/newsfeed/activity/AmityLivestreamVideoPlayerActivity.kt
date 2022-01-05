package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityLivestreamVideoPlayerBinding
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityLiveStreamVideoPlayerViewModel
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity


open class AmityLivestreamVideoPlayerActivity : RxAppCompatActivity() {


    private lateinit var binding: AmityActivityLivestreamVideoPlayerBinding
    private val viewModel: AmityLiveStreamVideoPlayerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AmityActivityLivestreamVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel.streamId = intent.getStringExtra(EXTRA_STREAM_ID) ?: ""
        getVideoURL()
        observeInvalidStream()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoPlayer.stop()
    }

    override fun onResume() {
        super.onResume()
        binding.videoPlayer.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.videoPlayer.pause()
    }

    private fun getVideoURL() {
        viewModel.getVideoURL(
            onLoadURLSuccess = { startStreaming(it) },
            onLoadURLError = { presentStreamLoadingError() })
            .doOnError { presentStreamLoadingError() }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun startStreaming(liveStreamURL: String) {
        binding.videoPlayer.enableStopWhenPause()
        binding.videoPlayer.play(viewModel.streamId, liveStreamURL)
    }

    private fun observeInvalidStream() {
        viewModel.observeInvalidStream(
            onStreamDeleted = { presentDeletedStream() },
            onStreamEnded = { presentEndedStream() })
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun presentDeletedStream() {
        binding.unavailableVideoContainer.visibility = View.VISIBLE
        binding.livestreamContainer.visibility = View.GONE
        binding.endedVideoContainer.visibility = View.GONE
        binding.videoPlayer.stop()
    }

    private fun presentEndedStream() {
        binding.unavailableVideoContainer.visibility = View.GONE
        binding.livestreamContainer.visibility = View.GONE
        binding.endedVideoContainer.visibility = View.VISIBLE
        binding.videoPlayer.stop()
    }

    private fun presentStreamLoadingError() {
        Toast.makeText(
            this,
            getString(R.string.amity_video_stream_unavailable_description),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun newIntent(context: Context, streamId: String): Intent {
            return Intent(
                context,
                AmityLivestreamVideoPlayerActivity::class.java
            ).apply {
                putExtra(EXTRA_STREAM_ID, streamId)
            }
        }
    }
}

private const val EXTRA_STREAM_ID = "EXTRA_STREAM_ID"
