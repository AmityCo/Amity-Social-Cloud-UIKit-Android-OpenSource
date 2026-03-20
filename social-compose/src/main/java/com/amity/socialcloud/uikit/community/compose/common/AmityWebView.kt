package com.amity.socialcloud.uikit.community.compose.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.roundToInt

private class ScrollableWebView(context: Context) : WebView(context) {
    val verticalScrollRange: Int get() = computeVerticalScrollRange() - height
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AmityWebView(
    url: String,
    modifier: Modifier = Modifier,
    onCreated: (WebView) -> Unit = {},
    onError: (() -> Unit)? = null,
    onLoadingChanged: ((Boolean) -> Unit)? = null,
) {
    val context = LocalContext.current
    var hasLoaded by remember { mutableStateOf(false) }

    val webView: ScrollableWebView = remember(context) {
        ScrollableWebView(context).apply {
            setBackgroundColor(android.graphics.Color.WHITE)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    onLoadingChanged?.invoke(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onLoadingChanged?.invoke(false)
                }

                @SuppressLint("WebViewClientOnReceivedSslError")
                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    // Proceed past SSL errors (e.g. ERR_CERT_AUTHORITY_INVALID / net_error -202)
                    // so the page still loads inside the in-app browser instead of triggering
                    // the error state. The user can always choose to open in the native browser.
                    handler?.proceed()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    if (request?.isForMainFrame == true) {
                        onLoadingChanged?.invoke(false)
                        onError?.invoke()
                    }
                }
            }

            onCreated(this)
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    val scrollableState = rememberScrollableState { delta ->
        val scrollY = webView.scrollY
        val consume = (scrollY - delta).coerceIn(0f, webView.verticalScrollRange.toFloat())
        webView.scrollTo(0, consume.roundToInt())
        (scrollY - webView.scrollY).toFloat()
    }

    AndroidView(
        factory = { webView },
        modifier = modifier
            .scrollable(scrollableState, Orientation.Vertical)
    ) { view ->
        if (!hasLoaded) {
            hasLoaded = true
            view.loadUrl(url)
        }
    }
}
