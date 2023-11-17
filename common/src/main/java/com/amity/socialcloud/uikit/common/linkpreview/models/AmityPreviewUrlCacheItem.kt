package com.amity.socialcloud.uikit.common.linkpreview.models

import org.joda.time.DateTime


open class AmityPreviewUrlCacheItem(
    val url: String,
    val editedAt: DateTime?
)

class AmityPreviewNoUrl : AmityPreviewUrlCacheItem("", DateTime.now())
