package com.amity.socialcloud.uikit.common.infra.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdAsset


@Dao
interface AmityAdAssetDao {
    @Query("SELECT * FROM ad_asset")
    fun getAll(): List<AmityAdAsset>

    @Query("SELECT * FROM ad_asset WHERE fileUrl = :fileUrl")
    fun getAdAsset(fileUrl: String): AmityAdAsset

    @Query("DELETE FROM ad_asset")
    fun deleteAll()

    @Query("DELETE FROM ad_asset WHERE fileUrl = :fileUrl")
    fun deleteAdAsset(fileUrl: String)

    @Query("UPDATE ad_asset SET downloadStatus = :status WHERE downloadId = :downloadId")
    fun updateStatus(downloadId: Long, status: Int)

    @Query("UPDATE ad_asset SET downloadStatus = :status WHERE fileUrl = :fileUrl")
    fun updateStatus(fileUrl: String, status: Int)

    @Query("UPDATE ad_asset SET downloadId = :downloadId WHERE fileUrl = :fileUrl")
    fun updateDownloadId(fileUrl: String, downloadId: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(adAsset: AmityAdAsset): Long

    @Update
    fun update(adAsset: AmityAdAsset)

    @Transaction
    fun upsert(adAsset: AmityAdAsset) {
        val id = insert(adAsset)
        if (id == -1L) {
            update(adAsset)
        }
    }

}