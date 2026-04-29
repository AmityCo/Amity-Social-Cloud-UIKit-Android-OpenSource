package com.amity.socialcloud.uikit.common.utils

import android.content.Context
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Format currency according to the device/app locale using ISO-4217 currency codes when possible.
 * If currencyCode is null/blank, formats as a plain number.
 * If currencyCode is invalid (or a symbol), falls back to showing "CODE <amount>" or the plain number.
 */
fun formatCurrencyForLocale(context: Context, amount: Double, currencyCode: String?): String {
    val locale: Locale = try {
        context.resources.configuration.locales[0]
    } catch (e: Exception) {
        Locale.getDefault()
    }

    return try {
        if (currencyCode.isNullOrBlank()) {
            NumberFormat.getNumberInstance(locale).format(amount)
        } else {
            // Try to interpret currencyCode as an ISO-4217 code first
            val currency = Currency.getInstance(currencyCode)
            NumberFormat.getCurrencyInstance(locale).apply { this.currency = currency }.format(amount)
        }
    } catch (e: Exception) {
        // Fallback: if currencyCode looks like a symbol (single char), show symbol + number
        // otherwise show code + number
        val formattedNumber = NumberFormat.getNumberInstance(locale).format(amount)
        if (!currencyCode.isNullOrBlank()) {
            if (currencyCode.length == 1) {
                "${currencyCode}$formattedNumber"
            } else {
                "$currencyCode $formattedNumber"
            }
        } else {
            formattedNumber
        }
    }
}
