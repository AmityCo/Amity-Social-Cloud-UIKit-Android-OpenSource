package com.amity.socialcloud.uikit.community.compose.ads

import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.amity.socialcloud.uikit.community.compose.R
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun AmityNativeAdItem(
    adUnitId: String = "ca-app-pub-3940256099942544/2247696110" // TEST Native
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(adUnitId) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad -> nativeAd = ad }
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            nativeAd?.destroy()
            nativeAd = null
        }
    }

    val ad = nativeAd ?: return

    AndroidView(
        factory = { ctx ->
            LayoutInflater.from(ctx).inflate(R.layout.amity_native_ad_small, null) as NativeAdView
        },
        update = { view ->
            val headline = view.findViewById<TextView>(R.id.ad_headline)
            val body = view.findViewById<TextView>(R.id.ad_body)
            val cta = view.findViewById<Button>(R.id.ad_cta)

            headline.text = ad.headline ?: ""
            view.headlineView = headline

            body.text = ad.body ?: ""
            view.bodyView = body

            cta.text = ad.callToAction ?: "Open"
            view.callToActionView = cta

            view.setNativeAd(ad)
        }
    )
}

