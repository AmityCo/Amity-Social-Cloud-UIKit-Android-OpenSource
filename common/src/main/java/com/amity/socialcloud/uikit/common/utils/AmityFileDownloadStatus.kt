package com.amity.socialcloud.uikit.common.utils

import android.net.Uri

interface AmityFileDownloadStatus {
    fun onDownloadComplete(fileUri: Uri)
    fun onError(error: String?)
    fun onProgressUpdate(progress: Int)
}