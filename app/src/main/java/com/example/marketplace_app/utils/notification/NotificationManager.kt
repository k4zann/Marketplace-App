package com.example.marketplace_app.utils.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.example.marketplace_app.ui.fragments.MainActivity
import com.example.marketplace_app.utils.createNotification
import com.example.marketplace_app.utils.getPendingIntent
import java.util.UUID
import javax.inject.Inject

class ProductNotificationManager @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManager
) {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    fun showNotification(productNotification: ProductNotification) {
        val pendingIntent = context.getPendingIntent(MainActivity::class.java)
        buildNotificationChannel(productNotification)
        val notification = context.createNotification(
            productNotification = productNotification,
            pendingIntent = pendingIntent
        )
        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotificationChannel(productNotification: ProductNotification) {
        val chan = NotificationChannel(
            productNotification.channelId,
            context.getString(productNotification.channelName),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        if (notificationManager.notificationChannels.all { it.id != productNotification.channelId }) {
            notificationManager.createNotificationChannel(chan)
        }
    }
}

data class ProductNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val text: String,
    val channelId: String = UUID.randomUUID().toString(),
    @StringRes val channelName: Int,
    @DrawableRes val icon: Int,
    @StringRes val channelDescription: Int
)