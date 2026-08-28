package com.cryptocash.android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balances")
data class BalanceEntity(
    @PrimaryKey val currency: String,
    val cryptoAmount: Double,
    val fiatUsd: Double,
    val lastUpdated: Long
)
