package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtagMetadataGetter
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.product.AmityProductStatus
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.elements.HashtagMetadataGetter
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.google.gson.Gson
import com.google.gson.JsonObject

@Composable
fun AmityPostContentElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    style: AmityPostContentComponentStyle,
    boldedText: String? = null,
    onClick: () -> Unit,
    onMentionedUserClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
) {
    val mentionGetter = AmityMentionMetadataGetter(post.getMetadata() ?: JsonObject())
    val hashtagGetter = AmityHashtagMetadataGetter(post.getMetadata() ?: JsonObject())
    val productTags = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
        val tags = post.getProductTags().filterIsInstance<AmityProductTag.Text>()
        Log.d("AmityPostContentElement", "Post ${post.getPostId()} has ${tags.size} product tags: ${tags.map { "index=${it.index}, length=${it.length}, text=${it.text}" }}")
        tags
    }
    // Get products list from post to lookup product by id
    val products = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
        post.getProducts().orEmpty()
    }
    val text = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
        (post.getData() as? AmityPost.Data.TEXT)?.getText() ?: ""
    }

    val title = remember(post.getPostId(), post.getEditedAt(), post.getUpdatedAt()) {
        (post.getData() as? AmityPost.Data.TEXT)?.getTitle() ?: ""
    }

    // State to control the product webview bottom sheet
    var showProductWebViewBottomSheet by remember { mutableStateOf<AmityProduct?>(null) }

    if (text.isEmpty() && title.isEmpty()) return

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Display title if available
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = AmityTheme.typography.titleBold.copy(
                    fontSize = 17.sp,
                    color = AmityTheme.colors.base,
                    textAlign = TextAlign.Start
                )
            )

            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Display body text if available
        if (text.isNotEmpty()) {
            AmityExpandableText(
                modifier = modifier,
                text = text,
                mentionGetter = mentionGetter,
                mentionees = post.getMentionees(),
                hashtagGetter = hashtagGetter,
                productTags = productTags,
                style = AmityTheme.typography.body,
                intialExpand = style == AmityPostContentComponentStyle.DETAIL,
                boldWhenMatches = boldedText?.let { listOf(it) } ?: emptyList(),
                onClick = onClick,
                onMentionedUserClick = onMentionedUserClick,
                onHashtagClick = onHashtagClick,
                onMentionedProductClick = { productTag ->
                    Log.d("AmityPostContentElement", "Product tag clicked: productId=${productTag.productId}, product=${productTag.product}")
                    Log.d("AmityPostContentElement", "Products list: ${products.map { it.getProductId() }}")
                    // Try to get product from productTag first, then fallback to lookup from products list
                    val product = productTag.product
                        ?: products.find { it.getProductId() == productTag.productId }
                    Log.d("AmityPostContentElement", "Found product: $product, status=${product?.getStatus()}")
                    
                    // Only show bottom sheet if product is not archived
                    product?.let {
                        if (it.getStatus() != AmityProductStatus.ARCHIVED) {
                            showProductWebViewBottomSheet = it
                        } else {
                            Log.d("AmityPostContentElement", "Product is archived, not opening bottom sheet")
                        }
                    }
                },
            )
        }
    }

    // Show product webview bottom sheet when a product is clicked
    showProductWebViewBottomSheet?.let { product ->
        AmityProductWebViewBottomSheet(
            product = product,
            onDismiss = {
                showProductWebViewBottomSheet = null
            },
        )
    }
}