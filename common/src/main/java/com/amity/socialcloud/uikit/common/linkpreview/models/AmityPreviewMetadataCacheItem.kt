package com.amity.socialcloud.uikit.common.linkpreview.models

import org.joda.time.DateTime


data class AmityPreviewMetadataCacheItem(
    val url: String = "",
    val domain: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val timestamp: DateTime
)