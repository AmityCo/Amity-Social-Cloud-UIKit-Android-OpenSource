package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.behavior.AmityGlobalBehavior
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductWebViewBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AmityProductTagListComponent
import com.amity.socialcloud.uikit.community.compose.post.composer.components.RenderModeEnum

@Composable
fun AmityPostMediaElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    clipClick: (childPost: AmityPost) -> Unit = {},
) {
    val postChildren = remember(
        post.getPostId(),
        post.getEditedAt(),
        post.getUpdatedAt(),
        post.getChildren().size
    ) {
        post.getChildren()
    }
    if (postChildren.isEmpty()) return

    when (postChildren.first().getData()) {
        is AmityPost.Data.IMAGE,
        is AmityPost.Data.VIDEO,
        is AmityPost.Data.CLIP,
            -> {
        }

        else -> return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {

        when (postChildren.first().getData()) {
            is AmityPost.Data.IMAGE -> AmityChildPostMediaElement(
                post = post,
                isVideoPost = false
            )

            is AmityPost.Data.VIDEO -> AmityChildPostMediaElement(
                post = post,
                isVideoPost = true
            )

            is AmityPost.Data.CLIP -> AmityChildPostMediaElement(
                modifier = modifier.aspectRatio(9 / 16f),
                post = post,
                clipClick = {
                    clipClick(it)
                },
                isVideoPost = true
            )

            else -> {}
        }
    }
}

@Composable
fun AmityChildPostMediaElement(
    modifier: Modifier = Modifier,
    post: AmityPost,
    clipClick: (AmityPost) -> Unit = {},
    isVideoPost: Boolean,
) {
    val childPosts = remember(post.getPostId(), post.getUpdatedAt(), isVideoPost) {
        getChildPostData(post)
    }

    val postChildrenList = remember(post.getPostId(), post.getUpdatedAt()) {
        post.getChildren()
    }

    val images by remember {
        derivedStateOf {
            childPosts.mapNotNull { data ->
                when (data) {
                    is AmityPost.Data.IMAGE -> data.getImage()
                    is AmityPost.Data.VIDEO -> data.getThumbnailImage()
                    is AmityPost.Data.CLIP -> data.getThumbnailImage()
                    else -> null
                }
            }
        }
    }
    val showMediaDialog = remember { mutableStateOf(false) }
    val selectedFileId = remember { mutableStateOf("") }

    // State for product tag bottom sheet
    var showProductTagSheet by remember { mutableStateOf(false) }
    var selectedProducts by remember { mutableStateOf<List<AmityProduct>>(emptyList()) }
    var selectedProduct by remember { mutableStateOf<AmityProduct?>(null) }
    val disposables = remember { io.reactivex.rxjava3.disposables.CompositeDisposable() }
    val context = LocalContext.current
    val behavior = AmitySocialBehaviorHelper.globalBehavior

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            disposables.clear()
        }
    }

    if (showMediaDialog.value && selectedFileId.value.isNotEmptyOrBlank()) {
        if (isVideoPost) {
            // Use AmityVideoPlayerPage for video posts
            AmityVideoPlayerPage(
                childPosts = post.getChildren(),
                selectedFileId = selectedFileId.value,
                onDismiss = { showMediaDialog.value = false }
            )
        } else {
            // Use AmityPostMediaPreviewDialog for image posts
            AmityPostMediaPreviewDialog(
                childPosts = post.getChildren(),
                isVideoPost = isVideoPost,
                isPostCreator = post.getCreatorId() == AmityCoreClient.getUserId(),
                selectedFileId = selectedFileId.value,
                onDismiss = { showMediaDialog.value = false }
            )
        }
    }

    // Product tag bottom sheet
    if (showProductTagSheet && selectedProducts.isNotEmpty()) {
        AmityProductTagListComponent(
            productTags = selectedProducts,
            renderMode = if (isVideoPost) RenderModeEnum.VIDEO else RenderModeEnum.IMAGE,
            onDismiss = { showProductTagSheet = false },
            onProductClick = { product ->
                val handled = behavior.onPostProductTagClick(
                    AmityGlobalBehavior.Context(
                        pageContext = context,
                        product = product,
                    )
                )
                if (!handled) {
                    selectedProduct = product
                }
            },
        )
    }

    selectedProduct?.let { p ->
        AmityProductWebViewBottomSheet(
            product = p,
            onDismiss = {
                selectedProduct = null
            }
        )
    }

    // Callback for product tag badge click
    val onProductTagClick: (AmityPost) -> Unit = { childPost ->
        val mediaTags = childPost.getProductTags().filterIsInstance<AmityProductTag.Media>()
        val productIds = mediaTags.map { it.productId }

        if (productIds.isNotEmpty()) {
            // Query products from productIds
            val disposable = io.reactivex.rxjava3.core.Observable.fromIterable(productIds)
                .flatMapSingle { productId ->
                    AmityCoreClient.newProductRepository()
                        .getProduct(productId)
                        .firstOrError()
                }
                .toList()
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                    { products ->
                        selectedProducts = products
                        showProductTagSheet = true
                    },
                    { error ->
                        android.util.Log.e("ProductTagBadge", "Error fetching products", error)
                    }
                )
            disposables.add(disposable)
        }
    }

    when (postChildrenList.size) {
        0 -> {}
        1 -> AmityPostMediaImageChildrenOne(
            modifier = modifier,
            isVideoPost = isVideoPost,
            postChild = postChildrenList.first(),
            onProductTagClick = onProductTagClick,
        ) {
            if (it.getData() is AmityPost.Data.CLIP) {
                clipClick(it)
            } else {
                selectedFileId.value = it.getPostId()
                showMediaDialog.value = true
            }
        }

        2 -> AmityPostMediaImageChildrenTwo(
            modifier = modifier,
            postChildren = postChildrenList,
            images = images,
            isVideoPost = isVideoPost,
            onProductTagClick = onProductTagClick,
        ) {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }

        3 -> AmityPostMediaImageChildrenThree(
            modifier = modifier,
            postChildren = postChildrenList,
            images = images,
            isVideoPost = isVideoPost,
            onProductTagClick = onProductTagClick,
        ) {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }

        else -> AmityPostMediaImageChildrenFour(
            modifier = modifier,
            postChildren = postChildrenList,
            images = images,
            isVideoPost = isVideoPost,
            onProductTagClick = onProductTagClick,
        ) {
            selectedFileId.value = it.getPostId()
            showMediaDialog.value = true
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenOne(
    modifier: Modifier = Modifier,
    postChild: AmityPost,
    isVideoPost: Boolean,
    onProductTagClick: (AmityPost) -> Unit = {},
    onClick: (AmityPost) -> Unit,
) {
    val productTagCount = remember(postChild.getPostId(), postChild.getUpdatedAt()) {
        getProductTagCount(postChild)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        AmityPostImageView(
            modifier = Modifier
                .semantics {
                    role = Role.Image
                    contentDescription =
                        if (isVideoPost) "Video 1 of 1" else "Photo 1 of 1: ${getAltText(postChild)}"
                },
            post = postChild,
            onClick = { onClick(postChild) }
        )
        if (isVideoPost) {
            AmityPostMediaPlayButton(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        AmityProductTagBadge(
            count = productTagCount,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 12.dp),
            onClick = { onProductTagClick(postChild) }
        )
    }
}

@Composable
fun AmityPostMediaImageChildrenTwo(
    modifier: Modifier = Modifier,
    postChildren: List<AmityPost>,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onProductTagClick: (AmityPost) -> Unit = {},
    onClick: (AmityPost) -> Unit,
) {
    val productTagCounts = remember(postChildren.joinToString(separator = "|") { "${it.getPostId()}@${it.getUpdatedAt()}" }) {
        postChildren.map { getProductTagCount(it) }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        ) {
            AmityPostImageView(
                modifier = Modifier
                    .semantics {
                        role = Role.Image
                        contentDescription =
                            if (isVideoPost) "Video 1 of 2" else "Photo 1 of 2: ${
                                getAltText(postChildren[0])
                            }"
                    },
                post = postChildren[0],
                onClick = { onClick(postChildren[0]) }
            )
            if (isVideoPost) {
                AmityPostMediaPlayButton(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            AmityProductTagBadge(
                count = productTagCounts.getOrElse(0) { 0 },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp),
                onClick = { onProductTagClick(postChildren[0]) }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        ) {
            AmityPostImageView(
                modifier = Modifier
                    .semantics {
                        role = Role.Image
                        contentDescription =
                            if (isVideoPost) "Video 2 of 2" else "Photo 2 of 2: ${
                                getAltText(postChildren[1])
                            }"
                    },
                post = postChildren[1],
                onClick = { onClick(postChildren[1]) }
            )
            if (isVideoPost) {
                AmityPostMediaPlayButton(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            AmityProductTagBadge(
                count = productTagCounts.getOrElse(1) { 0 },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp),
                onClick = { onProductTagClick(postChildren[1]) }
            )
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenThree(
    modifier: Modifier = Modifier,
    postChildren: List<AmityPost>,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onProductTagClick: (AmityPost) -> Unit = {},
    onClick: (AmityPost) -> Unit,
) {
    val productTagCounts = remember(postChildren.joinToString(separator = "|") { "${it.getPostId()}@${it.getUpdatedAt()}" }) {
        postChildren.map { getProductTagCount(it) }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            AmityPostImageView(
                modifier = Modifier
                    .semantics {
                        role = Role.Image
                        contentDescription = if (isVideoPost) "Video 1 of 3" else "Photo 1 of 3: ${
                            getAltText(postChildren[0])
                        }"
                    },
                post = postChildren[0],
                onClick = { onClick(postChildren[0]) }
            )
            if (isVideoPost) {
                AmityPostMediaPlayButton(
                    modifier = modifier.align(Alignment.Center)
                )
            }
            AmityProductTagBadge(
                count = productTagCounts.getOrElse(0) { 0 },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp),
                onClick = { onProductTagClick(postChildren[0]) }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                AmityPostImageView(
                    modifier = Modifier
                        .semantics {
                            role = Role.Image
                            contentDescription =
                                if (isVideoPost) "Video 2 of 3" else "Photo 2 of 3: ${
                                    getAltText(postChildren[1])
                                }"
                        },
                    post = postChildren[1],
                    onClick = { onClick(postChildren[1]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
                AmityProductTagBadge(
                    count = productTagCounts.getOrElse(1) { 0 },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp),
                    onClick = { onProductTagClick(postChildren[1]) }
                )
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                AmityPostImageView(
                    modifier = Modifier
                        .semantics {
                            role = Role.Image
                            contentDescription =
                                if (isVideoPost) "Video 3 of 3" else "Photo 3 of 3: ${
                                    getAltText(postChildren[2])
                                }"
                        },
                    post = postChildren[2],
                    onClick = { onClick(postChildren[2]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
                AmityProductTagBadge(
                    count = productTagCounts.getOrElse(2) { 0 },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp),
                    onClick = { onProductTagClick(postChildren[2]) }
                )
            }
        }
    }
}

@Composable
fun AmityPostMediaImageChildrenFour(
    modifier: Modifier = Modifier,
    postChildren: List<AmityPost>,
    images: List<AmityImage>,
    isVideoPost: Boolean,
    onProductTagClick: (AmityPost) -> Unit = {},
    onClick: (AmityPost) -> Unit,
) {
    val productTagCounts = remember(postChildren.joinToString(separator = "|") { "${it.getPostId()}@${it.getUpdatedAt()}" }) {
        postChildren.map { getProductTagCount(it) }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                AmityPostImageView(
                    modifier = Modifier
                        .semantics {
                            role = Role.Image
                            contentDescription =
                                if (isVideoPost) "Video 1 of ${postChildren.size}" else "Photo 1 of ${postChildren.size}: ${
                                    getAltText(postChildren[0])
                                }"
                        },
                    post = postChildren[0],
                    onClick = { onClick(postChildren[0]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
                AmityProductTagBadge(
                    count = productTagCounts.getOrElse(0) { 0 },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp),
                    onClick = { onProductTagClick(postChildren[0]) }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    modifier = Modifier
                        .semantics {
                            role = Role.Image
                            contentDescription =
                                if (isVideoPost) "Video 2 of ${postChildren.size}" else "Photo 2 of ${postChildren.size}: ${
                                    getAltText(postChildren[1])
                                }"
                        },
                    post = postChildren[1],
                    onClick = { onClick(postChildren[1]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
                AmityProductTagBadge(
                    count = productTagCounts.getOrElse(1) { 0 },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp),
                    onClick = { onProductTagClick(postChildren[1]) }
                )
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    modifier = Modifier
                        .semantics {
                            role = Role.Image
                            contentDescription =
                                if (isVideoPost) "Video 3 of ${postChildren.size}" else "Photo 3 of ${postChildren.size}: ${
                                    getAltText(postChildren[2])
                                }"
                        },
                    post = postChildren[2],
                    onClick = { onClick(postChildren[2]) }
                )
                if (isVideoPost) {
                    AmityPostMediaPlayButton(
                        modifier = modifier.align(Alignment.Center)
                    )
                }
                AmityProductTagBadge(
                    count = productTagCounts.getOrElse(2) { 0 },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp),
                    onClick = { onProductTagClick(postChildren[2]) }
                )
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AmityPostImageView(
                    modifier = Modifier
                        .semantics {
                            role = Role.Image
                            contentDescription =
                                if (isVideoPost) "Activate to view ${postChildren.size - 3} more videos" else "Activate to view ${postChildren.size - 3} more photos"
                        },
                    post = postChildren[3],
                    onClick = { onClick(postChildren[3]) }
                )

                if (postChildren.size > 4) {
                    // Show +X overlay when more than 4 images - no badge shown
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.5f))
                    ) {
                        Text(
                            text = "+${postChildren.size - 3}",
                            style = AmityTheme.typography.titleLegacy.copy(
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    // Exactly 4 images - show play button if video and badge
                    if (isVideoPost) {
                        AmityPostMediaPlayButton(
                            modifier = modifier.align(Alignment.Center)
                        )
                    }
                    AmityProductTagBadge(
                        count = productTagCounts.getOrElse(3) { 0 },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 12.dp),
                        onClick = { onProductTagClick(postChildren[3]) }
                    )
                }
            }
        }
    }
}

fun getChildPostData(post: AmityPost): List<AmityPost.Data> {
    return post.getChildren().mapNotNull {
        when (val data = it.getData()) {
            is AmityPost.Data.IMAGE -> data
            is AmityPost.Data.VIDEO -> data
            else -> null
        }
    }
}

fun getAltText(post: AmityPost): String {
    return (post.getData() as? AmityPost.Data.IMAGE)?.getImage()?.getAltText()
        ?: "No description available"
}

fun getProductTagCount(childPost: AmityPost): Int {
    return childPost.getProductTags()
        .filterIsInstance<AmityProductTag.Media>()
        .size
}

fun getProductsFromTags(childPost: AmityPost): List<AmityProduct> {
    return childPost.getProductTags()
        .filterIsInstance<AmityProductTag.Media>()
        .mapNotNull { it.product }
}

@Composable
fun AmityProductTagBadge(
    count: Int,
    modifier: Modifier = Modifier,
    showWhenEmpty: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    if (count <= 0 && !showWhenEmpty) return

    Row(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .background(
                color = Color(0x80000000),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.amity_ic_product_tag_filled),
            contentDescription = "Product tags",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        if (count > 0) {
            Text(
                text = count.toString(),
                style = AmityTheme.typography.caption.copy(
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun AmityPostMediaPlayButton(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.amity_ic_play_v4),
        contentDescription = null,
        modifier = modifier.size(40.dp)
    )
}