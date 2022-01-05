package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.utils.safeLet
import com.amity.socialcloud.uikit.community.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.amity_fragment_social_video_player.*

internal class AmitySimpleVideoPlayerFragment : Fragment() {

    private var exoplayer: SimpleExoPlayer? = null
    private var videoUrl: String? = null
    private var currentPosition = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.amity_fragment_social_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { nonNullContext ->
            exoplayer = SimpleExoPlayer.Builder(nonNullContext).build()
            exoplayer?.playWhenReady = true
            video_viewer?.player = exoplayer
            if (savedInstanceState != null) {
                val videoUrl = savedInstanceState.getString(ARG_VIDEO_URL)
                currentPosition = savedInstanceState.getInt(ARG_VIDEO_CURRENT_POSITION)
                prepareVideo(videoUrl)
            } else {
                val videoUrl = arguments?.getString(ARG_VIDEO_URL)
                prepareVideo(videoUrl)
            }
        }
    }

    private fun prepareVideo(url: String?) {
        safeLet(url, context) { nonNullUrl, nonNullContext ->
            video_viewer?.requestFocus()
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                nonNullContext,
                Util.getUserAgent(nonNullContext, resources.getString(R.string.app_name))
            )
            val extractorsFactory = DefaultExtractorsFactory()
            val videoSource: MediaSource =
                ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(nonNullUrl)))
            exoplayer?.setMediaSource(videoSource)
            exoplayer?.prepare()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_VIDEO_URL, videoUrl)
        outState.putInt(ARG_VIDEO_CURRENT_POSITION, exoplayer?.currentPosition?.toInt() ?: 0)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer?.stop()
        exoplayer?.release()
    }

    internal class Builder internal constructor() {
        private var videoUrl: String? = null
        fun build(activity: AppCompatActivity): AmitySimpleVideoPlayerFragment {
            val fragment = AmitySimpleVideoPlayerFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_VIDEO_URL, this@Builder.videoUrl)
            }
            return fragment
        }

        internal fun videoUrl(videoUrl: String): Builder {
            this.videoUrl = videoUrl
            return this
        }
    }

    companion object {
        fun newInstance(videoUrl: String): Builder {
            return Builder().videoUrl(videoUrl)
        }
    }

}

private const val ARG_VIDEO_URL = "AMITY_KEY_VIDEO_URL"
private const val ARG_VIDEO_CURRENT_POSITION = "AMITY_KEY_VIDEO_POSITION"

