package com.amity.socialcloud.uikit.common.asset

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.placeholder
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext
import com.ekoapp.ekosdk.internal.util.AppContext
import java.io.File


@Composable
fun ImageFromAsset(
    url: String,
    placeholder: Int? = null,
    fallbackResId: Int? = null,
    scale: ContentScale = ContentScale.Fit,
    modifier: Modifier
) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(

        model = ImageRequest.Builder(context)
            .apply {
                val privateDir = File(AmityAppContext.getContext().filesDir, "amityDir")
                val fileName = url.hashCode().toString() + ".jpg"
                val file = File(privateDir, fileName)
                if(file.exists()) {
                    data(Uri.fromFile(file))
                } else {
                    data(url)
                }
            }
            .apply {
                fallbackResId?.let {
                    error(it)
                }
            }
            .apply {
                placeholder?.let {
                    placeholder(it)
                }
            }
            .build()
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale =scale
    )
}
