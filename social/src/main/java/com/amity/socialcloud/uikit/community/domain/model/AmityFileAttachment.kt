package com.amity.socialcloud.uikit.community.domain.model

import android.net.Uri
import android.os.Parcelable
import com.amity.socialcloud.uikit.community.newsfeed.model.FileUploadState
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AmityFileAttachment(
    val id: String?,
    val uploadId: String?,
    val name: String,
    val size: Long,
    val uri: Uri,
    val readableSize: String,
    val mimeType: String,
    var uploadState: FileUploadState = FileUploadState.PENDING,
    var progress: Int = 0
) :
    Parcelable {

    override fun equals(other: Any?): Boolean {
        return (other is AmityFileAttachment)
                && name == other.name
                && size == other.size
                && uri == other.uri
                && mimeType == other.mimeType
                && uploadId == other.uploadId
    }

    override fun hashCode(): Int {
        return Objects.hashCode(super.hashCode(), id, uploadId, name, size, uri,
            readableSize, mimeType, uploadState, progress)
    }
}