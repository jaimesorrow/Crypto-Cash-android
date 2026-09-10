package com.cryptocash.android.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CryptoCashMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: return
        val body = message.notification?.body ?: message.data["body"] ?: return
        val type = message.data["type"] ?: "transaction"

        when (type) {
            "price_alert" -> NotificationHelper.showPriceAlertNotification(this, title, body)
            else -> NotificationHelper.showTransactionNotification(this, title, body)
        }
    }

    override fun onNewToken(token: String) {
        // TODO: send FCM token to backend
    }
}
