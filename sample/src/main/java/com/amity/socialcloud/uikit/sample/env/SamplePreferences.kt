package com.amity.socialcloud.uikit.sample.env

import android.content.Context
import com.amity.socialcloud.uikit.sample.AmitySampleApp
import com.f2prateek.rx.preferences2.RxSharedPreferences

object SamplePreferences {

    private const val API_KEY_KEY = "API_KEY_KEY"
    private const val HTTP_URL_KEY = "HTTP_URL_KEY"
    private const val SOCKET_URL_KEY = "SOCKET_URL_KEY"
    private const val MQTT_BROKER_KEY = "MQTT_BROKER_KEY"
    private const val UPLOAD_URL_KEY = "UPLOAD_URL_KEY"
    private const val MY_USER_ID_KEY = "MY_USER_ID_KEY"

    private val preferences: RxSharedPreferences by lazy {
        RxSharedPreferences.create(
            AmitySampleApp.APP.getSharedPreferences(
                "uikit_sample_preferences",
                Context.MODE_PRIVATE
            )
        )
    }

    fun getApiKey() = preferences.getString(API_KEY_KEY, SampleAPIKey.get())

    fun getHttpUrl() = preferences.getString(HTTP_URL_KEY, SampleUrl.get())

    fun getSocketUrl() = preferences.getString(SOCKET_URL_KEY, SampleUrl.get())

    fun getMqttBroker() = preferences.getString(MQTT_BROKER_KEY, SampleBroker.get())
    
    fun getUploadUrl() = preferences.getString(UPLOAD_URL_KEY, SampleUrl.get())

    fun getMyUserId() = preferences.getString(MY_USER_ID_KEY, "victimAndroid")

}