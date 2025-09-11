package com.amity.socialcloud.uikit.community.compose.post.composer.poll.model

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState

data class ImagePollUiState(
    val answer: TextFieldValue = TextFieldValue(""),
    val imageUri: Uri? = null,
    val uploadState: AmityFileUploadState = AmityFileUploadState.PENDING,
    val uploadProgress: Int = 0,
    val uploadError: Throwable? = null,
    val image: AmityImage? = null,
    val altText: AltTextMedia? = null,
)