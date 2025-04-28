package com.amity.socialcloud.uikit.common.infra.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amity.socialcloud.uikit.common.infra.db.dao.AmityAdAssetDao
import com.amity.socialcloud.uikit.common.infra.db.dao.AmityAdRecencyDao
import com.amity.socialcloud.uikit.common.infra.db.dao.AmityNetworkConfigDao
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdAsset
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdRecency
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityNetworkConfig
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext


@Database(
    version = 2,
    entities = [
        AmityAdAsset::class,
        AmityAdRecency::class,
        AmityNetworkConfig::class
    ],
)
@TypeConverters(
    AmityDateTimeTypeConverter::class,
    AmityJsonObjectTypeConverter::class
)
abstract class AmityUIKitDB : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AmityUIKitDB? = null

        fun init(): AmityUIKitDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    AmityAppContext.getContext(),
                    AmityUIKitDB::class.java,
                    "amitydb"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun get(): AmityUIKitDB {
            return INSTANCE ?: throw IllegalStateException("AmityUIKitDB is not initialized")
        }
    }

    abstract fun adAssetDao(): AmityAdAssetDao

    abstract fun adRecencyDao(): AmityAdRecencyDao

    abstract fun networkConfigDao(): AmityNetworkConfigDao

}