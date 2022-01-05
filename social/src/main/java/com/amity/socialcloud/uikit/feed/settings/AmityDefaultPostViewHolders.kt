package com.amity.socialcloud.uikit.feed.settings

import android.view.View
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.adapter.*

object AmityDefaultPostViewHolders {
    internal const val text = "text"
    internal const val poll = "poll"
    internal const val image = "image"
    internal const val video = "video"
    internal const val file = "file"
    internal const val livestream = "liveStream"
    internal const val unknown = "unknown"


    val textViewHolder = object : AmityPostRenderer {

        override fun getDataType(): String = text

        override fun getLayoutId(): Int = R.layout.amity_item_text_post

        override fun createViewHolder(view: View): AmityPostContentViewHolder {
            return AmityPostItemTextViewHolder(view)
        }

        override fun enableHeader(): Boolean = true

        override fun enableFooter(): Boolean = true
    }

    val pollViewHolder = object : AmityPostRenderer {

        override fun getDataType(): String = poll

        override fun getLayoutId(): Int = R.layout.amity_item_poll_post

        override fun createViewHolder(view: View): AmityPostItemPollViewHolder {
            return AmityPostItemPollViewHolder(view)
        }

        override fun enableHeader(): Boolean = true

        override fun enableFooter(): Boolean = true
    }

    val imageViewHolder = object : AmityPostRenderer {
        override fun getDataType(): String = image

        override fun getLayoutId(): Int = R.layout.amity_item_image_post

        override fun createViewHolder(view: View): AmityPostContentViewHolder {
            return AmityPostItemImageViewHolder(view)
        }

        override fun enableHeader(): Boolean = true

        override fun enableFooter(): Boolean = true
    }

    val videoViewHolder = object : AmityPostRenderer {
        override fun getDataType(): String = video

        override fun getLayoutId(): Int = R.layout.amity_item_video_post

        override fun createViewHolder(view: View): AmityPostContentViewHolder {
            return AmityPostItemVideoViewHolder(view)
        }

        override fun enableHeader(): Boolean = true

        override fun enableFooter(): Boolean = true
    }


    val fileViewHolder = object : AmityPostRenderer {

        override fun getDataType(): String = file

        override fun getLayoutId(): Int = R.layout.amity_item_files_post

        override fun createViewHolder(view: View): AmityPostContentViewHolder {
            return AmityPostItemAttachmentViewHolder(view)
        }

        override fun enableHeader(): Boolean = true

        override fun enableFooter(): Boolean = true
    }

    val livestreamViewHolder = object : AmityPostRenderer {

        override fun getDataType(): String = livestream

        override fun getLayoutId(): Int = R.layout.amity_item_livestream_post

        override fun createViewHolder(view: View): AmityPostItemLivestreamViewHolder {
            return AmityPostItemLivestreamViewHolder(view)
        }

        override fun enableHeader(): Boolean = true

        override fun enableFooter(): Boolean = true
    }

    internal val unknownViewHolder = object : AmityPostRenderer {
        override fun getDataType(): String = unknown

        override fun getLayoutId(): Int {
            return R.layout.amity_item_unknown_post
        }

        override fun createViewHolder(view: View): AmityPostContentViewHolder {
            return  AmityPostItemUnknownViewHolder(view)
        }

        override fun enableHeader(): Boolean = false

        override fun enableFooter(): Boolean = false
    }

    internal fun getDefaultMap() : MutableMap<String, AmityPostRenderer> {
        return mutableMapOf(
            Pair(text, textViewHolder),
            Pair(poll, pollViewHolder),
            Pair(image, imageViewHolder),
            Pair(video, videoViewHolder),
            Pair(file, fileViewHolder),
            Pair(livestream, livestreamViewHolder),
            Pair(unknown, unknownViewHolder)
        )
    }
}