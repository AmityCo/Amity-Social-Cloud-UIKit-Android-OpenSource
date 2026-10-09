package com.amity.socialcloud.uikit.common.infra.networklog

import com.amity.socialcloud.sdk.model.core.network.AmityCapturedBody
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivity
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivityType
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkRequest
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkResponse
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicLong

/**
 * Captures UIKit-originated HTTP traffic that the SDK never sees.
 *
 * Bodies are deliberately NOT captured here: this interceptor serves image and media-segment
 * traffic, whose payloads are binary and large, and whose diagnostic value is entirely in the
 * URL, status and timing.
 */
class AmityNetworkCaptureInterceptor(
    private val type: AmityNetworkActivityType,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!AmityNetworkCaptureBridge.isActive) return chain.proceed(chain.request())

        val startedAt = System.currentTimeMillis()
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            AmityNetworkCaptureBridge.record(
                AmityNetworkActivity(
                    id = nextId(),
                    type = type,
                    startedAt = startedAt,
                    endedAt = System.currentTimeMillis(),
                    request = AmityNetworkRequest(
                        method = request.method,
                        url = request.url.toString(),
                        headers = request.headers.toMultimap(),
                    ),
                    response = AmityNetworkResponse(
                        statusCode = response.code,
                        message = response.message.ifBlank { null },
                        headers = response.headers.toMultimap(),
                        body = bodySizeOnly(response),
                        contentLength = response.body?.contentLength()?.takeIf { it >= 0 },
                    ),
                )
            )
            return response
        } catch (t: Throwable) {
            AmityNetworkCaptureBridge.record(
                AmityNetworkActivity(
                    id = nextId(),
                    type = type,
                    startedAt = startedAt,
                    endedAt = System.currentTimeMillis(),
                    request = AmityNetworkRequest(
                        method = request.method,
                        url = request.url.toString(),
                        headers = request.headers.toMultimap(),
                    ),
                    error = t.toString(),
                )
            )
            throw t
        }
    }

    /** Records the declared size without reading the body, so the payload is untouched. */
    private fun bodySizeOnly(response: Response): AmityCapturedBody? {
        val length = response.body?.contentLength()?.takeIf { it > 0 } ?: return null
        return AmityCapturedBody(
            bytes = ByteArray(0),
            decodedLength = length,
            wireLength = length,
            contentEncoding = response.header("Content-Encoding"),
            truncated = true,
            contentType = response.body?.contentType()?.toString(),
        )
    }

    private companion object {
        val sequence = AtomicLong(0)
        fun nextId(): String = "uikit-${sequence.incrementAndGet()}"
    }
}
