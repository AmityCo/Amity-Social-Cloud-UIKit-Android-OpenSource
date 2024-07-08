package com.amity.socialcloud.uikit.common.ad

import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import kotlin.math.ceil


object AmityAdTimeWindowTracker {

    private val markedTimeWindow: MutableMap<AmityAdPlacement,String> = mutableMapOf()
    fun hasReachedTimeWindowLimit(placement: AmityAdPlacement): Boolean {
        val currentWindowKey = getCurrentWindowKey(placement)
        return markedTimeWindow[placement] == currentWindowKey
    }
    fun markSeen(placement: AmityAdPlacement) {
        markedTimeWindow[placement] = getCurrentWindowKey(placement)
    }

    fun clear() {
        markedTimeWindow.clear()
    }

    private fun getMinutesSinceStartOfDay(): Double {
        val now = DateTime.now()
        val startOfDay = now.withTimeAtStartOfDay()
        return (now.millis - startOfDay.millis) / 60000.0
    }
    private fun getCurrentWindowKey(placement: AmityAdPlacement): String {
        val windowSizeInMinutes = getTimeWindowSettings(placement)
        val windowIndex = ceil(getMinutesSinceStartOfDay() / windowSizeInMinutes)
        val today = DateTime.now().toString(DateTimeFormat.forPattern("dd-MM-yyyy-"))
        return today + windowIndex
    }
    private fun getTimeWindowSettings(placement: AmityAdPlacement): Int {
        val adFrequency = AmityAdEngine.getAdFrequency(placement)
        return if(adFrequency == null || adFrequency.getType() != "time-window") {
            0
        } else {
            adFrequency.getValue()
        }
    }

}