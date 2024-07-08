package com.amity.socialcloud.uikit.common.infra.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "ad_recency")
data class AmityAdRecency(
    @PrimaryKey
    val adId: String,
    val lastSeen: DateTime
)