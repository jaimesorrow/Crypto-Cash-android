package com.cryptocash.android.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cryptocash.android.MainActivity
import com.cryptocash.android.R

object NotificationHelper {

    const val CHANNEL_TRANSACTIONS = "channel_transactions"
    const val CHANNEL_PRICE_ALERTS = "channel_price_alerts"
    const val CHANNEL_SECURITY = "channel_security"

    fun createNotificationChannels(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannels(
            listOf(
                NotificationChannel(CHANNEL_TRANSACTIONS, context.getString(R.string.channel_transactions_name), NotificationManager.IMPORTANCE_HIGH).apply {
                    description = context.getString(R.string.channel_transactions_desc)
                    enableVibration(true)
                },
                NotificationChannel(CHANNEL_PRICE_ALERTS, context.getString(R.string.channel_price_alerts_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = context.getString(R.string.channel_price_alerts_desc)
                },
                NotificationChannel(CHANNEL_SECURITY, context.getString(R.string.channel_security_name), NotificationManager.IMPORTANCE_HIGH).apply {
                    description = context.getString(R.string.channel_security_desc)
                }
            )
        )
    }

    fun showTransactionNotification(context: Context, title: String, message: String, id: Int = 1001) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pending = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        NotificationManagerCompat.from(context).notify(id,
            NotificationCompat.Builder(context, CHANNEL_TRANSACTIONS)
                .setSmallIcon(R.drawable.ic_send)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pending)
                .setAutoCancel(true)
                .build()
        )
    }

    fun showPriceAlertNotification(context: Context, title: String, message: String, id: Int = 1002) {
        NotificationManagerCompat.from(context).notify(id,
            NotificationCompat.Builder(context, CHANNEL_PRICE_ALERTS)
                .setSmallIcon(R.drawable.ic_wallet)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
        )
    }
}
