package com.amity.socialcloud.uikit.community.compose.post.composer

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.content.AmityContentFeedType
import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.core.file.AmityFileInfo
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.link.AmityLink
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia
import com.amity.socialcloud.uikit.community.compose.post.model.AmityPostMedia.Type
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.core.net.toUri
import com.amity.socialcloud.sdk.helper.core.asAmityImage
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.metadata.AmityPostMetadataCreator
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.core.file.AmityFileType
import com.amity.socialcloud.sdk.model.core.file.AmityRawFile
import com.amity.socialcloud.sdk.model.core.link.AmityLinkPreviewMetadata
import com.amity.socialcloud.sdk.model.core.producttag.AmityAttachmentProductTags
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.sdk.model.core.product.AmityProductStatus
import com.amity.socialcloud.uikit.community.compose.post.composer.components.AltTextMedia
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext
import com.amity.socialcloud.uikit.community.compose.R
import kotlin.apply
import kotlin.math.min
import kotlin.math.max


class AmityPostComposerPageViewModel : AmityMediaAttachmentViewModel() {

    private val MAX_CHAR_LIMIT = 50000
    private val MAX_ATTACHMENTS = 10
    private val MAX_PRODUCT_TAGS_PER_MEDIA = 5

    companion object {
        const val MAX_TOTAL_PRODUCT_TAGS = 20
    }

    private var options: AmityPostComposerOptions? = null

    var community: AmityCommunity? = null

    private val _post by lazy {
        MutableStateFlow<AmityPost?>(null)
    }
    val post get() = _post

    private val mediaMap = java.util.LinkedHashMap<String, AmityPostMedia>()
    private val uploadedMediaMap = LinkedHashMap<String, AmityFileInfo>()
    private val deletedImageIds = mutableListOf<String>()
    private val uploadFailedMediaMap = LinkedHashMap<String, Boolean>()
    private val showAltTextConfigSheet = mutableStateOf(false)
    private val altTextMedia = mutableStateOf<AltTextMedia?>(null)

    // Product tags per media file: key = fileId/uploadId, value = list of products
    private val _mediaProductTags by lazy {
        MutableStateFlow<Map<String, List<AmityProduct>>>(emptyMap())
    }
    val mediaProductTags get() = _mediaProductTags

    // Product tags from text mentions with index for sorting
    private val _textProductTagsWithIndex by lazy {
        MutableStateFlow<List<Pair<Int, AmityProduct>>>(emptyList())
    }

    // Product tags from text mentions
    private val _textProductTags by lazy {
        MutableStateFlow<List<AmityProduct>>(emptyList())
    }
    val textProductTags get() = _textProductTags

    private val _originalMediaProductTagIds =
        MutableStateFlow<Map<String, Set<String>>>(emptyMap())
    val originalMediaProductTagIds: StateFlow<Map<String, Set<String>>> =
        _originalMediaProductTagIds.asStateFlow()

    private val _originalTextProductTagIds =
        MutableStateFlow<List<String>>(emptyList())
    val originalTextProductTagIds: StateFlow<List<String>> =
        _originalTextProductTagIds.asStateFlow()

    // All distinct product tags across all media AND text mentions
    // Order: text tags sorted by index first, then media tags in order of media files
    val allDistinctProductTags: kotlinx.coroutines.flow.Flow<List<AmityProduct>>
        get() = kotlinx.coroutines.flow.combine(
            _mediaProductTags,
            _textProductTagsWithIndex,
            _selectedMediaFiles
        ) { mediaTags, textTagsWithIndex, mediaFiles ->
            val seenProductIds = mutableSetOf<String>()
            val result = mutableListOf<AmityProduct>()

            // 1. Add text tags sorted by index first
            textTagsWithIndex
                .sortedBy { it.first }
                .forEach { (_, product) ->
                    if (seenProductIds.add(product.getProductId())) {
                        result.add(product)
                    }
                }

            // 2. Add media tags in order of media files
            mediaFiles.forEach { media ->
                val mediaId = media.id ?: media.url.toString()
                mediaTags[mediaId]?.forEach { product ->
                    if (seenProductIds.add(product.getProductId())) {
                        result.add(product)
                    }
                }
            }

            result
        }

    // Total count of distinct product tags across text and media
    val totalDistinctProductTagCount: kotlinx.coroutines.flow.Flow<Int>
        get() = allDistinctProductTags.map { it.size }

    // Set product tags from text mentions with index for sorting
    fun setTextProductTags(productsWithIndex: List<Pair<Int, AmityProduct>>) {
        viewModelScope.launch {
            _textProductTagsWithIndex.value = productsWithIndex
            _textProductTags.value = productsWithIndex.map { it.second }
        }
    }

    // Set product tags for a specific media file
    fun setProductTagsForMedia(mediaId: String, products: List<AmityProduct>) {
        viewModelScope.launch {
            val currentTags = _mediaProductTags.value.toMutableMap()
            currentTags[mediaId] = products
            _mediaProductTags.value = currentTags
        }
    }

    // Total distinct product count as StateFlow
    val totalDistinctProductCount: StateFlow<Int> by lazy {
        allDistinctProductTags
            .map { it.size }
            .stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    }

    // Check if can add more products
    fun canAddMoreProducts(): Boolean {
        return totalDistinctProductCount.value < MAX_TOTAL_PRODUCT_TAGS
    }

    // Calculate max selection for media product tagging
    fun getMaxSelectionForMedia(): Int {
        val currentTotal = totalDistinctProductCount.value
        val remaining = MAX_TOTAL_PRODUCT_TAGS - currentTotal
        return min(5, max(0, remaining))
    }

    // Get product tags for a specific media file
    fun getProductTagsForMedia(mediaId: String): List<AmityProduct> {
        return _mediaProductTags.value[mediaId] ?: emptyList()
    }

    // Remove product tags for a specific media file
    fun removeProductTagsForMedia(mediaId: String) {
        viewModelScope.launch {
            val currentTags = _mediaProductTags.value.toMutableMap()
            currentTags.remove(mediaId)
            _mediaProductTags.value = currentTags
        }
    }

    // Build AmityAttachmentProductTags from current media product tags state
    fun buildAttachmentProductTags(): AmityAttachmentProductTags? {
        val currentTags = _mediaProductTags.value
        if (currentTags.isEmpty()) return null

        val builder = AmityAttachmentProductTags.Builder()
        currentTags.forEach { (fileId, products) ->
            val mediaProductTags = products.map { product ->
                AmityProductTag.Media(
                    productId = product.getProductId(),
                    product = product
                )
            }
            builder.set(fileId, mediaProductTags)
        }
        return builder.build()
    }

    // Check if there are any attachment product tags
    fun hasAttachmentProductTags(): Boolean {
        return _mediaProductTags.value.values.any { it.isNotEmpty() }
    }

    // Clear all product tags
    fun clearAllProductTags() {
        viewModelScope.launch {
            _mediaProductTags.value = emptyMap()
        }
    }

    /**
     * Compute the total count of distinct product IDs across all media and text tags.
     */
    fun getTotalDistinctProductCount(): Int {
        val productIds = mutableSetOf<String>()
        _textProductTagsWithIndex.value.forEach { (_, product) ->
            productIds.add(product.getProductId())
        }
        _mediaProductTags.value.values.forEach { products ->
            products.forEach { product ->
                productIds.add(product.getProductId())
            }
        }
        return productIds.size
    }

    /**
     * Calculate how many products can still be selected for a specific media,
     * respecting both the per-media limit (5) and the per-post limit (20).
     *
     * @param mediaId The media whose selection sheet is about to open.
     * @return The effective maxSelection to pass to the product selection component.
     */
    fun getMaxSelectionForMedia(mediaId: String): Int {
        val totalDistinct = getTotalDistinctProductCount()
        val thisMediaProducts = _mediaProductTags.value[mediaId] ?: emptyList()
        val thisMediaProductIds = thisMediaProducts.map { it.getProductId() }.toSet()

        // Count distinct products in OTHER media + text (excluding this media's unique products)
        val allProductIds = mutableSetOf<String>()
        _textProductTagsWithIndex.value.forEach { (_, product) ->
            allProductIds.add(product.getProductId())
        }
        _mediaProductTags.value.forEach { (id, products) ->
            if (id != mediaId) {
                products.forEach { product ->
                    allProductIds.add(product.getProductId())
                }
            }
        }

        val otherDistinctCount = allProductIds.size
        val remainingSlots = MAX_TOTAL_PRODUCT_TAGS - otherDistinctCount
        return remainingSlots.coerceIn(0, MAX_PRODUCT_TAGS_PER_MEDIA)
    }

    /**
     * Check whether the per-post product tag limit has been reached.
     */
    fun isProductTagLimitReached(): Boolean {
        return getTotalDistinctProductCount() >= MAX_TOTAL_PRODUCT_TAGS
    }

    fun showAltTextConfigSheet() {
        showAltTextConfigSheet.value = true
    }

    fun hideAltTextConfigSheet() {
        showAltTextConfigSheet.value = false
    }

    fun shouldShowAltTextConfigSheet(): Boolean {
        return showAltTextConfigSheet.value
    }

    fun setAltTextMedia(media: AltTextMedia?) {
        altTextMedia.value = media
    }

    fun getAltTextMedia(): AltTextMedia? {
        return altTextMedia.value
    }

    private val _postCreationEvent by lazy {
        MutableStateFlow<AmityPostCreationEvent>(AmityPostCreationEvent.Initial)
    }
    val postCreationEvent get() = _postCreationEvent

    private val _selectedMediaFiles by lazy {
        MutableStateFlow<List<AmityPostMedia>>(emptyList())
    }
    val selectedMediaFiles get() = _selectedMediaFiles

    private val _isAllMediaSuccessfullyUploaded by lazy {
        MutableStateFlow(true)
    }
    val isAllMediaSuccessfullyUploaded get() = _isAllMediaSuccessfullyUploaded

    // Link preview state
    private val _detectedUrls by lazy {
        MutableStateFlow<List<AmityLink>>(emptyList())
    }
    val detectedUrls get() = _detectedUrls

    private val _linkPreviewMetadata by lazy {
        MutableStateFlow<AmityLinkPreviewMetadata?>(null)
    }
    val linkPreviewMetadata get() = _linkPreviewMetadata

    private val _isLinkPreviewDismissed by lazy {
        MutableStateFlow(false)
    }
    val isLinkPreviewDismissed get() = _isLinkPreviewDismissed

    private var previousUrl: String? = null
    private var metadataDisposable: io.reactivex.rxjava3.disposables.Disposable? = null

    fun updateDetectedUrls(urls: List<AmityLink>) {
        val firstUrl = urls.firstOrNull()?.getUrl()

        // If we have existing metadata and the first URL hasn't changed, preserve it
        val currentMetadata = _linkPreviewMetadata.value
        if (firstUrl == previousUrl && currentMetadata != null && urls.isNotEmpty()) {
            // Keep first link with metadata, others without metadata
            _detectedUrls.value = urls.mapIndexed { index, link ->
                if (index == 0) {
                    // First link keeps its metadata and renderPreview = true
                    try {
                        val domain = currentMetadata.getDomain()
                        val title = currentMetadata.getTitle()
                        val imageUrl = currentMetadata.getImageUrl()
                        val hasValidMetadata = !domain.isNullOrEmpty() || !title.isNullOrEmpty() || !imageUrl.isNullOrEmpty()
                        AmityLink(
                            index = link.getIndex(),
                            length = link.getLength(),
                            url = link.getUrl(),
                            renderPreview = hasValidMetadata,
                            domain = domain,
                            title = title,
                            imageUrl = imageUrl
                        )
                    } catch (e: Exception) {
                        link
                    }
                } else {
                    // Other links have no metadata and renderPreview = false
                    link
                }
            }
            return
        }

        _detectedUrls.value = urls

        // Fetch metadata only if URL changed
        // The debouncing is already handled by AmityMentionTextField (2 seconds)
        if (firstUrl != null && firstUrl != previousUrl) {
            previousUrl = firstUrl
            _isLinkPreviewDismissed.value = false
            _linkPreviewMetadata.value = null // Clear cached metadata
            fetchLinkPreviewMetadata(firstUrl)
        } else if (firstUrl == null) {
            // Clear metadata when no URLs detected
            previousUrl = null
            _linkPreviewMetadata.value = null
        }
    }

    private fun fetchLinkPreviewMetadata(url: String) {
        metadataDisposable?.dispose()
        metadataDisposable = AmityCoreClient.getLinkPreviewMetadata(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { metadata ->
                    _linkPreviewMetadata.value = metadata
                    // Update detected URLs with metadata
                    updateUrlsWithMetadata(metadata)
                },
                { error ->
                    // Handle error silently
                    _linkPreviewMetadata.value = null
                }
            )
    }

    private fun updateUrlsWithMetadata(metadata: AmityLinkPreviewMetadata) {
        val metadataDomain = metadata.getDomain()
        val metadataTitle = metadata.getTitle()
        val imageUrl = metadata.getImageUrl()

        _detectedUrls.value = _detectedUrls.value.mapIndexed { index, link ->
            try {
                val linkUrl = link.getUrl() ?: ""

                // Extract original text by checking if https:// was auto-added
                // If URL starts with https:// but originalText didn't, remove it for display
                val originalText = if (linkUrl.startsWith("https://") &&
                    !linkUrl.substring(8).startsWith("http")) {
                    linkUrl.substring(8) // Remove "https://"
                } else {
                    linkUrl
                }

                // Determine domain and title based on what metadata is available
                val finalDomain: String
                val finalTitle: String

                when {
                    // Both null → use original text for both
                    metadataDomain == null && metadataTitle == null -> {
                        finalDomain = originalText
                        finalTitle = originalText
                    }
                    // Domain exists but title null → use domain for both
                    metadataDomain != null && metadataTitle == null -> {
                        finalDomain = metadataDomain
                        finalTitle = metadataDomain
                    }
                    // Title exists but domain null → use original text for domain
                    metadataDomain == null && metadataTitle != null -> {
                        finalDomain = originalText
                        finalTitle = metadataTitle
                    }
                    // Both exist → use them as is
                    else -> {
                        finalDomain = metadataDomain!!
                        finalTitle = metadataTitle!!
                    }
                }

                AmityLink(
                    index = link.getIndex(),
                    length = link.getLength(),
                    url = linkUrl,
                    renderPreview = index == 0, // Always render preview for first link
                    domain = finalDomain,
                    title = finalTitle,
                    imageUrl = imageUrl
                )
            } catch (e: Exception) {
                link
            }
        }
    }

    fun dismissLinkPreview() {
        _isLinkPreviewDismissed.value = true
        // Set renderPreview to false for all links
        _detectedUrls.value = _detectedUrls.value.map { link ->
            try {
                AmityLink(
                    index = link.getIndex(),
                    length = link.getLength(),
                    url = link.getUrl(),
                    renderPreview = false,
                    domain = link.getDomain(),
                    title = link.getTitle(),
                    imageUrl = link.getImageUrl()
                )
            } catch (e: Exception) {
                link
            }
        }
    }

    fun preserveUrlWithMetadata() {
        // Preserve existing link with metadata when text is removed
        if (_detectedUrls.value.isNotEmpty() && _linkPreviewMetadata.value != null) {
            _detectedUrls.value = _detectedUrls.value.map { link ->
                try {
                    AmityLink(
                        index = 0,
                        length = 0,
                        url = link.getUrl(),
                        renderPreview = link.getRenderPreview(),
                        domain = link.getDomain(),
                        title = link.getTitle(),
                        imageUrl = link.getImageUrl()
                    )
                } catch (e: Exception) {
                    link
                }
            }
        }
    }

    fun setComposerOptions(options: AmityPostComposerOptions) {
        this.options = options
        when (options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                this.community = options.community
            }

            is AmityPostComposerOptions.AmityPostComposerCreateClipOptions -> {
                this.community = options.community
            }

            is AmityPostComposerOptions.AmityPostComposerEditOptions -> {
                preparePostData(options.post.getPostId())
            }

            is AmityPostComposerOptions.AmityPostComposerEditClipOptions -> {
                preparePostData(options.post.getPostId())
            }
        }
    }

    private fun preparePostData(postId: String) {
        getPostDetails(
            postId = postId,
            onPostLoading = {

            },
            onPostLoaded = { post ->
                this.community = (post.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
                this._post.value = post
                when (val postData = post.getData()) {
                    is AmityPost.Data.TEXT -> prepareTextPost(post)
                    is AmityPost.Data.IMAGE -> prepareImagePost(post)
                    is AmityPost.Data.VIDEO -> prepareVideoPost(postData)
                    is AmityPost.Data.CLIP -> prepareClipPost(post)
//            is AmityPost.Data.FILE -> prepareFilePost(post)
                    else -> {}
                }

                // Prepare product tags from parent post and child posts
                prepareProductTags(post)

                setPostAttachmentAllowedPickerType(
                    when (post.getChildren().firstOrNull()?.getData()) {
                        is AmityPost.Data.IMAGE -> AmityPostAttachmentAllowedPickerType.Image(
                            canAddMore = post.getChildren().size < MAX_ATTACHMENTS
                        )

                        is AmityPost.Data.VIDEO -> AmityPostAttachmentAllowedPickerType.Video(
                            canAddMore = post.getChildren().size < MAX_ATTACHMENTS
                        )

                        else -> AmityPostAttachmentAllowedPickerType.All
                    }
                )
                checkAllMediaUploadedSuccessfully()
            },
            onPostLoadFailed = {

            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun getPostDetails(
        postId: String,
        onPostLoading: () -> Unit,
        onPostLoaded: (AmityPost) -> Unit,
        onPostLoadFailed: (Throwable) -> Unit,
    ): Completable {
        return AmitySocialClient.newPostRepository()
            .getPost(postId)
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                onPostLoading.invoke()
            }
            .doOnSuccess {
                onPostLoaded.invoke(it)
            }
            .doOnError {
                onPostLoadFailed.invoke(it)
            }
            .ignoreElement()
    }

    private fun prepareTextPost(post: AmityPost) {
        setUpPostTextWithImagesOrFiles(post)
    }

    private fun prepareImagePost(post: AmityPost) {
        getPostImage(post)?.let {
            val feedImage = mapImageToFeedImage(it, Type.IMAGE)
            mediaMap[feedImage.url.toString()] = feedImage
        }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun prepareVideoPost(videoData: AmityPost.Data.VIDEO) {
        val thumbnail = videoData.getThumbnailImage()
        val videoPost = if (thumbnail != null) {
            mapVideoToFeedImage(
                video = videoData.getVideo().blockingGet(),
                thumbnail = thumbnail,
                type = Type.VIDEO
            )
            mapImageToFeedImage(thumbnail, Type.VIDEO)
        } else {
            // Create placeholder for video without thumbnail
            createPlaceholderVideoMedia(videoData)
        }
        mediaMap[videoPost.url.toString()] = videoPost
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun prepareClipPost(post: AmityPost) {
        getPostVideoThumbnail(post)?.let {
            val videoPost = mapImageToFeedImage(it, Type.VIDEO)
            mediaMap[videoPost.url.toString()] = videoPost
        }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    /**
     * Prepare product tags from existing post for edit mode.
     * Collects product tags from:
     * 1. Parent post text (AmityProductTag.Text) - use post.getProducts() for full product data
     * 2. Child posts media (AmityProductTag.Media)
     */
    private fun prepareProductTags(post: AmityPost) {
        // 1. Get text product tags from parent post
        // Use post.getProducts() which contains full product data, then filter by productIds from text tags
        val textTags = post.getProductTags()
            .filterIsInstance<AmityProductTag.Text>()

        val productsMap = post.getProducts()?.associateBy { it.getProductId() } ?: emptyMap()

        // Create list with index for sorting
        val textProductTagsWithIndex = textTags.mapNotNull { tag ->
            productsMap[tag.productId]?.let { product ->
                tag.index to product
            }
        }

        if (textProductTagsWithIndex.isNotEmpty()) {
            _textProductTagsWithIndex.value = textProductTagsWithIndex
            _textProductTags.value = textProductTagsWithIndex.map { it.second }
        }
        _originalTextProductTagIds.value = textProductTagsWithIndex.map { it.second.getProductId() }

        // 2. Get media product tags from child posts
        val productTagsMap = mutableMapOf<String, List<AmityProduct>>()

        post.getChildren().forEach { childPost ->
            val fileId = when (val childData = childPost.getData()) {
                is AmityPost.Data.IMAGE -> childData.getImage()?.getFileId()
                is AmityPost.Data.VIDEO -> childData.getVideo().blockingGet().getFileId()
                else -> null
            }

            if (fileId != null) {
                // Get product tags from child post using getProducts() for full product data
                val mediaTagProductIds = childPost.getProductTags()
                    .filterIsInstance<AmityProductTag.Media>()
                    .map { it.productId }
                    .toSet()

                val childProductTags = childPost.getProducts()
                    ?.filter { it.getProductId() in mediaTagProductIds }
                    ?: emptyList()

                if (childProductTags.isNotEmpty()) {
                    productTagsMap[fileId] = childProductTags
                }
            }
        }

        // Update the media product tags state
        _originalMediaProductTagIds.value = productTagsMap.mapValues { entry ->
            entry.value.map { it.getProductId() }.toSet()
        }
        if (productTagsMap.isNotEmpty()) {
            _mediaProductTags.value = productTagsMap
        }
    }

    private fun setUpPostTextWithImagesOrFiles(post: AmityPost) {
        val postChildren = post.getChildren()
        if (postChildren.isNotEmpty()) {
            when (postChildren.first().getData()) {
                is AmityPost.Data.IMAGE -> setupImagePost(postChildren)
                is AmityPost.Data.VIDEO -> setupVideoPost(postChildren)
//                is AmityPost.Data.FILE -> setupFilePost(postChildren)
                else -> {}
            }
        }
    }

    private fun setupImagePost(postChildren: List<AmityPost>) {
        postChildren
            .map { getPostImage(it) }
            .forEach { image ->
                image?.let {
                    val feedImage = mapImageToFeedImage(it, Type.IMAGE)
                    mediaMap[feedImage.url.toString()] = feedImage
                }
            }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun setupVideoPost(postChildren: List<AmityPost>) {
        postChildren.filter { it.getData() is AmityPost.Data.VIDEO }.forEach { post ->
            val thumbnail = getPostVideoThumbnail(post)
            val videoData = post.getData() as AmityPost.Data.VIDEO
            val videoMedia = if (thumbnail != null) {
                mapVideoToFeedImage(
                    video = videoData.getVideo().blockingGet(),
                    thumbnail = thumbnail,
                    type = Type.VIDEO
                )
            } else {
                createPlaceholderVideoMedia(videoData)
            }
            mediaMap[videoMedia.url.toString()] = videoMedia
        }
        _selectedMediaFiles.value = mediaMap.values.toList()
    }

    private fun mapImageToFeedImage(image: AmityImage, type: Type): AmityPostMedia {
        val fileId = image.getFileId()
        return AmityPostMedia(
            id = fileId,
            uploadId = fileId, // Use fileId as uploadId for existing media so tag icon shows in edit mode
            url = image.getUrl(AmityImage.Size.MEDIUM).toUri(),
            uploadState = AmityFileUploadState.COMPLETE,
            currentProgress = 100,
            type = type,
            media = AmityPostMedia.Media.Image(image)
        )
    }

    private fun mapVideoToFeedImage(video: AmityVideo, thumbnail: AmityImage, type: Type): AmityPostMedia {
        val fileId = video.getFileId()
        return AmityPostMedia(
            id = fileId,
            uploadId = fileId, // Use fileId as uploadId for existing media so tag icon shows in edit mode
            url = thumbnail.getUrl(AmityImage.Size.MEDIUM).toUri(),
            uploadState = AmityFileUploadState.COMPLETE,
            currentProgress = 100,
            type = type,
            media = AmityPostMedia.Media.Video(video)
        )
    }

    private fun createPlaceholderVideoMedia(videoData: AmityPost.Data.VIDEO): AmityPostMedia {
        val fileId = videoData.getVideo().blockingGet().getFileId()

        return AmityPostMedia(
            id = fileId,
            uploadId = fileId, // Use fileId as uploadId for existing media so tag icon shows in edit mode
            url = Uri.EMPTY, // Use empty URI as placeholder
            uploadState = AmityFileUploadState.COMPLETE,
            currentProgress = 100,
            type = Type.VIDEO,
            media = null // No thumbnail available
        )
    }

    private fun getPostImage(newsFeed: AmityPost): AmityImage? {
        val imageData = newsFeed.getData() as? AmityPost.Data.IMAGE
        return imageData?.getImage()
    }

    private fun getPostVideoThumbnail(post: AmityPost): AmityImage? {
        val videoData = post.getData() as? AmityPost.Data.VIDEO
        return videoData?.getThumbnailImage()
    }

    private fun getPostFile(newsFeed: AmityPost): AmityFile? {
        val fileData = newsFeed.getData() as? AmityPost.Data.FILE
        return fileData?.getFile()
    }

    fun updatePost(
        postText: String,
        postTitle: String?,
        mentionedUsers: List<AmityMentionMetadata.USER>,
        hashtags: List<AmityHashtag> = emptyList(),
        links: List<AmityLink> = emptyList(),
        productTags: List<AmityProductTag.Text> = emptyList(),
        attachmentProductTags: AmityAttachmentProductTags? = null,
    ) {
        if (postText.length > MAX_CHAR_LIMIT) {
            setPostCreationEvent(
                AmityPostCreationEvent.Failed(
                    TextPostExceedException(
                        MAX_CHAR_LIMIT
                    )
                )
            )
            return
        }

        setPostCreationEvent(AmityPostCreationEvent.Updating)

        val postId = _post.value!!.getPostId()

        // Add special handling for clip posts
        val isClipPost = options is AmityPostComposerOptions.AmityPostComposerEditClipOptions

        if (isClipPost) {
            // For clip posts, only update the text without modifying attachments
            val postEditor = AmitySocialClient.newPostRepository()
                .editPost(postId = postId)
                .text(postText.trim())

            val metadata = mentionedUsers.takeIf { it.isNotEmpty() }?.let {
                AmityMentionMetadataCreator(mentionedUsers).create()
            }
            val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()

            postEditor.apply {
                metadata?.let {
                    this.metadata(metadata)
                    this.mentionUsers(mentionUserIds.toList())
                }
                if (links.isNotEmpty()) {
                    this.links(links)
                }
            }

            postEditor.build().apply()
                .andThen(Single.defer {
                    AmitySocialClient.newPostRepository()
                        .getPost(postId)
                        .firstOrError()
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (it.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                        setPostCreationEvent(AmityPostCreationEvent.Pending)
                    } else {
                        AmityPostComposerHelper.updatePost(postId)
                        setPostCreationEvent(AmityPostCreationEvent.Success)
                    }
                }
                .doOnError {
                    setPostCreationEvent(AmityPostCreationEvent.Failed(it))
                }
                .subscribe()
        } else {
            // Original handling for non-clip postsdeleteImageOrFileInPost()
            deleteImageOrFileInPost()
                .andThen(Completable.defer {
                    val attachments = _post.value?.getChildren()
                        ?.map { it.getData() }
                        ?: emptyList()
                    val newAttachments = uploadedMediaMap.values.toList()
                    updateParentPost(postId,
                        postText.trim(),
                        postTitle,
                        attachments,
                        newAttachments,
                        mentionedUsers,
                        hashtags,
                        links,
                        productTags = productTags,
                        attachmentProductTags = attachmentProductTags,
                    )
                })
                .andThen(Single.defer {
                    AmitySocialClient.newPostRepository()
                        .getPost(postId)
                        .firstOrError()
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (it.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                        setPostCreationEvent(AmityPostCreationEvent.Pending)
                    } else {
                        AmityPostComposerHelper.updatePost(postId)
                        checkAndNotifyMissingProductTags(it, productTags)
                        setPostCreationEvent(AmityPostCreationEvent.Success)
                    }
                }
                .doOnError {
                    setPostCreationEvent(AmityPostCreationEvent.Failed(it))
                }
                .subscribe()
        }
    }

    private fun deleteImageOrFileInPost(): Completable {
        return _post.value?.getChildren()?.let { ekoPostItems ->
            Observable.fromIterable(ekoPostItems)
                .map { ekoPostItem ->
                    when (val postData = ekoPostItem.getData()) {
                        is AmityPost.Data.IMAGE -> {
                            if (postData.getImage()?.getFileId() in deletedImageIds) {
                                AmitySocialClient.newPostRepository()
                                    .hardDeletePost(ekoPostItem.getPostId())
                            } else {
                                Completable.complete()
                            }
                        }

                        is AmityPost.Data.VIDEO -> {
                            if (postData.getThumbnailImage()?.getFileId() in deletedImageIds) {
                                AmitySocialClient.newPostRepository()
                                    .hardDeletePost(ekoPostItem.getPostId())
                            } else {
                                Completable.complete()
                            }
                        }

                        else -> {
                            Completable.complete()
                        }
                    }
                }.ignoreElements()
        } ?: run {
            Completable.complete()
        }
    }

    private fun updateParentPost(
        postId: String,
        postText: String,
        postTitle: String?,
        attachments: List<AmityPost.Data>,
        newAttachments: List<AmityFileInfo>,
        mentionedUsers: List<AmityMentionMetadata.USER>,
        hashtags: List<AmityHashtag> = emptyList(),
        links: List<AmityLink> = emptyList(),
        productTags: List<AmityProductTag.Text> = emptyList(),
        attachmentProductTags: AmityAttachmentProductTags? = null,
    ): Completable {
        // Determine attachment type by examining all attachments, not just the first one
        val attachmentType: Type? = run {
            // Check if there are any non-deleted IMAGE attachments
            val hasImages = attachments.any {
                it is AmityPost.Data.IMAGE &&
                        !(it.getImage()?.getFileId() in deletedImageIds)
            }

            if (hasImages) return@run Type.IMAGE

            // Check if there are any non-deleted VIDEO attachments
            val hasVideos = attachments.any {
                it is AmityPost.Data.VIDEO &&
                        !(it.getThumbnailImage()?.getFileId() in deletedImageIds)
            }

            if (hasVideos) return@run Type.VIDEO

            // If no existing attachments remain, check new attachments
            if (newAttachments.isNotEmpty()) {
                when (newAttachments.first()) {
                    is AmityImage -> Type.IMAGE
                    is AmityVideo -> Type.VIDEO
                    else -> null
                }
            } else {
                null // No attachments at all
            }
        }

        val postEditor = AmitySocialClient.newPostRepository()
            .editPost(postId = postId)
            .text(postText)

        if (postTitle != null) {
            postEditor.title(postTitle)
        }

        when (attachmentType) {
            Type.VIDEO -> {
                val videos = attachments.mapNotNull {
                    it as? AmityPost.Data.VIDEO
                }
                    .map {
                        it.getVideo().blockingGet()
                    }
                    .filter {
                        !(it.getFileId().let(deletedImageIds::contains))
                    } + newAttachments.map { it as AmityVideo }

                postEditor.attachments(*videos.toTypedArray())
            }

            Type.IMAGE -> {
                val images = attachments
                    .mapNotNull { (it as? AmityPost.Data.IMAGE)?.getImage() }
                    .filter {
                        !deletedImageIds.contains(it.getFileId())
                    } + newAttachments.map { it as AmityImage }

                postEditor.attachments(*images.toTypedArray())
            }

            else -> {
                postEditor.text(postText)
                if (postTitle != null) {
                    postEditor.title(postTitle)
                }
                postEditor.attachments(*emptyList<AmityImage>().toTypedArray())
            }
        }

        val metadata = createMetadata(mentionedUsers, hashtags)
        val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()
        postEditor
            .apply {
                metadata?.let {
                    this.metadata(metadata)
                    this.mentionUsers(mentionUserIds.toList())
                    this.hashtags(hashtags.map { it.getText() })
                }
                if (links.isNotEmpty()) {
                    this.links(links)
                }
            }

        return postEditor
            .productTags(productTags)
            .taggedProducts(attachmentProductTags)
            .build()
            .apply()
    }

    fun createPost(
        postText: String,
        postTitle: String?,
        mentionedUsers: List<AmityMentionMetadata.USER>,
        hashtags: List<AmityHashtag> = emptyList(),
        links: List<AmityLink> = emptyList(),
        productTags: List<AmityProductTag.Text>? = null,
        attachmentProductTags: AmityAttachmentProductTags? = null,
    ) {
        if (postText.length > MAX_CHAR_LIMIT) {
            setPostCreationEvent(
                AmityPostCreationEvent.Failed(
                    TextPostExceedException(
                        MAX_CHAR_LIMIT
                    )
                )
            )
            return
        }

        setPostCreationEvent(AmityPostCreationEvent.Creating)
        val targetType = getTargetTypeForPostCreation()
        val targetId = getTargetIdForPostCreation()
        // Create metadata with both mentions and hashtags
        val metadata = createMetadata(mentionedUsers, hashtags)


        val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()

        when {
            isUploadedImageMedia() -> {
                val orderById =
                    mediaMap.values.withIndex().associate { it.value.id to it.index }
                val sortedImages =
                    uploadedMediaMap.values
                        .filter {
                            mediaMap.values.any { postMedia -> postMedia.id == it.getFileId() }
                        }.sortedBy {
                            orderById[it.getFileId()]
                        }.map {
                            it as AmityImage
                        }.toSet()

                createPostTextAndImages(
                    postText = postText,
                    images = sortedImages,
                    targetType = targetType,
                    title = postTitle,
                    targetId = targetId,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                    productTags = productTags,
                    attachmentProductTags = attachmentProductTags,
                )
            }

            isUploadedVideoMedia() -> {
                val videos = uploadedMediaMap.values.toList().map {
                    it as AmityVideo
                }.toSet()

                createPostTextAndVideos(
                    postText = postText,
                    videos = videos,
                    targetType = targetType,
                    targetId = targetId,
                    title = postTitle,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                    productTags = productTags,
                    attachmentProductTags = attachmentProductTags,
                )
            }

            isClipMedia() -> {
                val data = (options as AmityPostComposerOptions.AmityPostComposerCreateClipOptions)
                createPostTextAndClip(
                    postText = postText,
                    clip = data.clip,
                    targetType = AmityPost.TargetType.enumOf(data.targetType.name.lowercase()),
                    targetId = data.targetId ?: AmityCoreClient.getUserId(),
                    aspectRatio = data.aspectRatio ?: AmityClip.DisplayMode.FILL,
                    isMute = data.isMute == true,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                )
            }
            // TODO: 16/6/24 create file post
            else -> {
                createPostText(
                    postText = postText,
                    targetType = targetType,
                    targetId = targetId,
                    title = postTitle,
                    metadata = metadata,
                    mentionUserIds = mentionUserIds,
                    hashtags = hashtags.map { it.getText() },
                    links = links,
                    productTags = productTags,
                )
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (it.getReviewStatus() == AmityReviewStatus.UNDER_REVIEW) {
                    setPostCreationEvent(AmityPostCreationEvent.Pending)
                } else {
                    AmityPostComposerHelper.addNewPost(it)
                    checkAndNotifyMissingProductTags(it, productTags)
                    setPostCreationEvent(AmityPostCreationEvent.Success)
                }
            }
            .doOnError {
                setPostCreationEvent(AmityPostCreationEvent.Failed(it))
            }
            .subscribe()
    }

    private fun createPostText(
        postText: String,
        targetType: AmityPost.TargetType,
        targetId: String,
        title: String?,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
        links: List<AmityLink> = emptyList(),
        productTags: List<AmityProductTag.Text>? = null,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createTextPost(
                targetType = targetType,
                targetId = targetId,
                title = title,
                text = postText,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
                links = links,
                productTags = productTags,
            )
    }

    private fun createPostTextAndImages(
        title: String?,
        postText: String,
        images: Set<AmityImage>,
        targetType: AmityPost.TargetType,
        targetId: String,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
        productTags: List<AmityProductTag.Text>? = null,
        attachmentProductTags: AmityAttachmentProductTags? = null,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createImagePost(
                targetType = targetType,
                targetId = targetId,
                title = title,
                text = postText,
                images = images,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
                productTags = productTags,
                attachmentProductTags = attachmentProductTags,
            )
    }

    private fun createPostTextAndVideos(
        title: String?,
        postText: String,
        videos: Set<AmityVideo>,
        targetType: AmityPost.TargetType,
        targetId: String,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
        productTags: List<AmityProductTag.Text>? = null,
        attachmentProductTags: AmityAttachmentProductTags? = null,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createVideoPost(
                targetType = targetType,
                targetId = targetId,
                title = title,
                text = postText,
                videos = videos,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
                productTags = productTags,
                attachmentProductTags = attachmentProductTags,
            )
    }

    private fun createPostTextAndClip(
        postText: String,
        clip: AmityClip,
        targetType: AmityPost.TargetType,
        targetId: String,
        isMute: Boolean,
        aspectRatio: AmityClip.DisplayMode,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<String>,
    ): Single<AmityPost> {
        return AmitySocialClient.newPostRepository()
            .createClipPost(
                targetType = targetType,
                targetId = targetId,
                clip = clip,
                text = postText,
                displayMode = aspectRatio,
                isMuted = isMute,
                metadata = metadata,
                mentionUserIds = mentionUserIds,
                hashtags = hashtags,
            )
    }

    fun addMedia(medias: List<Uri>, mediaType: Type) {
        if (mediaType == Type.IMAGE && medias.size + mediaMap.size > MEDIA_IMAGE_UPLOAD_LIMIT) {
            setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.MaxUploadLimitReached(mediaType))
            return
        } else if (mediaType == Type.VIDEO && medias.size + mediaMap.size > MEDIA_VIDEO_UPLOAD_LIMIT) {
            setPostAttachmentPickerEvent(AmityPostAttachmentPickerEvent.MaxUploadLimitReached(mediaType))
            return
        }

        medias.filter {
            !mediaMap.containsKey(it.toString())
        }.map { uri ->
            val postMedia = AmityPostMedia(UUID.randomUUID().toString(), uri, mediaType)
            mediaMap[uri.toString()] = postMedia
            uploadMedia(postMedia)
        }

        if (medias.isNotEmpty()) {
            setPostAttachmentAllowedPickerType(
                when (mediaType) {
                    Type.IMAGE -> AmityPostAttachmentAllowedPickerType.Image(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    Type.VIDEO -> AmityPostAttachmentAllowedPickerType.Video(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    else -> {
                        AmityPostAttachmentAllowedPickerType.All
                    }
                }
            )
        }
    }


    fun removeMedia(postMedia: AmityPostMedia) {
        mediaMap.remove(postMedia.url.toString())
        uploadFailedMediaMap.remove(postMedia.url.toString())
        cancelUpload(postMedia.uploadId)

        if (postMedia.id != null) {
            //In case update post we want to keep track of the images to delete
            if (_post.value != null) {
                deletedImageIds.add(postMedia.id!!)
            } else {
                uploadedMediaMap.remove(postMedia.id!!)
            }
            // Remove product tags for this media
            removeProductTagsForMedia(postMedia.id!!)
        }
        _selectedMediaFiles.value = mediaMap.values.toList()

        checkAllMediaUploadedSuccessfully()

        if (mediaMap.isEmpty()) {
            setPostAttachmentAllowedPickerType(AmityPostAttachmentAllowedPickerType.All)
            // Clear all product tags when all media is removed
            clearAllProductTags()
        } else {
            val mediaType = mediaMap.values.first().type
            setPostAttachmentAllowedPickerType(
                when (mediaType) {
                    Type.IMAGE -> AmityPostAttachmentAllowedPickerType.Image(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    Type.VIDEO -> AmityPostAttachmentAllowedPickerType.Video(canAddMore = mediaMap.size < MAX_ATTACHMENTS)
                    else -> {
                        AmityPostAttachmentAllowedPickerType.All
                    }
                }
            )
        }
    }

    fun isUploadedImageMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityImage }
    }

    fun isUploadedVideoMedia(): Boolean {
        return uploadedMediaMap.values.any { it is AmityVideo }
    }

    fun isClipMedia(): Boolean {
        return options is AmityPostComposerOptions.AmityPostComposerCreateClipOptions
    }

    fun isUploadingImageMedia(): Boolean {
        return mediaMap.values.any { it.type == Type.IMAGE }
    }

    fun isUploadingVideoMedia(): Boolean {
        return mediaMap.values.any { it.type == Type.VIDEO }
    }

    private fun checkAllMediaUploadedSuccessfully() {
        viewModelScope.launch {
            _isAllMediaSuccessfullyUploaded.value = if (mediaMap.isEmpty()) {
                true
            } else {
                mediaMap.values.all { it.uploadState == AmityFileUploadState.COMPLETE }
            }
        }
    }

    private fun setPostCreationEvent(event: AmityPostCreationEvent) {
        viewModelScope.launch {
            _postCreationEvent.value = event
            delay(500)
            _postCreationEvent.value = AmityPostCreationEvent.Initial
        }
    }

    /**
     * Returns true if an error snackbar was shown (some products became unavailable).
     */
    private fun checkAndNotifyMissingProductTags(
        post: AmityPost,
        sentProductTags: List<AmityProductTag.Text>?,
    ): Boolean {
        if (sentProductTags != null) {
            val unavailableMessage = AmityAppContext.getContext()
                .getString(R.string.amity_v4_post_products_unavailable_toast)

            // Condition 3: any product tagged in text or media is ARCHIVED
            val hasArchivedProduct = post.getProductTags().firstOrNull { (it as? AmityProductTag.Text)?.product?.getStatus() == AmityProductStatus.ARCHIVED } != null ||
                    post.getChildren().any { child ->
                        child.getProductTags().firstOrNull { (it as? AmityProductTag.Media)?.product?.getStatus() == AmityProductStatus.ARCHIVED } != null
                    }
            if (hasArchivedProduct) {
                AmityUIKitSnackbar.publishSnackbarErrorMessage(message = unavailableMessage)
                return true
            }

            // Condition 1: sent text product count > result text product count
            val sentTextProductIds = sentProductTags.map { it.productId }
            val resultTextProductIds = post.getProductTags()
                .filterIsInstance<AmityProductTag.Text>()
                .map { it.productId }

            if (sentTextProductIds.size > resultTextProductIds.size) {
                AmityUIKitSnackbar.publishSnackbarErrorMessage(message = unavailableMessage)
                return true
            }

            // Condition 2: any media entry has more sent products than what came back in child post
            val sentMediaProductTagsMap = _mediaProductTags.value
            if (sentMediaProductTagsMap.isNotEmpty()) {
                val childTagCountMap = mutableMapOf<String, Int>()
                post.getChildren().forEach { childPost ->
                    val fileId = when (val childData = childPost.getData()) {
                        is AmityPost.Data.IMAGE -> childData.getImage()?.getFileId()
                        is AmityPost.Data.VIDEO -> childData.getVideo().blockingGet().getFileId()
                        else -> null
                    }
                    if (fileId != null) {
                        childTagCountMap[fileId] = childPost.getProductTags()
                            .filterIsInstance<AmityProductTag.Media>()
                            .size
                    }
                }

                val hasMissingMediaTag = sentMediaProductTagsMap.any { (fileId, sentProducts) ->
                    sentProducts.size > (childTagCountMap[fileId] ?: 0)
                }

                if (hasMissingMediaTag) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage(message = unavailableMessage)
                }
                return true
            }
        }
        return false
    }

    private fun hasProductTagsChanged(): Boolean {
        val currentMediaTagIds = _mediaProductTags.value.mapValues { (_, products) ->
            products.map { it.getProductId() }.toSet()
        }
        val originalMediaTagIds = _originalMediaProductTagIds.value

        if (currentMediaTagIds.keys != originalMediaTagIds.keys) return true
        for ((fileId, currentIds) in currentMediaTagIds) {
            if (currentIds != originalMediaTagIds[fileId]) return true
        }

        val currentTextTagIds = _textProductTags.value.map { it.getProductId() }.toSet()
        val originalTextTagIds = _originalTextProductTagIds.value.toSet()
        if (currentTextTagIds != originalTextTagIds) return true

        return false
    }

    private fun cancelUpload(uploadId: String?) {
        uploadId?.let {
            AmityFileService().cancelUpload(uploadId)
        }
    }


    private fun uploadMedia(postMedia: AmityPostMedia) {
        updateMediaTransCodingStatus(postMedia)
        AmityFileService()
            .run {
                when (postMedia.type) {
                    Type.IMAGE -> uploadImage(postMedia.url)
                    Type.VIDEO -> {
                        uploadVideo(
                            postMedia.url,
                            AmityContentFeedType.POST
                        )
                    }

                    Type.ClIP -> uploadClip(
                        postMedia.url,
                        AmityContentFeedType.POST
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        updateMediaUploadStatus(postMedia, it)
                    }
                    .ignoreElements()
                    .subscribe()
            }
    }

    private fun updateMediaTransCodingStatus(
        postMedia: AmityPostMedia,
    ) {
        val pm = AmityPostMedia(
            id = postMedia.id,
            uploadId = postMedia.uploadId,
            url = postMedia.url,
            uploadState = AmityFileUploadState.UPLOADING,
            currentProgress = 1,
            type = postMedia.type
        )
        updateList(pm)
    }

    private fun updateMediaUploadStatus(
        postMedia: AmityPostMedia,
        result: AmityUploadResult<AmityFileInfo>,
    ) {
        when (result) {
            is AmityUploadResult.PROGRESS -> {
                val pm = AmityPostMedia(
                    id = postMedia.id,
                    uploadId = postMedia.uploadId,
                    url = postMedia.url,
                    uploadState = AmityFileUploadState.UPLOADING,
                    currentProgress = result.getUploadInfo().getProgressPercentage(),
                    type = postMedia.type
                )
                updateList(pm)
            }

            is AmityUploadResult.COMPLETE -> {
                val file = result.getFile()
                uploadFailedMediaMap.remove(postMedia.url.toString())
                uploadedMediaMap[file.getFileId()] = file
                val media = if (file is AmityImage) {
                    AmityPostMedia.Media.Image(file)
                } else {
                    null
                }
                val pm = AmityPostMedia(
                    id = result.getFile().getFileId(),
                    uploadId = postMedia.uploadId,
                    url = postMedia.url,
                    uploadState = AmityFileUploadState.COMPLETE,
                    currentProgress = 100,
                    type = postMedia.type,
                    media = media,
                )
                updateList(pm)
            }

            is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                if (mediaMap.containsKey(postMedia.url.toString())) {
                    val pm = AmityPostMedia(
                        postMedia.id,
                        postMedia.uploadId,
                        postMedia.url,
                        AmityFileUploadState.FAILED,
                        0,
                        postMedia.type
                    )
                    updateList(pm)

                    val firstTimeFailedToUpload =
                        !uploadFailedMediaMap.containsKey(pm.url.toString())
                    uploadFailedMediaMap[pm.url.toString()] = firstTimeFailedToUpload
                }
            }
        }
    }

    private fun updateList(postMedia: AmityPostMedia) {
        viewModelScope.launch {
            if (mediaMap.containsKey(postMedia.url.toString())) {
                mediaMap[postMedia.url.toString()] = postMedia
                _selectedMediaFiles.value = mediaMap.values.toList()
            }
        }
        checkAllMediaUploadedSuccessfully()
    }

    private fun getTargetForPostCreation(): AmityPost.Target {
        return when (val option = options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                when (option.targetType) {
                    AmityPostTargetType.USER -> {
                        option.targetId?.let { AmityPost.Target.USER.create(it) }
                            ?: AmityPost.Target.USER.create(AmityCoreClient.getUserId())
                    }

                    AmityPostTargetType.COMMUNITY -> {
                        option.targetId?.let { AmityPost.Target.COMMUNITY.create(it) }
                    }
                }
            }

            else -> AmityPost.Target.USER.create(AmityCoreClient.getUserId())
        } ?: AmityPost.Target.USER.create(AmityCoreClient.getUserId())
    }

    private fun getTargetTypeForPostCreation(): AmityPost.TargetType {
        return when (val option = options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                when (option.targetType) {
                    AmityPostTargetType.USER -> AmityPost.TargetType.USER
                    AmityPostTargetType.COMMUNITY -> AmityPost.TargetType.COMMUNITY
                }
            }
            else -> AmityPost.TargetType.USER
        }
    }

    private fun getTargetIdForPostCreation(): String {
        return when (val option = options) {
            is AmityPostComposerOptions.AmityPostComposerCreateOptions -> {
                option.targetId ?: AmityCoreClient.getUserId()
            }
            else -> AmityCoreClient.getUserId()
        }
    }

    fun updateAltText(
        fileId: String,
        altText: String,
        onSuccess: (AmityImage) -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        AmityCoreClient.newFileRepository().let { fileRepository ->
            fileRepository.updateAltText(fileId, altText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    refreshMedia(fileId, onSuccess)
                }
                .doOnError {
                    onError.invoke(it)
                }
                .subscribe()
        }
    }

    private fun refreshMedia(fileId: String, onSuccess: (AmityImage) -> Unit) {
        AmityCoreClient.newFileRepository().getFile(fileId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { rawFile: AmityRawFile ->
                if (rawFile.getFileType() == AmityFileType.IMAGE) {
                    val imageFromRaw: AmityImage? =
                        rawFile.asAmityImage() // null for incorrect file type
                    imageFromRaw?.let { image ->
                        updateMedia(image)
                        onSuccess.invoke(image)
                    }
                }
            }
            .subscribe()
    }

    private fun updateMedia(image: AmityImage) {
        val media = mediaMap.values.firstOrNull { it.id == image.getFileId() }
        media?.let {
            val pm = AmityPostMedia(
                id = image.getFileId(),
                uploadId = it.uploadId,
                url = it.url,
                uploadState = AmityFileUploadState.COMPLETE,
                currentProgress = 100,
                type = it.type,
                media = AmityPostMedia.Media.Image(image)
            )
            updateList(pm)
        }
    }

    override fun onCleared() {
        super.onCleared()
        metadataDisposable?.dispose()
    }
}

sealed class AmityPostAttachmentPickerEvent {
    object Initial : AmityPostAttachmentPickerEvent()
    object OpenImageOrVideoSelectionSheet : AmityPostAttachmentPickerEvent()
    object OpenImageCamera : AmityPostAttachmentPickerEvent()
    object OpenVideoCamera : AmityPostAttachmentPickerEvent()
    object OpenImagePicker : AmityPostAttachmentPickerEvent()
    object OpenVideoPicker : AmityPostAttachmentPickerEvent()
    object OpenFilePicker : AmityPostAttachmentPickerEvent()
    data class MaxUploadLimitReached(val mediaType: Type) : AmityPostAttachmentPickerEvent()
}

sealed class AmityPostAttachmentAllowedPickerType(
    val isEnabled: Boolean,
) {
    object All : AmityPostAttachmentAllowedPickerType(true)
    data class Image(val canAddMore: Boolean) : AmityPostAttachmentAllowedPickerType(canAddMore)
    data class Video(val canAddMore: Boolean) : AmityPostAttachmentAllowedPickerType(canAddMore)
    data class File(val canAddMore: Boolean) : AmityPostAttachmentAllowedPickerType(canAddMore)
}

sealed class AmityPostCreationEvent {
    object Initial : AmityPostCreationEvent()
    object Creating : AmityPostCreationEvent()
    object Updating : AmityPostCreationEvent()
    class Failed(val throwable: Throwable) : AmityPostCreationEvent()
    object Success : AmityPostCreationEvent()
    object Pending : AmityPostCreationEvent()
}

class TextPostExceedException(val charLimit: Int) : Exception()

const val MEDIA_VIDEO_UPLOAD_LIMIT = 10
const val MEDIA_IMAGE_UPLOAD_LIMIT = 10

/**
 * Creates a combined metadata object containing both mentions and hashtags
 */
fun createMetadata(
    mentionedUsers: List<AmityMentionMetadata.USER>,
    hashtags: List<AmityHashtag>
): JsonObject? {
    // If no mentions or hashtags, return null
    if (mentionedUsers.isEmpty() && hashtags.isEmpty()) {
        return null
    }

    // Create hashtag metadata if hashtags exist
    val validHashtags = hashtags.takeIf { it.isNotEmpty() }?.let {
        // Check if we exceed the maximum of 30 hashtags
        if (hashtags.size > 30) hashtags.take(30) else hashtags
    }

    return AmityPostMetadataCreator(
        hashtags = validHashtags,
        mentionMetaData = mentionedUsers
    ).create()
}