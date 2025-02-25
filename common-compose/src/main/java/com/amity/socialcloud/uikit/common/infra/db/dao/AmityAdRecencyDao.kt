package com.amity.socialcloud.uikit.common.infra.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdAsset
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdRecency
import org.joda.time.DateTime


@Dao
interface AmityAdRecencyDao {

    @Query("SELECT * FROM ad_recency WHERE adId = :adId")
    fun getAdRecency(adId: String): AmityAdRecency?

    @Query("DELETE FROM ad_recency")
    fun deleteAll()

    @Query("DELETE FROM ad_recency WHERE adId = :adId")
    fun deleteAdRecency(adId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(adRecency: AmityAdRecency): Long

    @Update
    fun update(adRecency: AmityAdRecency)

    @Transaction
    fun upsert(adRecency: AmityAdRecency) {
        val id = insert(adRecency)
        if (id == -1L) {
            update(adRecency)
        }
    }
}