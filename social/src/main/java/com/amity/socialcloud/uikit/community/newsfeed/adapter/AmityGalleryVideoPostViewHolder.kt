package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.databinding.AmityItemGalleryPostVideoBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.PostGalleryClickEvent
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AmityGalleryVideoPostViewHolder constructor(
    private val binding: AmityItemGalleryPostVideoBinding,
    private val postClickPublisher: PublishSubject<PostGalleryClickEvent>,
) : RecyclerView.ViewHolder(binding.root) {

    private var durationDisposable = CompositeDisposable()

    fun bind(post: AmityPost?) {
        durationDisposable.clear()
        binding.itemGalleryPostVideoContainer.setOnClickListener { _ ->
            post?.let { postClickPublisher.onNext(PostGalleryClickEvent.Video(it)) }
        }
        binding.itemGalleryPostVideoImageview.setImageDrawable(null)
        if (post?.getData() is AmityPost.Data.VIDEO) {
            val video = (post.getData() as AmityPost.Data.VIDEO)
            renderThumbnail(video)
            renderVideoDuration(video)
        }
    }

    private fun renderThumbnail(video: AmityPost.Data.VIDEO) {
        val thumbnail = video.getThumbnailImage()
        thumbnail?.let {
            Glide.with(itemView)
                .load(it.getUrl(AmityImage.Size.MEDIUM))
                .into(binding.itemGalleryPostVideoImageview)
        }
    }

    private fun renderVideoDuration(video: AmityPost.Data.VIDEO) {
        durationDisposable.add(
            getVideoDuration(video)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { hideVideoDuration() }
            .doOnSuccess { duration ->
                if (duration != NO_DURATION) {
                    showVideoDuration(duration)
                } else {
                    hideVideoDuration()
                }
            }
            .doOnError {
                hideVideoDuration()
            }
            .subscribe()
        )
    }

    private fun getVideoDuration(video: AmityPost.Data.VIDEO): Single<Number> {
        return video.getVideo()
            .map {
                it.getMetadata()
                    ?.getAsJsonObject("video")
                    ?.get("duration")
                    ?.asNumber
                    ?: NO_DURATION
            }
    }

    private fun showVideoDuration(duration: Number) {
        val seconds = duration.toLong()
        val hours = TimeUnit.SECONDS.toHours(seconds)
        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val remainingSeconds = seconds % 60
        val formattedDuration = when {
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, remainingSeconds)
            else -> String.format("%02d:%02d", minutes, remainingSeconds)
        }

        binding.itemGalleryPostVideoTextview.visibility = View.VISIBLE
        binding.itemGalleryPostVideoTextview.text = formattedDuration
    }

    private fun hideVideoDuration() {
        binding.itemGalleryPostVideoTextview.visibility = View.GONE
    }
}

const val NO_DURATION = -1
