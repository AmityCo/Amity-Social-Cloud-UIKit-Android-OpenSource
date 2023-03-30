package com.amity.socialcloud.uikit.sample

import android.util.Log
import com.amity.socialcloud.sdk.push.AmityFcm
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.util.concurrent.ConcurrentHashMap

class AmitySampleFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("fcm_message", "messageType -> " + remoteMessage.messageType)
        Log.e("fcm_message", "priority -> " + remoteMessage.priority)
        val notification: MutableMap<String, String> = ConcurrentHashMap()
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title ?: ""
            val body = remoteMessage.notification!!.body ?: ""
            val data = Gson().toJson(remoteMessage.data)

            notification["title"] = title
            notification["body"] = body

            AmityNotificationUtil.showNotification(
                this,
                title,
                body,
                data
            )
        }

        Log.e("fcm_message", "notification -> " + Gson().toJson(notification))
        Log.e("fcm_message", "data -> " + Gson().toJson(remoteMessage.data))
    }

    override fun onNewToken(token: String) {
        Log.e("fcm_new_token", token)
        AmityFcm.create()
            .setup(token)
            .doOnError {
                Log.e("fcm_new_token", "onNewToken: $it")
            }
            .subscribe()
    }
}