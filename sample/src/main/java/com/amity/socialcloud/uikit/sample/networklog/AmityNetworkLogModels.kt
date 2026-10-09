package com.amity.socialcloud.uikit.sample.networklog

import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivity
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivityType

enum class AmityNetworkLogRowVariant { REQUEST, MESSAGE, STATE, GROUP, DROPPED }

enum class AmityNetworkLogStatusClass { SUCCESS, NEUTRAL, WARNING, ERROR, IN_FLIGHT, NONE }

/** Filter chips. Five chips over eight activity types — mapping owned by the viewer. */
enum class AmityNetworkLogFilter(val label: String, val types: Set<AmityNetworkActivityType>) {
    REST("REST", setOf(
        AmityNetworkActivityType.API,
        AmityNetworkActivityType.UPLOAD,
        AmityNetworkActivityType.DOWNLOAD,
    )),
    MQTT("MQTT", setOf(
        AmityNetworkActivityType.MQTT_MESSAGE,
        AmityNetworkActivityType.MQTT_STATE,
    )),
    HLS("HLS", setOf(AmityNetworkActivityType.MEDIA_SEGMENT)),
    IMAGES("IMG", setOf(AmityNetworkActivityType.IMAGE)),
    EVENTS("EVT", setOf(AmityNetworkActivityType.STREAM_STATE));

    companion object {
        fun of(type: AmityNetworkActivityType): AmityNetworkLogFilter? =
            entries.firstOrNull { type in it.types }
    }
}

/**
 * Precomputed row view model. All formatting happens on the collector's thread so no
 * string work occurs during composition.
 */
data class AmityNetworkLogEntry(
    val id: String,
    val variant: AmityNetworkLogRowVariant,
    val type: AmityNetworkActivityType,
    val timestampLabel: String,
    val chipLabel: String,
    val primaryLabel: String,
    val statusLabel: String?,
    val statusClass: AmityNetworkLogStatusClass,
    val childCount: Int? = null,
    val failureCount: Int? = null,
    val children: List<AmityNetworkLogEntry> = emptyList(),
    val activity: AmityNetworkActivity? = null,
)
