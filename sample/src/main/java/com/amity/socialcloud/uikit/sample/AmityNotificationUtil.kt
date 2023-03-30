package com.amity.socialcloud.uikit.sample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import kotlin.random.Random

object AmityNotificationUtil {
    private lateinit var notificationManager: NotificationManager
    private val CHANNEL_ID = "UPSTRA_PUSH_NOTIFICATION_CHANNEL"
    private val NEW_NOTIFICATION_ID
        get() = Random.nextInt()

    fun showNotification(context: Context, tile: String, description: String, data: String) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentTitle(tile)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(data)
            )
            .setAutoCancel(true)

        createNotificationChannel(context)

        notificationManager.notify(NEW_NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.amity_notification_channel)
            val descriptionText = context.getString(R.string.amity_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
    }
}