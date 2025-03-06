package com.amity.socialcloud.uikit.common.memberpicker.adapter

import java.text.Normalizer
import java.util.Locale
import java.util.regex.Pattern


object DisplayNameGrouper {
    // Pattern to match combining diacritical marks.
    private val DIACRITICS: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")

    fun getGroupingKey(displayName: String?): String {
        if (displayName.isNullOrEmpty()) {
            return "#" // fallback for empty names
        }
        // Normalize to decompose characters (e.g., "Å" becomes "A" + "◌̊")
        val normalized = Normalizer.normalize(displayName, Normalizer.Form.NFD)
        // Remove diacritical marks
        val withoutDiacritics = DIACRITICS.matcher(normalized).replaceAll("")
        // Convert the entire string to uppercase to standardize
        val upperCaseName = withoutDiacritics.uppercase(Locale.getDefault())

        // Get the first character and check if it's in A-Z
        val firstChar = upperCaseName[0]
        if (firstChar < 'A' || firstChar > 'Z') {
            return "#"
        }
        return firstChar.toString()
    }
}
