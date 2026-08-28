package com.cryptocash.android.util

import android.content.Context
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {

    fun formatFiat(context: Context, amount: Double, currencyCode: String = "USD"): String {
        val locale = context.resources.configuration.locales[0]
        return try {
            NumberFormat.getCurrencyInstance(locale).also {
                it.currency = Currency.getInstance(currencyCode)
            }.format(amount)
        } catch (e: Exception) {
            NumberFormat.getCurrencyInstance(Locale.US).format(amount)
        }
    }

    fun formatCrypto(amount: Double, symbol: String, decimals: Int = 8): String {
        val formatted = "%.${decimals}f".format(amount).trimEnd('0').trimEnd('.')
        return "$formatted $symbol"
    }

    fun formatPercent(value: Double): String {
        val sign = if (value >= 0) "+" else ""
        return "$sign${"%.2f".format(value)}%"
    }
}
