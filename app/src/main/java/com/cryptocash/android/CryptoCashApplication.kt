package com.cryptocash.android

import android.app.Application
import com.cryptocash.android.notifications.NotificationHelper

class CryptoCashApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannels(this)
    }
}
