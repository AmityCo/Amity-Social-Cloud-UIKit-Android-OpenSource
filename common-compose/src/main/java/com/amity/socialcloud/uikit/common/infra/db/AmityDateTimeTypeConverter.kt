package com.amity.socialcloud.uikit.common.infra.db

import androidx.room.TypeConverter
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.joda.time.DateTime
import java.lang.reflect.Type


class AmityDateTimeTypeConverter : JsonDeserializer<DateTime>, JsonSerializer<DateTime> {
    @TypeConverter
    fun stringToDateTime(dateTimeString: String?): DateTime? {
        return dateTimeString?.let { DateTime(it) }
    }

    @TypeConverter
    fun dateTimeToString(dateTime: DateTime?): String? {
        return dateTime?.toString()
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DateTime {
        return DateTime(json.asJsonPrimitive.asString)
    }

    override fun serialize(src: DateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }
}