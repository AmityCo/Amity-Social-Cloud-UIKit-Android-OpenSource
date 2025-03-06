package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.utils.safeLet
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentSocialVideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

internal class AmityVideoPostPlayerFragment : AmityBaseFragment() {

    private var _binding: AmityFragmentSocialVideoPlayerBinding? = null
    private val binding get() = _binding!!
    private var exoplayer: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AmityFragmentSocialVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        val videoData = arguments?.getParcelable<AmityPost.Data.VIDEO>(ARGS_VIDEO_DATA)
        safeLet(context, videoData) { nonNullContext, nonNullVideoData ->
            setupPlayer(nonNullContext)
            nonNullVideoData.getVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { prepareVideo(it.getUrl()) }
                .subscribe()
        }
    }

    private fun setupPlayer(context: Context) {
        exoplayer = ExoPlayer.Builder(context).build()
        exoplayer?.playWhenReady = false
        binding.videoViewer.player = exoplayer
    }

    private fun prepareVideo(url: String?) {
        safeLet(url, context) { nonNullUrl, nonNullContext ->
            binding.videoViewer.requestFocus()
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

    private fun setupListener() {
        binding.btnAudioToggle.setOnClickListener {
            val isMuted = exoplayer?.volume == 0f
            exoplayer?.volume = if (isMuted) 1f else 0f

            binding.btnAudioToggle.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (isMuted) R.drawable.amity_ic_story_audio_unmute else R.drawable.amity_ic_story_audio_mute
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer?.stop()
        exoplayer?.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
