package com.amity.socialcloud.uikit.sample

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.sdk.video.AmityStreamBroadcasterClient
import com.amity.socialcloud.sdk.video.AmityStreamPlayerClient
import com.amity.socialcloud.uikit.AmityUIKitClient
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingSettings
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingTarget
import com.amity.socialcloud.uikit.sample.env.SamplePreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmitySampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        APP = this

        AmityCoreClient.setup(
            SamplePreferences.getApiKey().get(),
            SamplePreferences.getHttpUrl().get(),
            SamplePreferences.getSocketUrl().get()
        )

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
                Toast.makeText(this, "This user is global banned", Toast.LENGTH_LONG).show() }
            .subscribe()
            
        AmityStreamBroadcasterClient.setup(AmityCoreClient.getConfiguration())
        AmityStreamPlayerClient.setup(AmityCoreClient.getConfiguration())

    }

    companion object {
        lateinit var APP: AmitySampleApp
    }
}