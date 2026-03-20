package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.product.AmityProductStatus
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.common.AmityWebView
import org.joda.time.DateTime

@Composable
fun AmityProductWebViewComponent(
    modifier: Modifier = Modifier,
    product: AmityProduct,
    onDismiss: () -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current
    val productUrl = product.getProductUrl()

    // Extract domain from product URL using android.net.Uri — never throws,
    // handles malformed/empty URLs without a try-catch.
    val domainName = remember(productUrl) {
        productUrl.toUri()
            .host
            ?.removePrefix("www.")
            ?.takeIf { it.isNotBlank() }
            ?: productUrl
    }

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    // Defer WebView creation to the next frame so the header is painted before
    // the heavyweight AndroidView is attached to the composition tree.
    var isWebViewVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withFrameNanos { } // suspend until the first frame is drawn
        isWebViewVisible = true
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Title Section — always visible even on error
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF292B32),
                    modifier = Modifier.size(18.dp)
                )
            }

            // Product Domain and lock icon
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.amity_ic_product_lock),
                        contentDescription = "webview secure"
                    )
                    Text(
                        text = domainName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF292B32),
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            // Open External Browser Button
            IconButton(
                onClick = {
                    uriHandler.openUri(productUrl)
                    onDismiss()
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_open_browser),
                    contentDescription = "Open external browser",
                    tint = Color(0xFF292B32),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // WebView Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            if (isError) {
                // Error state — header stays visible above; show fallback here
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Unable to load page",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF292B32),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The page could not be loaded. You can open it in your browser instead.",
                        fontSize = 14.sp,
                        color = Color(0xFF636878),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            uriHandler.openUri(productUrl)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1054DE)
                        )
                    ) {
                        Text(text = "Open in Browser", color = Color.White)
                    }
                }
            }

            if (!isError && isWebViewVisible) {
                AmityWebView(
                    url = productUrl,
                    modifier = Modifier.fillMaxSize(),
                    onError = {
                        isError = true
                        isLoading = false
                    },
                    onLoadingChanged = { loading ->
                        isLoading = loading
                    }
                )

                // Loading overlay — shown while the page is fetched
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF1054DE),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AmityProductWebViewComponentPreview() {
    AmityProductWebViewComponent(
        product = AmityProduct(
            productId = "prod1",
            productName = "Sample Product 1",
            price = 29.99,
            currency = "$",
            thumbnailUrl = "https://via.placeholder.com/150",
            status = AmityProductStatus.ACTIVE,
            thumbnailMode = null,
            createdBy = "",
            updatedBy = null,
            isDeleted = false,
            createdAt = DateTime.now(),
            updatedAt = DateTime.now(),
            productUrl = ""
        ),
        onDismiss = {}
    )
}