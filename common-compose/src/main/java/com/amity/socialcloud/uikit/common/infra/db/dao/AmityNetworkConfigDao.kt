package com.amity.socialcloud.uikit.common.infra.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdRecency
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityNetworkConfig


@Dao
interface AmityNetworkConfigDao {

    @Query("SELECT * FROM network_config limit 1")
    fun getNetworkConfig(): AmityNetworkConfig?

    @Query("DELETE FROM network_config")
    fun deleteAll()

    @Query("DELETE FROM network_config WHERE id = :id")
    fun deleteNetworkConfig(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(networkConfig: AmityNetworkConfig): Long

    @Update
    fun update(networkConfig: AmityNetworkConfig)

    @Transaction
    fun upsert(networkConfig: AmityNetworkConfig) {
        val id = insert(networkConfig)
        if (id == -1L) {
            update(networkConfig)
        }
    }
}