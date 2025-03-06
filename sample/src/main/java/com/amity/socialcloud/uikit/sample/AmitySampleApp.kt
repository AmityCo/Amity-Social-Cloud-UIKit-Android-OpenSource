package com.amity.socialcloud.uikit.sample

import android.app.Application
import android.content.Context
import android.widget.Toast
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.util.DebugLogger
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.model.core.file.AmityFileAccessType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.AmityUIKitClient
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingSettings
import com.amity.socialcloud.uikit.feed.settings.AmityPostSharingTarget
import com.amity.socialcloud.uikit.sample.env.SamplePreferences
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmitySampleApp : Application()  {

    override fun onCreate() {
        super.onCreate()
        APP = this

        // V4 setup
        AmityUIKit4Manager.setup(
            apiKey = SamplePreferences.getApiKey().get(),
            endpoint =  AmityEndpoint.CUSTOM(
                SamplePreferences.getHttpUrl().get(),
                SamplePreferences.getMqttBroker().get(),
                SamplePreferences.getUploadUrl().get(),
            )
        )

        // V3 Ex. override post sharing event
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

        // OPTIONAL: Set the default file access type for uploaded files
        AmityCoreClient.setUploadedFileAccessType(AmityFileAccessType.PUBLIC)

    }

    companion object {
        lateinit var APP: AmitySampleApp
    }


}