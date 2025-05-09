package com.amity.socialcloud.uikit.community.compose.post.model

import android.net.Uri
import android.os.Parcelable
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import kotlinx.parcelize.Parcelize


@Parcelize
data class AmityPostMedia(
    var id: String?,
    var uploadId: String?,
    val url: Uri,
    var uploadState: AmityFileUploadState = AmityFileUploadState.PENDING,
    var currentProgress: Int = 0,
    var type: Type = Type.IMAGE,
    var media: Media? = null,
) : Parcelable {

    constructor(uploadId: String, url: Uri, type: Type)
            : this(null, uploadId, url, AmityFileUploadState.PENDING, 0, type)

    enum class Type(val key: String) {
        IMAGE("image"),
        VIDEO("video");
    }

    @Parcelize
    sealed class Media : Parcelable {
        data class Image(val image: AmityImage) : Media()
    }
}