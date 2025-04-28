package com.amity.socialcloud.uikit.common.infra.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject


@Entity(tableName = "network_config")
data class AmityNetworkConfig(
    @PrimaryKey
    var id: String,
    var config: JsonObject?
)