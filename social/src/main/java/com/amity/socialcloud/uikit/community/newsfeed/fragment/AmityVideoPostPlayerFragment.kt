package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amity.socialcloud.sdk.core.AmityVideo
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.amity_fragment_social_video_player.*

internal class AmityVideoPostPlayerFragment : AmityBaseFragment() {

    private var exoplayer: SimpleExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.amity_fragment_social_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val videoData = arguments?.getParcelable<AmityPost.Data.VIDEO>(ARGS_VIDEO_DATA)
        safeLet(context, videoData) { nonNullContext, nonNullVideoData ->
            setupPlayer(nonNullContext)
            nonNullVideoData.getVideo(AmityVideo.Quality.ORIGINAL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { prepareVideo(it.getUrl()) }
                .subscribe()
        }
    }

    private fun setupPlayer(context: Context) {
        exoplayer = SimpleExoPlayer.Builder(context).build()
        exoplayer?.playWhenReady = false
        video_viewer?.player = exoplayer
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

    override fun onDestroy() {
        super.onDestroy()
        exoplayer?.stop()
        exoplayer?.release()
    }

    internal class Builder internal constructor() {
        private var videoData: AmityPost.Data.VIDEO? = null

        fun build(): AmityVideoPostPlayerFragment {
            val fragment = AmityVideoPostPlayerFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ARGS_VIDEO_DATA, videoData)
            }
            return fragment
        }

        internal fun videoData(videoData: AmityPost.Data.VIDEO): Builder {
            this.videoData = videoData
            return this
        }
    }

    companion object {
        fun newInstance(videoData: AmityPost.Data.VIDEO): Builder {
            return Builder().videoData(videoData)
        }
    }
}

private const val ARGS_VIDEO_DATA = "ARGS_VIDEO_DATA"
