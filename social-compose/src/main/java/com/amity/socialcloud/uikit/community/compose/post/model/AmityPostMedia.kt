package com.amity.socialcloud.uikit.community.compose.post.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AmityPostMedia(
    var id: String?,
    var uploadId: String?,
    val url: Uri,
    var uploadState: AmityFileUploadState = AmityFileUploadState.PENDING,
    var currentProgress: Int = 0,
    var type: Type = Type.IMAGE
) : Parcelable {

    constructor(uploadId: String, url: Uri, type: Type)
            : this(null, uploadId, url, AmityFileUploadState.PENDING, 0, type)

    enum class Type(val key: String) {
        IMAGE("image"),
        VIDEO("video");
    }
}