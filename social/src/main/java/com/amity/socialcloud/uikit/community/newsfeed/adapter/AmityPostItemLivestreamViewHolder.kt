package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.content.Context
import android.view.View
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.sdk.video.stream.AmityStream
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemLivestreamPostBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostLivestreamClickListener
import com.bumptech.glide.Glide
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmityPostItemLivestreamViewHolder(itemView: View) : AmityPostContentViewHolder(itemView) {

    private var liveStreamClickListener = object : AmityPostLivestreamClickListener {
        override fun onClickLivestreamVideo(stream: AmityStream) {
            postContentClickPublisher.onNext(PostContentClickEvent.Livestream(stream))
        }
    }

    override fun bind(post: AmityPost) {
        val context = itemView.context
        val binding = AmityItemLivestreamPostBinding.bind(itemView)

        val data = post.getChildren()[0].getData() as AmityPost.Data.LIVE_STREAM

        setPostText(post, showFullContent)

        data.getStream()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                when {
                    it.isDeleted() -> {
                        presentPlaceholder(binding)
                    }
                    it.getStatus() == AmityStream.Status.LIVE -> {
                        presentLiveVideo(context, it, binding)
                    }
                    it.getStatus() == AmityStream.Status.RECORDED -> {
                        presentRecordedVideo(context, it, binding)
                    }
                    it.getStatus() == AmityStream.Status.ENDED -> {
                        presentEndedVideo(binding)
                    }
                    else -> {
                        presentPlaceholder(binding)
                    }
                }
            }
            .doOnError { presentPlaceholder(binding) }
            .untilLifecycleEnd(itemView)
            .subscribe()
    }

    private fun presentLiveVideo(
        context: Context,
        stream: AmityStream,
        binding: AmityItemLivestreamPostBinding
    ) {
        binding.unavailableVideoContainer.visibility = View.GONE
        binding.endedVideoContainer.visibility = View.GONE
        binding.liveVideoContainer.visibility = View.VISIBLE
        binding.liveTextview.visibility = View.VISIBLE
        binding.recordedTextview.visibility = View.GONE
        binding.playIcon.visibility = View.VISIBLE
        val imageUrl = stream.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.amity_default_stream_thumbnail)
                .into(binding.thumbnailImageview)
        } else {
            binding.thumbnailImageview.setImageResource(R.drawable.amity_default_stream_thumbnail)
        }
        binding.liveVideoContainer.setOnClickListener {
            liveStreamClickListener.onClickLivestreamVideo(stream)
        }
    }

    private fun presentRecordedVideo(
        context: Context,
        stream: AmityStream,
        binding: AmityItemLivestreamPostBinding
    ) {
        binding.unavailableVideoContainer.visibility = View.GONE
        binding.endedVideoContainer.visibility = View.GONE
        binding.liveVideoContainer.visibility = View.VISIBLE
        binding.liveTextview.visibility = View.GONE
        binding.recordedTextview.visibility = View.VISIBLE
        binding.playIcon.visibility = View.VISIBLE
        val imageUrl = stream.getThumbnailImage()?.getUrl(AmityImage.Size.MEDIUM)
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.amity_default_stream_thumbnail)
                .into(binding.thumbnailImageview)
        } else {
            binding.thumbnailImageview.setImageResource(R.drawable.amity_default_stream_thumbnail)
        }
        binding.liveVideoContainer.setOnClickListener {
            liveStreamClickListener.onClickLivestreamVideo(stream)
        }
    }

    private fun presentEndedVideo(binding: AmityItemLivestreamPostBinding) {
        binding.unavailableVideoContainer.visibility = View.GONE
        binding.endedVideoContainer.visibility = View.VISIBLE
        binding.liveVideoContainer.visibility = View.GONE
        binding.playIcon.visibility = View.VISIBLE
        binding.thumbnailImageview.setImageDrawable(null)
        binding.liveVideoContainer.setOnClickListener(null)
    }

    private fun presentPlaceholder(binding: AmityItemLivestreamPostBinding) {
        binding.unavailableVideoContainer.visibility = View.VISIBLE
        binding.endedVideoContainer.visibility = View.GONE
        binding.liveVideoContainer.visibility = View.GONE
        binding.liveTextview.visibility = View.GONE
        binding.recordedTextview.visibility = View.GONE
        binding.playIcon.visibility = View.GONE
        binding.thumbnailImageview.setImageDrawable(null)
        binding.liveVideoContainer.setOnClickListener(null)
    }
}