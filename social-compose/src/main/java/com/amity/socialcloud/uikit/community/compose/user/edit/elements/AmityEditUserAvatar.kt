package com.amity.socialcloud.uikit.community.compose.user.edit.elements

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityEditUserAvatar(
    modifier: Modifier = Modifier,
    user: AmityUser? = null,
    onImagePicked: (Uri) -> Unit,
) {
    var avatarUri by remember { mutableStateOf(Uri.EMPTY) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                onImagePicked(it)
                avatarUri = it
            }
        }

    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .clickableWithoutRipple {
                imagePickerLauncher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
    ) {
        if (avatarUri == Uri.EMPTY) {
            AmityUserAvatarView(
                user = user,
                size = 64.dp,
            )
        } else {
            AsyncImage(
                model = avatarUri,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxWidth(),
            )
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
        )

        Icon(
            painter = painterResource(R.drawable.amity_ic_camera),
            contentDescription = null,
            tint = Color.White,
            modifier = modifier.align(Alignment.Center)
        )
    }
}