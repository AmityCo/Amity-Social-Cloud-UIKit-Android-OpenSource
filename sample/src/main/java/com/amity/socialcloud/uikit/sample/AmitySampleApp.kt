package com.amity.socialcloud.uikit.sample

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.video.AmityStreamBroadcasterClient
import com.amity.socialcloud.sdk.video.AmityStreamPlayerClient
import com.amity.socialcloud.uikit.AmityUIKitClient
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingSettings
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingTarget
import com.amity.socialcloud.uikit.sample.env.SamplePreferences
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmitySampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        APP = this

        AmityCoreClient.setup(
            SamplePreferences.getApiKey().get(),
            AmityEndpoint.CUSTOM(
                SamplePreferences.getHttpUrl().get(),
                SamplePreferences.getSocketUrl().get(),
                SamplePreferences.getMqttBroker().get()
            )
        ).subscribe()

        //TODO This allow setting share external for example
        val settings = AmityPostSharingSettings()
        settings.myFeedPostSharingTarget = enumValues<AmityPostSharingTarget>().toList()
        AmityUIKitClient.socialUISettings.postSharingSettings = settings

        AmityUIKitClient.socialUISettings.postShareClickListener = object :
            AmityPostShareClickListener {
            override fun shareToExternal(context: Context, post: AmityPost) {
                val fakeURL = "https://www.amity.co/post?id=" + post.getPostId()
                context.shareLinkToExternalApp(fakeURL)
            }
        }

        AmityCoreClient.getGlobalBanEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Toast.makeText(this, "This user is global banned", Toast.LENGTH_LONG).show()
            }
            .subscribe()

        AmityStreamBroadcasterClient.setup(AmityCoreClient.getConfiguration())
        AmityStreamPlayerClient.setup(AmityCoreClient.getConfiguration())

    }

    companion object {
        lateinit var APP: AmitySampleApp
    }
}