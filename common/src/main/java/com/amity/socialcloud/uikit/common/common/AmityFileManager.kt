package com.amity.socialcloud.uikit.common.common

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File


class AmityFileManager {

    companion object {

        fun saveFile(context: Context, url: String, fileName: String) {
            if (url.isNotEmptyOrBlank() && fileName.isNotEmptyOrBlank()) {
                val dirPath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString()
                val filePath = "$dirPath/$fileName"
                val file = File(filePath)
                val uri = Uri.fromFile(file)
                val downloadRequest = DownloadManager.Request(Uri.parse(url))
                downloadRequest.setTitle(fileName)
                downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                downloadRequest.setDestinationUri(uri)
                val downloadmanager: DownloadManager =
                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadmanager.enqueue(downloadRequest)
            }
        }

    }
}