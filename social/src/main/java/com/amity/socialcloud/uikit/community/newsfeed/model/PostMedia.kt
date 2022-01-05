package com.amity.socialcloud.uikit.community.newsfeed.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostMedia(
    var id: String?,
    var uploadId: String?,
    val url: Uri,
    var uploadState: FileUploadState = FileUploadState.PENDING,
    var currentProgress: Int = 0,
    var type : Type = Type.IMAGE
) : Parcelable {

    constructor(uploadId: String, url: Uri, type: Type)
            : this(null,uploadId, url, FileUploadState.PENDING, 0, type )

    enum class Type(val key: String) {
        IMAGE("image"),
        VIDEO("video");
    }
}