package com.amity.socialcloud.uikit.common.ad

import com.amity.socialcloud.uikit.common.infra.db.AmityUIKitDB
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdAsset

class AmityAdAssetRepository {
    fun getAllAdAssets(): List<AmityAdAsset> {
        return AmityUIKitDB.get().adAssetDao().getAll()
    }

    fun getAdAsset(fileUrl: String): AmityAdAsset? {
        return AmityUIKitDB.get().adAssetDao().getAdAsset(fileUrl)
    }

    fun insertAdAsset(adAsset: AmityAdAsset) {
        AmityUIKitDB.get().adAssetDao().insert(adAsset)
    }

    fun updateDownloadId(fileUrl: String, downloadId: Long) {
        AmityUIKitDB.get().adAssetDao().updateDownloadId(fileUrl, downloadId)
    }

    fun updateDownloadStatus(downloadId: Long, status: Int) {
        AmityUIKitDB.get().adAssetDao().updateStatus(downloadId, status)
    }

    fun deleteAdAsset(fileUrl: String) {
        AmityUIKitDB.get().adAssetDao().deleteAdAsset(fileUrl)
    }

    fun deleteAll() {
        AmityUIKitDB.get().adAssetDao().deleteAll()
    }

}