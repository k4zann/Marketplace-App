package com.example.marketplace_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.marketplace_app.utils.notification.ProductNotification
import com.example.marketplace_app.utils.notification.ProductNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BroadcastReceiver: BroadcastReceiver() {

    @Inject
    lateinit var productNotificationManager: ProductNotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "TEST_ACTION") {
            productNotificationManager.showNotification(
                ProductNotification(
                    title = "Update Cache",
                    text = "cache has been updated successfully",
                    channelId = "update cache",
                    channelName = R.string.app_name,
                    icon = R.drawable.ic_notification,
                    channelDescription = R.string.app_name
                )
            )
        }
    }
}