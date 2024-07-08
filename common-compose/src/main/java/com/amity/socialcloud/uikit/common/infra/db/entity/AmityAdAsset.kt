package com.amity.socialcloud.uikit.common.infra.db.entity

import android.app.DownloadManager
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ad_asset")
data class AmityAdAsset(
    @PrimaryKey
    val fileUrl: String,
    val downloadStatus: Int = -1,
    val downloadId: Long = -1L
)