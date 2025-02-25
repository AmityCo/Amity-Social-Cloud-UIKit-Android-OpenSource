package com.amity.socialcloud.uikit.common.infra.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.ekoapp.ekosdk.internal.util.AppContext
import java.io.File


object AmityDownloader {
    private val downloadManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        AppContext.get().getSystemService(DownloadManager::class.java)
    } else {
        AppContext.get().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    fun enqueue(url: String): Long {
        val privateDir = File(AppContext.get().filesDir, "amityDir")
        if (!privateDir.exists()) {
            privateDir.mkdirs()
        }
        val fileName = url.hashCode().toString() + ".jpg"
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setDestinationInExternalFilesDir(AppContext.get(), null, fileName)

        return downloadManager.enqueue(request)
    }

    fun remove(id: Long, url: String) {
        downloadManager.remove(id)
        val privateDir = File(AppContext.get().filesDir, "amityDir")
        val fileName = url.hashCode().toString() + ".jpg"
        val file = File(privateDir, fileName)
        if (file.exists()) {
            file.delete()
        }
    }

    @SuppressLint("Range")
    fun getStatus(id: Long): Int {
        val query = DownloadManager.Query().setFilterById(id)
        val cursor = downloadManager.query(query)
        return if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        } else {
            DownloadManager.STATUS_FAILED
        }
    }


}