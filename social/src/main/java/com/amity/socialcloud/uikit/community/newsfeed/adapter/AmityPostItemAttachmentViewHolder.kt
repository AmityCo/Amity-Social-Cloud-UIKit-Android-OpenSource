package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmitySpacesItemDecoration
import com.amity.socialcloud.uikit.common.common.AmityFileUtils
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment
import com.amity.socialcloud.uikit.community.newsfeed.events.PostContentClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostFileItemClickListener
import com.amity.socialcloud.uikit.community.newsfeed.model.FileUploadState


class AmityPostItemAttachmentViewHolder(itemView: View) : AmityPostContentViewHolder(itemView) {

    private val rvAttachment = itemView.findViewById<RecyclerView>(R.id.rvAttachment)
    private val space = itemView.context.resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
    private val itemDecor = AmitySpacesItemDecoration(0, 0, 0, space)
    private var files: List<AmityFile> = emptyList()

    private var loadMoreFilesClickListener =
        object : AmityPostViewFileAdapter.ILoadMoreFilesClickListener {

            override fun loadMoreFiles(post: AmityPost) {
                postContentClickPublisher.onNext(PostContentClickEvent.Text(post))
            }
        }

    private var fileItemClickListener = object : AmityPostFileItemClickListener {

        override fun onClickFileItem(file: AmityFileAttachment) {
            val selectedFile = files.find { it.getFileId() == file.id }
            selectedFile?.let {
                postContentClickPublisher.onNext(PostContentClickEvent.File(it))
            }
        }
    }

    override fun bind(post: AmityPost) {
        setPostText(post, showFullContent)
        val files = mutableListOf<AmityFile>()
        if (!post.getChildren().isNullOrEmpty()) {
            post.getChildren().forEach {
                when (val postData = it.getData()) {
                    is AmityPost.Data.FILE -> {
                        postData.getFile()?.let { ekoFile ->
                            files.add(ekoFile)
                        }
                    }
                    else -> {}
                }
            }
            this.files = files
            initAttachments(post, !showFullContent)
        }
    }

    private fun initAttachments(data: AmityPost, isCollapsed: Boolean) {
        val adapter = AmityPostViewFileAdapter(
            loadMoreFilesClickListener,
            fileItemClickListener,
            data,
            isCollapsed
        )
        rvAttachment.removeItemDecoration(itemDecor)
        rvAttachment.addItemDecoration(itemDecor)
        rvAttachment.layoutManager = LinearLayoutManager(itemView.context)
        rvAttachment.adapter = adapter
        adapter.submitList(mapFilesToFileAttachments(files))
    }

    private fun mapFilesToFileAttachments(ekoFile: List<AmityFile>): List<AmityFileAttachment> {
        return ekoFile.map {
            val fileSize = it.getFileSize()?.toLong() ?: 0L
            AmityFileAttachment(
                it.getFileId(),
                null,
                it.getFileName() ?: "",
                fileSize,
                Uri.parse(it.getUrl()),
                AmityFileUtils.humanReadableByteCount(fileSize, true) ?: "",
                it.getMimeType() ?: "",
                FileUploadState.COMPLETE,
                100
            )
        }
    }

}