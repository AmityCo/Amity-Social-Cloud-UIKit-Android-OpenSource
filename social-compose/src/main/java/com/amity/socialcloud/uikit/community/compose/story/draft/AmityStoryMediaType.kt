package com.amity.socialcloud.uikit.community.compose.story.draft

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AmityStoryMediaType : Parcelable {
    data class Image(val uri: Uri) : AmityStoryMediaType()
    data class Video(val uri: Uri) : AmityStoryMediaType()
}