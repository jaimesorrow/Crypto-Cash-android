package com.cryptocash.android.data.repository

import kotlinx.coroutines.delay

data class BalanceResult(
    val cryptoFormatted: String,
    val fiatUsd: Double,
    val kycRequired: Boolean
)

class WalletRepository {

    /**
     * Fetches the user's balance with automatic retry and exponential back-off.
     * Replace the stub with a real Retrofit API call.
     */
    suspend fun getBalance(): BalanceResult = retryWithBackoff {
        delay(800) // simulate network latency
        BalanceResult(
            cryptoFormatted = "0.04521 BTC",
            fiatUsd = 2_940.75,
            kycRequired = false
        )
    }

    private suspend fun <T> retryWithBackoff(maxRetries: Int = 3, block: suspend () -> T): T {
        var attempt = 0
        while (true) {
            try {
                return block()
            } catch (e: Exception) {
                attempt++
                if (attempt >= maxRetries) throw e
                delay(1_000L * attempt)
            }
        }
    }
}
