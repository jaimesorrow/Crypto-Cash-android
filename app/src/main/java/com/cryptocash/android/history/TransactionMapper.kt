package com.cryptocash.android.history

import android.content.Context
import com.cryptocash.android.R
import com.cryptocash.android.data.local.TransactionEntity
import com.cryptocash.android.util.CurrencyFormatter

private val COIN_NAMES = mapOf(
    "BTC" to "Bitcoin",
    "ETH" to "Ethereum",
    "LTC" to "Litecoin",
    "SOL" to "Solana",
    "USDC" to "USD Coin"
)

/**
 * Maps a Room-persisted [TransactionEntity] to the display-ready [TransactionItem]
 * consumed by [TransactionAdapter]. Keeps all user-facing string formatting here so
 * the DB layer stays purely data (raw amounts/currency codes, not localized text).
 */
fun TransactionEntity.toTransactionItem(context: Context): TransactionItem {
    val coinName = COIN_NAMES[cryptoCurrency] ?: cryptoCurrency
    val isPositive = type == "buy" || type == "receive" || type == "deposit"
    val sign = if (isPositive) "+" else "-"

    val title = when (type) {
        "buy" -> context.getString(R.string.tx_title_bought, coinName)
        "sell" -> context.getString(R.string.tx_title_sold, coinName)
        "send" -> context.getString(R.string.tx_title_sent_to, counterparty)
        "receive" -> context.getString(R.string.tx_title_received, coinName)
        "deposit" -> context.getString(R.string.tx_title_deposited_cash)
        else -> counterparty
    }

    val subtitle = when (type) {
        "send" -> context.getString(R.string.tx_subtitle_network, coinName)
        "receive" -> context.getString(R.string.tx_subtitle_from, counterparty)
        else -> counterparty
    }

    val amountCrypto = if (type == "deposit") {
        sign + CurrencyFormatter.formatFiat(context, fiatAmount)
    } else {
        sign + CurrencyFormatter.formatCrypto(cryptoAmount, cryptoCurrency)
    }
    val amountFiat = CurrencyFormatter.formatFiat(context, fiatAmount)

    val iconRes = when (type) {
        "buy" -> R.drawable.ic_buy
        "sell" -> R.drawable.ic_sell
        "send" -> R.drawable.ic_send
        "receive" -> R.drawable.ic_receive
        "deposit" -> R.drawable.ic_deposit
        else -> R.drawable.ic_send
    }

    return TransactionItem(
        id = id,
        type = type,
        title = title,
        subtitle = subtitle,
        amountCrypto = amountCrypto,
        amountFiat = amountFiat,
        isPositive = isPositive,
        status = status,
        timestamp = timestamp,
        iconRes = iconRes
    )
}
