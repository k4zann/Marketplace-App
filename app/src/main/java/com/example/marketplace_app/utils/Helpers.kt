package com.example.marketplace_app.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.marketplace_app.utils.notification.ProductNotification

fun Context.getPendingIntent(activity: Class<*>): PendingIntent {
    return Intent(this, activity).let { notificationIntent ->
        PendingIntent.getActivity(
            this, 1, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotification(
    productNotification: ProductNotification,
    pendingIntent: PendingIntent?,
) = Notification.Builder(this, productNotification.channelId)
    .setContentTitle(productNotification.title)
    .setContentText(productNotification.text)
    .setSmallIcon(productNotification.icon)
    .setContentIntent(pendingIntent)
    .build()

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannel(
    channelId: String = "channelId",
    channelName: String = "channelName",
) : String {
    val chan = NotificationChannel(
        channelId,
        channelName, NotificationManager.IMPORTANCE_NONE
    )
    chan.lightColor = Color.BLUE
    chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
    val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (service.notificationChannels.all { it.id != channelId }) {
        service.createNotificationChannel(chan)
    }
    return channelId
}