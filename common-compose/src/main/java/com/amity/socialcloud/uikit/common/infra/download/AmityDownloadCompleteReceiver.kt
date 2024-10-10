package com.amity.socialcloud.uikit.common.infra.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.amity.socialcloud.uikit.common.ad.AmityAdAssetRepository


class AmityDownloadCompleteReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if(id != -1L) {
                try {
                    AmityAdAssetRepository().updateDownloadStatus(id, DownloadManager.STATUS_SUCCESSFUL)
                } catch (e: Exception) {
                
                }
            }
        }
    }
}