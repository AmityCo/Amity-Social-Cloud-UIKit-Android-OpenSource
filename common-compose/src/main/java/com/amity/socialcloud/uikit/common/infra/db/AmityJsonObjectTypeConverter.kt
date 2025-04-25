package com.amity.socialcloud.uikit.common.infra.db

import androidx.room.TypeConverter
import com.ekoapp.ekosdk.internal.data.converter.EkoGson.get
import com.google.gson.JsonObject

class AmityJsonObjectTypeConverter {
    @TypeConverter
    fun stringToJsonObject(json: String?): JsonObject {
        return if (json.isNullOrBlank()) {
            JsonObject()
        } else get().fromJson(json, JsonObject::class.java)
    }

    @TypeConverter
    fun jsonObjectToString(jsonObject: JsonObject?): String? {
        return if (jsonObject == null) {
            null
        } else get().toJson(jsonObject)
    }
}
