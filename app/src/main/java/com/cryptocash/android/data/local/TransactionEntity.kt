package com.cryptocash.android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val type: String,
    val cryptoAmount: Double,
    val cryptoCurrency: String,
    val fiatAmount: Double,
    val counterparty: String,
    val timestamp: Long,
    val status: String,
    val isSynced: Boolean = false
)
