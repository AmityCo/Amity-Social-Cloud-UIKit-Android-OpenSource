package com.amity.socialcloud.uikit.common.ad

import com.amity.socialcloud.uikit.common.infra.db.AmityUIKitDB
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdRecency
import org.joda.time.DateTime

class AmityAdRecencyRepository {

    fun getLastSeen(adId: String?): DateTime? {
        if(adId == null) return null
        return AmityUIKitDB.get().adRecencyDao().getAdRecency(adId)?.lastSeen
    }

    fun updateLastSeen(adId: String, lastSeen: DateTime) {
        AmityUIKitDB.get().adRecencyDao().upsert(AmityAdRecency(adId, lastSeen))
    }

    fun deleteAll() {
        AmityUIKitDB.get().adRecencyDao().deleteAll()
    }

}