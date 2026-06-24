package com.cryptocash.android.history

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.cryptocash.android.R

data class TransactionItem(
    val id: String,
    val type: String,           // "buy", "sell", "send", "receive", "deposit"
    val title: String,
    val subtitle: String,
    val amountCrypto: String,
    val amountFiat: String,
    val isPositive: Boolean,
    val status: String,
    val timestamp: Long,
    @DrawableRes val iconRes: Int = R.drawable.ic_send
)
