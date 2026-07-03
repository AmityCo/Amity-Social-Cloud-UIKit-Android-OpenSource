package com.amity.socialcloud.uikit.community.compose.livestream.create.nopermission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.community.compose.livestream.create.element.AmityMediaAndCameraNoPermissionView
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityNoPermissionPage() {
    val context = LocalContext.current

    DisposableEffectWithLifeCycle(
        onResume = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                context.closePageWithResult(Activity.RESULT_OK)
            }
        },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(amityMediaSurface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(58.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableWithoutRipple {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                },
                painter = painterResource(R.drawable.amity_ic_back),
                tint = AmityTheme.colors.baseInverse,
                contentDescription = "back"
            )
        }

        AmityMediaAndCameraNoPermissionView(
            modifier = Modifier.weight(1f),
            title = amitySocialString("amity_social_permission_allow_photos_title"),
            description = amitySocialString("amity_social_status_permission_photo_thumbnail"),
            onOpenSettingClick = {
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                context.startActivity(intent)
            }
        )
    }

}