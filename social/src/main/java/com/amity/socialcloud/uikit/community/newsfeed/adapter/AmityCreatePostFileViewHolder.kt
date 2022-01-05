package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostFileActionListener
import com.amity.socialcloud.uikit.community.newsfeed.model.FileUploadState


class AmityCreatePostFileViewHolder(
    itemView: View,
    val listener: AmityCreatePostFileActionListener?
) : AmityBasePostAttachmentViewHolder(itemView) {

    private val removeFile: ImageView = itemView.findViewById(R.id.ivRemove)
    private val errorFile: ImageView = itemView.findViewById(R.id.ivError)
    private val overlayView: View = itemView.findViewById(R.id.overlay)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress)
    private var layoutPreparingFile: LinearLayout = itemView.findViewById(R.id.layoutPreparingFile)

    override fun bind(data: AmityFileAttachment?, position: Int) {
        super.bind(data, position)
        if (data != null) {
            removeFile.setOnClickListener {
                listener?.onRemoveFile(data, position)
            }
            // progressBar.visibility = if(data.uploadState == FileUploadState.UPLOADING) View.VISIBLE else View.GONE
            progressBar.progress = data.progress
            layoutPreparingFile.visibility =
                if (data.uploadState == FileUploadState.PENDING) View.VISIBLE else View.GONE
            if (data.uploadState == FileUploadState.PENDING || data.uploadState == FileUploadState.FAILED) {
                overlayView.visibility = View.VISIBLE
            } else if (overlayView.visibility == View.VISIBLE) {
                overlayView.visibility = View.GONE
            }

            errorFile.visibility =
                if (data.uploadState == FileUploadState.FAILED) View.VISIBLE else View.GONE
        }

    }

    override fun getMaxCharacterLimit(): Int {
        return itemView.resources.getInteger(R.integer.max_character_create_post)
    }


}
