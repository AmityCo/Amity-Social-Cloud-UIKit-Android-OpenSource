package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewAdapter
import com.amity.socialcloud.uikit.common.common.AmityFileUtils
import com.amity.socialcloud.uikit.common.utils.AmityConstants.FILE_EXTENSION_SEPARATOR
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostFileItemClickListener


open class AmityBasePostAttachmentViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView), AmityBaseRecyclerViewAdapter.IBinder<AmityFileAttachment> {

    private var fileName: TextView = itemView.findViewById(R.id.tvFileName)
    private var fileSize: TextView = itemView.findViewById(R.id.tvFileSize)
    private val fileIcon: ImageView = itemView.findViewById(R.id.ivFileIcon)

    private var itemClickListener: AmityPostFileItemClickListener? = null

    constructor(itemView: View, itemClickListener: AmityPostFileItemClickListener?) : this(itemView) {
        this.itemClickListener = itemClickListener
    }

    override fun bind(data: AmityFileAttachment?, position: Int) {
        data?.let {
            val fileNameTruncated = data.name
            setFileName(fileNameTruncated)
            setFileIcon(data)
            fileSize.text =
                AmityFileUtils.humanReadableByteCount(data.size, true)
        }

        itemView.setOnClickListener {
            data?.let {
                itemClickListener?.onClickFileItem(it)
            }
        }
    }

    open fun getMaxCharacterLimit(): Int {
        return itemView.resources.getInteger(R.integer.maxCharacterNewsFeed)
    }

    private fun setFileIcon(data: AmityFileAttachment) {
        val drawableRes = AmityFileUtils.getFileIcon(data.mimeType)
        fileIcon.setImageDrawable(itemView.context.getDrawable(drawableRes))
    }

    private fun setFileName(originalName: String) {
        var fileNameTruncated = originalName
        if (originalName.length > getMaxCharacterLimit()) {

            val fileExtension: String = originalName.substringAfterLast(FILE_EXTENSION_SEPARATOR)
            val lastCharShown: Int = getMaxCharacterLimit() - fileExtension.length - 5
            fileNameTruncated = try {
                originalName.substringBeforeLast(FILE_EXTENSION_SEPARATOR)
                    .substring(0, lastCharShown) + "... ." + fileExtension
            } catch (e: StringIndexOutOfBoundsException) {
                //Protect against the case that extension name is very long
                //For example : hello.amitycompanyisawesome or x.mircrosoftwordfile
                originalName
            }
        }
        fileName.text = fileNameTruncated
    }
}
