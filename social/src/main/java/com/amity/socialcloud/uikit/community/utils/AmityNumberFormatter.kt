package com.amity.socialcloud.uikit.community.utils

import java.math.RoundingMode
import java.text.DecimalFormat

fun Long.formatFollowers(): String? {
    if ((this / 1000) > 1) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        var formattedNumber = this.toDouble() / 1000;
        return df.format(formattedNumber).toString() + "K";
    }
    return this.toString()
}

fun Int.formatFollowers(): String? {
    if ((this / 1000) > 1) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        var formattedNumber = this.toDouble() / 1000;
        return df.format(formattedNumber).toString() + "K";
    }
    return this.toString()
}
