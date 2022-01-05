package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.utils.AmityScreenUtils
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityVideoPlayerActivity
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostImageActionListener
import com.amity.socialcloud.uikit.community.newsfeed.model.FileUploadState
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia
import com.amity.socialcloud.uikit.community.utils.AmityCommunityNavigation
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily


class AmityCreatePostImageViewHolder(
    private val editingPostId: String?,
    itemView: View,
    val listener: AmityCreatePostImageActionListener,
    private var itemChangeListener: IListItemChangeListener
) :
    RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewAdapter.IBinder<PostMedia> {


    val container: ConstraintLayout = itemView.findViewById(R.id.container)
    var photo: ShapeableImageView = itemView.findViewById(R.id.ivPhoto)
    var removePhoto: ShapeableImageView = itemView.findViewById(R.id.ivCross)
    var errorPhoto: ShapeableImageView = itemView.findViewById(R.id.ivError)
    private val progrssBar: ProgressBar = itemView.findViewById(R.id.pbImageUpload)
    private val playIcon: ShapeableImageView = itemView.findViewById(R.id.ivPlay)
    private val radius: Float = itemView.context.resources.getDimension(R.dimen.amity_four)

    override fun bind(data: PostMedia?, position: Int) {
        setupShape()
        if (container.tag != itemChangeListener.itemCount().toString()) {

            var containerHeight = getHeight(itemChangeListener.itemCount())
            var containerWidth = getWidth(itemChangeListener.itemCount())
            if (container.width != containerWidth || containerHeight != container.height) {
                container.layoutParams.apply {
                    width = containerWidth
                    height = containerHeight
                }
            }
            container.tag = itemChangeListener.itemCount().toString()
        }

        if (photo.tag != data!!.url.toString()) {
            Glide.with(itemView.context)
                .load(data.url)
                .into(photo)
            photo.tag = data.url.toString()
        }

        playIcon.visibility = View.GONE
        container.setOnClickListener(null)
        progrssBar.visibility =
            if (data.uploadState == FileUploadState.UPLOADING) View.VISIBLE else View.GONE

        if (data.uploadState == FileUploadState.UPLOADING) {
            if (progrssBar.visibility != View.VISIBLE)
                progrssBar.visibility = View.VISIBLE

        } else {
            progrssBar.visibility = View.GONE
        }
        progrssBar.progress = data.currentProgress
        if (data.uploadState == FileUploadState.COMPLETE) {
            playIcon.visibility =
                if (data.type == PostMedia.Type.VIDEO) View.VISIBLE else View.GONE
            photo.alpha = 1F
            if (data.type == PostMedia.Type.VIDEO) {
                container.setOnClickListener { openVideoViewer(it.context, data) }
            }
        } else if (photo.alpha != 0.6F) {
            photo.alpha = 0.6F
        }

        errorPhoto.visibility =
            if (data.uploadState == FileUploadState.FAILED) View.VISIBLE else View.GONE

        removePhoto.setOnClickListener {
            listener.onRemoveImage(data, position)
        }
    }

    private fun openVideoViewer(context: Context, media: PostMedia) {
        if (!editingPostId.isNullOrEmpty()) {
            AmityCommunityNavigation.navigateToVideoPreview(
                context,
                editingPostId,
                bindingAdapterPosition
            )
        } else {
            val videoIntent = AmityVideoPlayerActivity
                .newIntent(context = context, videoUrl = media.url.toString())
            context.startActivity(videoIntent)
        }
    }

    private fun setupShape() {
        val shapeAppearanceModel = photo.shapeAppearanceModel
            .toBuilder()
        photo.shapeAppearanceModel = shapeAppearanceModel
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()

    }

    private fun getHeight(itemCount: Int): Int {
        var dimenRes: Int = -1
        dimenRes = when (itemCount) {
            1, 2 -> R.dimen.amity_three_hundred_twenty_eight
            else -> {
                R.dimen.amity_one_hundred_twenty
            }
        }
        return itemView.context.resources.getDimensionPixelSize(dimenRes)
    }

    private fun getWidth(itemCount: Int): Int {
        val margin: Int = itemView.context.resources.getDimensionPixelSize(R.dimen.amity_sixteen)
        var dimenRes: Int = itemView.context.resources.getDimensionPixelSize(R.dimen.amity_eight)
        dimenRes = when (itemCount) {
            1 -> return AmityScreenUtils.getScreenWidth(itemView.context) - margin * 2
            2 -> {
                return AmityScreenUtils.getHalfScreenWidth(itemView.context) - margin - dimenRes / 2
            }
            else -> {
                return AmityScreenUtils.getOneThirdScreenWidth(itemView.context) - margin - dimenRes / 2
            }
        }
        return itemView.context.resources.getDimensionPixelSize(dimenRes)
    }
}