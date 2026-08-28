package com.cryptocash.android.payment

import androidx.annotation.DrawableRes
import com.cryptocash.android.R

enum class PaymentMethodType { BANK_ACCOUNT, DEBIT_CARD, CREDIT_CARD }

data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val label: String,          // e.g. "Chase ••••4321"
    val subLabel: String,       // e.g. "Checking" or "Visa"
    val isDefault: Boolean = false,
    @DrawableRes val iconRes: Int = R.drawable.ic_bank
)
