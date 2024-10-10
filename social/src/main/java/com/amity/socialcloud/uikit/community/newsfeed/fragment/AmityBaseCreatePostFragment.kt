package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.toPublisher
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.base.AmitySpacesItemDecoration
import com.amity.socialcloud.uikit.common.base.LinearLayoutPagerManager
import com.amity.socialcloud.uikit.common.common.AmityFileUtils
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.bottomsheet.AmityBottomSheetListFragment
import com.amity.socialcloud.uikit.common.common.views.bottomsheet.AmityMenuItemClickListener
import com.amity.socialcloud.uikit.common.common.views.dialog.AmityAlertDialogFragment
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.model.AmityMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.AmityOptionMenuColorUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentPostCreateBinding
import com.amity.socialcloud.uikit.community.domain.model.AmityFileAttachment
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityCreatePostFileAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityCreatePostMediaAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityPostAttachmentOptionsAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionPagingDataAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostFileActionListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostImageActionListener
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityPostAttachmentOptionItem
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.newsfeed.model.FileUploadState
import com.amity.socialcloud.uikit.community.newsfeed.model.PostMedia
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityCreatePostViewModel
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_COMMUNITY_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ATTACHMENT_OPTIONS
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.snackbar.Snackbar
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit


const val REQUEST_STORAGE_PERMISSION_IMAGE_UPLOAD = 100
const val REQUEST_STORAGE_PERMISSION_FILE_UPLOAD = 101
const val REQUEST_STORAGE_PERMISSION_VIDEO_UPLOAD = 102
const val REQUEST_CAMERA_PERMISSION_IMAGE_UPLOAD = 103
const val REQUEST_CAMERA_PERMISSION_VIDEO_UPLOAD = 104

private const val MAX_IMAGE_SELECTABLE = 10
private const val MAX_FILE_SELECTABLE = 10
private const val MAX_VIDEO_SELECTABLE = 10
private const val MAX_COLLAPSED_ATTACHMENT_OPTIONS = 5
private const val IMAGE_COUNT_SINGLE = 1
private const val IMAGE_COUNT_DOUBLE = 2

private const val ID_MENU_ITEM_POST: Int = 133

abstract class AmityBaseCreatePostFragment : AmityBaseFragment(),
    AmityCreatePostImageActionListener, AmityCreatePostFileActionListener,
    AmityAlertDialogFragment.IAlertDialogActionListener,
    SuggestionsVisibilityManager, QueryTokenReceiver {

    private var menuItemPost: MenuItem? = null
    protected val viewModel: AmityCreatePostViewModel by activityViewModels()
    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val postAttachmentOptionClickEventPublisher =
        PublishSubject.create<AmityPostAttachmentOptionItem>()
    private val postAttachmentOptionsAdapter =
        AmityPostAttachmentOptionsAdapter(postAttachmentOptionClickEventPublisher)
    private var mediaAdapter: AmityCreatePostMediaAdapter? = null
    private var itemDecor: RecyclerView.ItemDecoration? = null
    private var fileAdapter: AmityCreatePostFileAdapter? = null
    private val userMentionAdapter by lazy { AmityUserMentionAdapter() }
    private val userMentionPagingDataAdapter by lazy { AmityUserMentionPagingDataAdapter() }
    private lateinit var imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var videoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    
    private val searchDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var photoFile: File? = null
    protected var isLoading = false

    internal lateinit var binding: AmityFragmentPostCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consumeBackPress = true
        arguments?.getString(EXTRA_PARAM_COMMUNITY_ID)?.let { communityId ->
            viewModel.observeCommunity(communityId)
                .untilLifecycleEnd(this)
                .subscribe()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AmityFragmentPostCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        addPostEditTextListener()
        setupUserMention()
        setupPostAttachmentOptions()
        observeImageData()
        observeFileAttachments()
        addViewModelListener()
        registerMediaPickerResult()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuItemPost =
            menu.add(Menu.NONE, ID_MENU_ITEM_POST, Menu.NONE, getString(R.string.amity_save))
        menuItemPost?.setTitle(getPostMenuText())
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        updatePostMenu(isRightButtonActive())
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == ID_MENU_ITEM_POST) {
            handlePostMenuItemClick()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerMediaPickerResult() {
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                MAX_IMAGE_SELECTABLE
            )
        ) { uris ->
            val selectedCount = viewModel.getImages().value?.size ?: 0
            if (uris.isNotEmpty()) {
                val canSelect = Math.min(MAX_IMAGE_SELECTABLE - selectedCount, uris.size)
                addMedia(uris.subList(0, canSelect), PostMedia.Type.IMAGE)
            }
        }

        videoPickerLauncher = registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                MAX_VIDEO_SELECTABLE
            )
        ) { uris ->
            val selectedCount = viewModel.getImages().value?.size ?: 0
            if (uris.isNotEmpty()) {
                val canSelect = Math.min(MAX_VIDEO_SELECTABLE - selectedCount, uris.size)
                addMedia(uris.subList(0, canSelect), PostMedia.Type.VIDEO)
            }
        }
    }

    abstract fun handlePostMenuItemClick()

    private fun setupUserMention() {
        binding.etPost.apply {
            setSuggestionsVisibilityManager(this@AmityBaseCreatePostFragment)
            setQueryTokenReceiver(this@AmityBaseCreatePostFragment)
        }
        binding.recyclerViewUserMention.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUserMention.adapter = userMentionAdapter

        userMentionAdapter.setListener(object :
            AmityUserMentionAdapter.AmityUserMentionAdapterListener {
            override fun onClickUserMention(userMention: AmityUserMention) {
                insertUserMention(userMention)
            }
        })

        userMentionPagingDataAdapter.setListener(object :
            AmityUserMentionViewHolder.AmityUserMentionListener {
            override fun onClickUserMention(userMention: AmityUserMention) {
                insertUserMention(userMention)
            }
        })
    }

    private fun insertUserMention(userMention: AmityUserMention) {
        displaySuggestions(false)
        searchDisposable.clear()
        binding.etPost.insertMention(userMention)
    }

    private fun observeImageData() {
        Flowable.fromPublisher(
            viewModel.getImages()
                .toPublisher(viewLifecycleOwner)
        )
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .subscribe {
                setupImageAdapter()
                val imageCount = mediaAdapter?.itemCount ?: 0
                mediaAdapter!!.submitList(it.toMutableList())
                if (imageCount != it.size)
                    mediaAdapter?.notifyItemRangeChanged(0, it.size)
                handleButtonActiveInactiveBehavior()
            }
    }

    private fun observeFileAttachments() {
        setupFileAttachmentAdapter()
        Flowable.fromPublisher(
            viewModel.getFiles()
                .toPublisher(viewLifecycleOwner)
        )
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .subscribe {
                fileAdapter!!.submitList(it)
                handleButtonActiveInactiveBehavior()
            }
    }

    override fun onDestroy() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        if (!searchDisposable.isDisposed) {
            searchDisposable.dispose()
        }
        super.onDestroy()
    }

    protected fun showErrorMessage(error: String?) {
        error?.let { view?.showSnackBar(it) }
    }

    private fun getPostAttachmentOptions(): List<AmityPostAttachmentOptionItem> {
        var options =
            arguments?.getParcelableArrayList<AmityPostAttachmentOptionItem>(
                EXTRA_PARAM_POST_ATTACHMENT_OPTIONS
            )
        if (options == null) {
            options = arrayListOf(
                AmityPostAttachmentOptionItem.PHOTO,
                AmityPostAttachmentOptionItem.VIDEO,
                AmityPostAttachmentOptionItem.FILE
            )
        }
        return options
    }

    private fun setupPostAttachmentOptions() {
        binding.recyclerViewPostAttachmentsOptions.layoutManager = LinearLayoutPagerManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false,
            MAX_COLLAPSED_ATTACHMENT_OPTIONS
        )
        binding.recyclerViewPostAttachmentsOptions.adapter = postAttachmentOptionsAdapter
        refreshPostAttachmentOptions()
        postAttachmentOptionClickEventPublisher.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                handlePostAttachmentOptionClicked(it)
            }
            .doOnError { }
            .untilLifecycleEnd(this)
            .subscribe()

    }

    private fun handlePostAttachmentOptionClicked(item: AmityPostAttachmentOptionItem) {
        when (item) {
            is AmityPostAttachmentOptionItem.CAMERA -> {
                handleCamera()
            }

            is AmityPostAttachmentOptionItem.PHOTO -> {
                handleAddPhotos()
            }

            is AmityPostAttachmentOptionItem.VIDEO -> {
                handleAddVideos()
            }

            is AmityPostAttachmentOptionItem.FILE -> {
                handleAddFiles()
            }

            is AmityPostAttachmentOptionItem.EXPAND -> {
                showExpandedAttachmentOptions()
            }

            else -> {
                // do nothing
            }
        }
    }

    private fun refreshPostAttachmentOptions() {
        postAttachmentOptionsAdapter.submitList(getCollapsedAttachmentOptions())
    }

    private fun getCollapsedAttachmentOptions(): List<AmityPostAttachmentOptionItem> {
        var options = mutableListOf<AmityPostAttachmentOptionItem>().apply {
            addAll(getAvailableAttachmentOptions())
        }

        while (options.size < MAX_COLLAPSED_ATTACHMENT_OPTIONS - 1) {
            val blankSize = MAX_COLLAPSED_ATTACHMENT_OPTIONS - options.size
            for (i in 0..blankSize) {
                options.add(AmityPostAttachmentOptionItem.BLANK)
            }
        }
        if (options.size > MAX_COLLAPSED_ATTACHMENT_OPTIONS - 1) {
            options = options.subList(0, MAX_COLLAPSED_ATTACHMENT_OPTIONS - 1)
        }

        options.add(AmityPostAttachmentOptionItem.EXPAND.apply {
            isEnable = true
        })
        return options
    }

    open fun showComposeBar() {
        binding.recyclerViewPostAttachmentsOptions.visibility = View.VISIBLE
    }

    open fun hideComposeBar() {
        binding.recyclerViewPostAttachmentsOptions.visibility = View.GONE
    }

    private fun getAvailableAttachmentOptions(): List<AmityPostAttachmentOptionItem> {
        val requestedOptions = getPostAttachmentOptions()
        val presentingOptions = arrayListOf<AmityPostAttachmentOptionItem>()

        if (requestedOptions.isEmpty()) {
            hideComposeBar()
        } else {
            if (requestedOptions.contains(AmityPostAttachmentOptionItem.PHOTO)) {
                presentingOptions.add(AmityPostAttachmentOptionItem.PHOTO.apply {
                    isEnable = !viewModel.hasAttachments()
                            && !viewModel.isUploadingVideoMedia()
                            && !viewModel.isUploadedVideoMedia()
                })
            }

            if (requestedOptions.contains(AmityPostAttachmentOptionItem.PHOTO)
                || requestedOptions.contains(AmityPostAttachmentOptionItem.VIDEO)
            ) {
                presentingOptions.add(AmityPostAttachmentOptionItem.CAMERA.apply {
                    isEnable = !viewModel.hasAttachments()
                })
            }

            if (requestedOptions.contains(AmityPostAttachmentOptionItem.VIDEO)) {
                presentingOptions.add(AmityPostAttachmentOptionItem.VIDEO.apply {
                    isEnable = !viewModel.hasAttachments()
                            && !viewModel.isUploadingImageMedia()
                            && !viewModel.isUploadedImageMedia()
                })
            }

            if (requestedOptions.contains(AmityPostAttachmentOptionItem.FILE)) {
                presentingOptions.add(AmityPostAttachmentOptionItem.FILE.apply {
                    isEnable = !viewModel.hasImages()
                })
            }

            showComposeBar()
        }

        return presentingOptions
    }

    private fun showExpandedAttachmentOptions() {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        val options = getAvailableAttachmentOptions().map {
            val imageRes = if (it.isEnable) it.activeIcon else it.inactiveIcon
            val action: () -> Unit = {
                if (it.isEnable) {
                    when (it) {
                        is AmityPostAttachmentOptionItem.CAMERA -> {
                            handleCamera()
                            bottomSheet.dismiss()
                        }

                        is AmityPostAttachmentOptionItem.PHOTO -> {
                            handleAddPhotos()
                            bottomSheet.dismiss()
                        }

                        is AmityPostAttachmentOptionItem.VIDEO -> {
                            handleAddVideos()
                            bottomSheet.dismiss()
                        }

                        is AmityPostAttachmentOptionItem.FILE -> {
                            handleAddFiles()
                            bottomSheet.dismiss()
                        }

                        else -> {
                            bottomSheet.dismiss()
                        }
                    }
                }
            }
            BottomSheetMenuItem(imageRes, null, it.optionName, action)
        }
        if (options.isNotEmpty()) {
            bottomSheet.show(options)
        }
    }

    private fun handleCamera() {
        if (hasReachedSelectionLimit()) {
            view?.showSnackBar(getString(R.string.amity_create_post_max_image_selected_warning))
        } else {
            when {
                viewModel.isUploadingImageMedia() -> {
                    takePicture()
                }

                viewModel.isUploadingVideoMedia() -> {
                    takeVideo()
                }

                else -> {
                    showCameraInputSheet()
                }
            }
        }
    }

    private fun hasReachedSelectionLimit(): Boolean {
        val selectedImageCount = viewModel.getImages().value?.size ?: 0
        val selectedFileCount = viewModel.getFiles().value?.size ?: 0
        return selectedImageCount == MAX_IMAGE_SELECTABLE || selectedFileCount == MAX_FILE_SELECTABLE
    }

    private fun showCameraInputSheet() {
        val requestedOptions = getPostAttachmentOptions()
        if (requestedOptions.contains(AmityPostAttachmentOptionItem.PHOTO)
            && requestedOptions.contains(AmityPostAttachmentOptionItem.VIDEO)
        ) {
            val items = arrayListOf(
                AmityMenuItem(
                    AmityConstants.ID_SELECT_IMAGE_CAMERA,
                    getString(R.string.amity_general_photos)
                ),
                AmityMenuItem(
                    AmityConstants.ID_SELECT_VIDEO_CAMERA,
                    getString(R.string.amity_general_videos)
                )
            )
            val imageInputFragment = AmityBottomSheetListFragment.newInstance(items)
            imageInputFragment.setMenuItemClickListener(object : AmityMenuItemClickListener {
                override fun onMenuItemClicked(menuItem: AmityMenuItem) {
                    when (menuItem.id) {
                        AmityConstants.ID_SELECT_IMAGE_CAMERA -> takePicture()
                        AmityConstants.ID_SELECT_VIDEO_CAMERA -> takeVideo()
                    }
                    imageInputFragment.dismiss()
                }
            })
            imageInputFragment.show(parentFragmentManager, this.toString())
        } else if (requestedOptions.contains(AmityPostAttachmentOptionItem.PHOTO)) {
            takePicture()
        } else if (requestedOptions.contains(AmityPostAttachmentOptionItem.VIDEO)) {
            takeVideo()
        }
    }

    private fun handleAddFiles() {
        if (hasReachedSelectionLimit()) {
            view?.showSnackBar(getString(R.string.amity_create_post_max_image_selected_warning))
        } else {
            grantStoragePermission(REQUEST_STORAGE_PERMISSION_FILE_UPLOAD) { openFilePicker() }
        }
    }

    private fun handleAddPhotos() {
        if (hasReachedSelectionLimit()) {
            view?.showSnackBar(getString(R.string.amity_create_post_max_image_selected_warning))
        } else {
            openImagePicker()
        }
    }

    private fun handleAddVideos() {
        if (hasReachedSelectionLimit()) {
            view?.showSnackBar(getString(R.string.amity_create_post_max_image_selected_warning))
        } else {
            openVideoPicker()
        }
    }

    private fun openFilePicker() {
        val filesIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE)
        filesIntent.type = "*/*"
        startActivityForResult(filesIntent, AmityConstants.PICK_FILES)
    }

    private fun grantStoragePermission(requestCode: Int, onPermissionGrant: () -> Unit) {
        val requiredPermissions = emptyList<String>().toMutableList()
        requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        val hasRequiredPermission = requiredPermissions.fold(true) { acc, permission ->
            acc && hasPermission(permission)
        }
        if (hasRequiredPermission) {
            onPermissionGrant()
        } else {
            requestPermission(requiredPermissions.toTypedArray(), requestCode)
        }
    }

    private fun grantCameraPermission(requestCode: Int, onPermissionGrant: () -> Unit) {
        val requiredPermissions = mutableListOf(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        val hasRequiredPermission = requiredPermissions.fold(true) { acc, permission ->
            acc && hasPermission(permission)
        }
        if (hasRequiredPermission) {
            onPermissionGrant()
        } else {
            requestPermission(requiredPermissions.toTypedArray(), requestCode)
        }
    }

    open fun createPostMediaAdapter(): AmityCreatePostMediaAdapter {
        return AmityCreatePostMediaAdapter(this)
    }

    private fun setupImageAdapter() {
        if (mediaAdapter == null || mediaAdapter!!.itemCount == 0) {
            if (itemDecor != null) {
                binding.rvAttachment.removeItemDecoration(itemDecor!!)
            }

            val space = resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
            itemDecor = AmitySpacesItemDecoration(0, 0, 0, space)

            mediaAdapter = createPostMediaAdapter()
            binding.rvAttachment.addItemDecoration(itemDecor!!)
            val layoutManager = GridLayoutManager(context, 6)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (mediaAdapter?.itemCount()) {
                        IMAGE_COUNT_SINGLE -> 6 //in case single image it takes full row
                        IMAGE_COUNT_DOUBLE -> {
                            3 //in case two image it takes each item take half of the row
                        }

                        else -> 2
                    }
                }
            }

            binding.rvAttachment.layoutManager = layoutManager
            binding.rvAttachment.adapter = mediaAdapter
            binding.rvAttachment.itemAnimator?.changeDuration = 0
        }
    }

    private fun setupFileAttachmentAdapter() {
        if (fileAdapter == null || fileAdapter!!.itemCount == 0) {
            fileAdapter = AmityCreatePostFileAdapter(this)
            if (itemDecor != null) {
                binding.rvAttachment.removeItemDecoration(itemDecor!!)
            }
            val space = resources.getDimensionPixelSize(R.dimen.amity_padding_xs)

            itemDecor = AmitySpacesItemDecoration(0, 0, 0, space)
            binding.rvAttachment.addItemDecoration(itemDecor!!)
            binding.rvAttachment.layoutManager = LinearLayoutManager(context)
            binding.rvAttachment.adapter = fileAdapter
            binding.rvAttachment.itemAnimator?.changeDuration = 0
        }
    }

    private fun addPostEditTextListener() {
        binding.etPost.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.etPost.setDefaultPostHint()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handleButtonActiveInactiveBehavior()
            }
        })


    }

    abstract fun setToolBarText()

    abstract fun getPostMenuText(): String


    fun isEditMode(): Boolean {
        return viewModel.postId != null
    }

    fun showLoading() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.pbLoading.visibility = View.GONE
    }

    fun clearHint() {
        binding.etPost.hint = null
    }

    open fun isRightButtonActive(): Boolean {
        if (viewModel.hasPendingImageToUpload() || viewModel.hasFailedToUploadImages()) {
            return false
        } else if (viewModel.hasPendingFileToUpload() || viewModel.hasFailedToUploadFiles()) {
            return false
        } else if (isEmptyPostTest() && isEmptyFileAttachments() && isEmptyImages()) {
            return false
        }
        return true
    }

    private fun handleButtonActiveInactiveBehavior() {
        updatePostMenu(isRightButtonActive())
        refreshPostAttachmentOptions()
    }

    fun updatePostMenu(enabled: Boolean) {
        menuItemPost?.isEnabled = enabled
        val title = menuItemPost?.title
        title?.let {
            val spannableString = SpannableString(title)
            spannableString.setSpan(
                ForegroundColorSpan(
                    AmityOptionMenuColorUtil.getColor(
                        menuItemPost?.isEnabled
                            ?: false, requireContext()
                    )
                ), 0, spannableString.length, 0
            )
            menuItemPost?.title = spannableString
        }
    }

    private fun isEmptyFileAttachments(): Boolean {
        return fileAdapter == null || fileAdapter!!.itemCount == 0
    }

    private fun isEmptyImages(): Boolean {
        return mediaAdapter == null || mediaAdapter!!.itemCount == 0
    }

    private fun isEmptyPostTest(): Boolean {
        return binding.etPost.text.toString().trim().isEmpty()
    }

    override fun handleBackPress() {
        handleCancelPost()
    }

    private fun handleCancelPost() {
        if (hasDraft()) {
            showExitConfirmationDialog()
        } else {
            backPressFragment()
        }
    }

    private fun showExitConfirmationDialog() {
        val exitConfirmationDialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_discard_post_title, R.string.amity_discard_post_message,
                R.string.amity_discard, R.string.amity_cancel
            )
        exitConfirmationDialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG);
        exitConfirmationDialogFragment.listener = this
    }

    private fun hasDraft(): Boolean {
        return if (isEditMode()) {
            viewModel.hasUpdateOnPost(binding.etPost.text.toString().trim())
        } else {
            !(isEmptyFileAttachments() && isEmptyImages() && isEmptyPostTest())
        }
    }

    private fun openImagePicker() {
        if (canSelectImage()) {
            imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
    
    private fun getSelectedImageCount(): Int {
        return viewModel.getImages().value?.size ?: 0
    }

    private fun canSelectImage(): Boolean {
        val selectedImageCount = viewModel.getImages().value?.size ?: 0
        if (selectedImageCount == MAX_IMAGE_SELECTABLE) {
            view?.showSnackBar(
                getString(R.string.amity_create_post_max_image_selected_warning)
            )
            return false
        }
        return true
    }

    private fun openVideoPicker() {
        val selectedVideoCount = viewModel.getImages().value?.size ?: 0
        if (selectedVideoCount == MAX_VIDEO_SELECTABLE) {
            view?.showSnackBar(getString(R.string.amity_create_post_max_image_selected_warning))
        } else {
            videoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            view?.showSnackBar("Permission denied", Snackbar.LENGTH_SHORT)
            return
        }
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION_IMAGE_UPLOAD -> openImagePicker()
            REQUEST_STORAGE_PERMISSION_VIDEO_UPLOAD -> openVideoPicker()
            REQUEST_STORAGE_PERMISSION_FILE_UPLOAD -> openFilePicker()
            REQUEST_CAMERA_PERMISSION_IMAGE_UPLOAD -> takePicture()
            REQUEST_CAMERA_PERMISSION_VIDEO_UPLOAD -> takeVideo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) when (requestCode) {

            AmityConstants.PICK_FILES -> {
                if (data != null)
                    addFileAttachments(data)
            }

            AmityConstants.CAPTURE_IMAGE -> {
                photoFile?.also {
                    setupImageAdapter()
                    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    val contentUri = Uri.fromFile(photoFile)
                    mediaScanIntent.data = contentUri
                    activity?.sendBroadcast(mediaScanIntent)
                    val images = viewModel.addMedia(listOf(contentUri), PostMedia.Type.IMAGE)
                    uploadMedia(images)
                }
            }

            AmityConstants.CAPTURE_VIDEO -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    data?.data
                } else {
                    photoFile?.let(Uri::fromFile)
                }?.let { contentUri ->
                    setupImageAdapter()
                    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    mediaScanIntent.data = contentUri
                    activity?.sendBroadcast(mediaScanIntent)
                    val videos = viewModel.addMedia(listOf(contentUri), PostMedia.Type.VIDEO)
                    uploadMedia(videos)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun addViewModelListener() {
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.FAILED_TO_UPLOAD_IMAGE -> {
                    showImageUploadFailedDialog()
                }

                AmityEventIdentifier.FAILED_TO_UPLOAD_FILES -> {
                    showAttachmentUploadFailedDialog()
                }

                AmityEventIdentifier.FILE_UPLOAD_MAX_LIMIT_EXCEED -> {
                    showErrorMessage(R.string.amity_attachment_count_limit_exceed)
                }

                AmityEventIdentifier.CREATE_POST_IMAGE_REMOVED -> {
                    if (event.dataObj as Int > 0)
                        mediaAdapter?.notifyItemRangeChanged(0, event.dataObj as Int)
                }

                else -> {
                }
            }
        }
    }

    private fun showImageUploadFailedDialog() {
        val dialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_upload_incomplete, R.string.amity_image_upload_failed_message,
                null, R.string.amity_ok
            )
        dialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG);
        dialogFragment.setAlertDialogActionListener(object :
            AmityAlertDialogFragment.IAlertDialogActionListener {
            override fun onClickPositiveButton() {
                dialogFragment.dismiss()
            }

            override fun onClickNegativeButton() {

            }

        })
    }

    private fun showAttachmentUploadFailedDialog() {
        val dialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_upload_incomplete,
                R.string.amity_attachment_upload_failed_message,
                null,
                R.string.amity_ok
            )
        dialogFragment.show(childFragmentManager, AmityAlertDialogFragment.TAG);
        dialogFragment.setAlertDialogActionListener(object :
            AmityAlertDialogFragment.IAlertDialogActionListener {
            override fun onClickPositiveButton() {
                dialogFragment.dismiss()
            }

            override fun onClickNegativeButton() {

            }

        })
    }

    private fun maxAttachmentCountExceed(data: Intent): Boolean {
        val currentAttachmentCount = viewModel.getFiles().value?.size ?: 0
        return (null != data.clipData && (currentAttachmentCount + data.clipData!!.itemCount) > MAX_FILE_SELECTABLE)
                || (currentAttachmentCount + 1) > MAX_FILE_SELECTABLE
    }

    private fun addMedia(uris: List<Uri>, mediaType: PostMedia.Type) {
        setupImageAdapter()
        if (uris.isNotEmpty()) {
            val postMediaList = viewModel.addMedia(uris, mediaType)
            uploadMedia(postMediaList)
        }
    }

    private fun uploadMedia(mediaList: List<PostMedia>) {
        viewModel.uploadMediaList(mediaList)
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun addFileAttachments(data: Intent) {
        setupFileAttachmentAdapter()
        if (maxAttachmentCountExceed(data)) {
            showErrorMessage(R.string.amity_attachment_count_limit_exceed)
        } else {
            var maxLimitExceedError = false
            val fileUriList = mutableListOf<Uri>()
            var addedFiles: MutableList<AmityFileAttachment> = arrayListOf()
            if (null != data.clipData) {
                for (i in 0 until data.clipData!!.itemCount) {
                    val fileUri = data.clipData!!.getItemAt(i).uri
                    val fileAttachment = getFileAttachment(fileUri)
                    if (exceedMaxFileSize(fileAttachment.size)) {
                        maxLimitExceedError = true
                    } else {
                        fileUriList.add(fileUri)
                        addedFiles.add(fileAttachment)
                    }
                }
            } else {
                val fileUri = data.data!!
                val fileAttachment = getFileAttachment(fileUri)
                if (exceedMaxFileSize(fileAttachment.size)) {
                    maxLimitExceedError = true
                } else {
                    fileUriList.add(fileUri)
                    addedFiles.add(fileAttachment)
                }
            }
            if (maxLimitExceedError) {
                showMaxLimitExceedError(addedFiles)
            } else {
                uploadFileAttachments(addedFiles)
            }
        }
    }

    private fun showMaxLimitExceedError(addedFiles: MutableList<AmityFileAttachment>) {
        val dialogFragment = AmityAlertDialogFragment
            .newInstance(
                R.string.amity_file_max_limit_exceed_title,
                R.string.amity_file_max_limit_exceed_message,
                null,
                R.string.amity_ok
            )
        dialogFragment.setAlertDialogActionListener(object :
            AmityAlertDialogFragment.IAlertDialogActionListener {
            override fun onClickPositiveButton() {
                dialogFragment.dismiss()
                uploadFileAttachments(addedFiles)
            }

            override fun onClickNegativeButton() {
                uploadFileAttachments(addedFiles)
            }

        })
        val fragmentTransaction = childFragmentManager.beginTransaction();
        fragmentTransaction.add(dialogFragment, AmityAlertDialogFragment.TAG)
            .commitAllowingStateLoss()

    }

    private fun uploadFileAttachments(addedFiles: MutableList<AmityFileAttachment>) {
        val files = viewModel.addFiles(addedFiles)
        if (files.size != addedFiles.size) {
            showDuplicateFilesMessage()
        }
        files.forEach { fileAttachment ->
            val disposable = viewModel.uploadFile(fileAttachment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .untilLifecycleEnd(this)
                .doOnNext {
                    viewModel.updateFileUploadStatus(fileAttachment, it)
                }.doOnError { }.subscribe()
            compositeDisposable.add(disposable)
        }
    }

    private fun showDuplicateFilesMessage() {
        view?.showSnackBar(getString(R.string.amity_duplicate_files))
    }


    private fun showErrorMessage(@StringRes error: Int) {
        view?.showSnackBar(getString(error))
    }

    private fun exceedMaxFileSize(size: Long): Boolean {
        return (size > AmityConstants.FILE_SIZE_GB)
    }

    private fun takePicture() {
        grantCameraPermission(REQUEST_CAMERA_PERMISSION_IMAGE_UPLOAD) { dispatchTakePictureIntent() }
    }

    private fun takeVideo() {
        grantCameraPermission(REQUEST_CAMERA_PERMISSION_VIDEO_UPLOAD) { dispatchTakeVideoIntent() }
    }

    private fun dispatchTakePictureIntent() {
        if (canSelectImage()) {
            if (activity?.applicationContext != null) {
                photoFile =
                    AmityCameraUtil.createImageFile(requireActivity().applicationContext)
                photoFile?.also {
                    val photoUri =
                        AmityCameraUtil.createPhotoUri(requireActivity().applicationContext, it)
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        // Ensure that there's a camera activity to handle the intent
                        takePictureIntent.resolveActivity(requireActivity().packageManager)
                            ?.also {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                startActivityForResult(
                                    takePictureIntent,
                                    AmityConstants.CAPTURE_IMAGE
                                )
                            }
                    }
                }
            }
        }
    }

    private fun dispatchTakeVideoIntent() {
        if (activity?.applicationContext != null) {
            photoFile = AmityCameraUtil.createVideoFile(requireActivity().applicationContext)
            photoFile?.also {
                val photoUri =
                    AmityCameraUtil.createVideoUri(requireActivity().applicationContext, it)
                Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        }
                        startActivityForResult(takePictureIntent, AmityConstants.CAPTURE_VIDEO)
                    }
                }
            }
        }
    }

    private fun getFileAttachment(uri: Uri): AmityFileAttachment {
        val fileName: String = AmityFileUtils.getName(requireActivity().contentResolver, uri)!!
        val fileSize: Long =
            AmityFileUtils.getSize(requireActivity().contentResolver, uri).toLong()
        val mimeType = AmityFileUtils.getMimeType(requireActivity().contentResolver, uri)
        return AmityFileAttachment(
            null,
            UUID.randomUUID().toString(),
            fileName,
            fileSize,
            uri,
            AmityFileUtils.humanReadableByteCount(fileSize, true)!!,
            mimeType!!,
            FileUploadState.PENDING, 0
        )
    }

    override fun onRemoveImage(postMedia: PostMedia, position: Int) {
        viewModel.removeMedia(postMedia)
        handleButtonActiveInactiveBehavior()
    }

    override fun onRemoveFile(file: AmityFileAttachment, position: Int) {
        viewModel.removeFile(file)
    }

    override fun onClickPositiveButton() {
        consumeBackPress = false
        viewModel.discardPost()
        activity?.onBackPressed()
    }

    @ExperimentalPagingApi
    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        if (queryToken.tokenString.startsWith(AmityUserMention.CHAR_MENTION)) {
            searchDisposable.clear()
            val disposable = if (viewModel.community?.isPublic() == false) {
                binding.recyclerViewUserMention.swapAdapter(userMentionPagingDataAdapter, true)
                viewModel.searchCommunityUsersMention(viewModel.community?.getCommunityId()!!,
                    queryToken.keywords, onResult = {
                        userMentionPagingDataAdapter.submitData(lifecycle, it)
                        displaySuggestions(true)
                    }).subscribe()
            } else {
                binding.recyclerViewUserMention.swapAdapter(userMentionAdapter, true)
                viewModel.searchUsersMention(queryToken.keywords, onResult = {
                    userMentionAdapter.submitData(lifecycle, it)
                    displaySuggestions(true)
                }).subscribe()
            }
            searchDisposable.add(disposable)
        } else {
            displaySuggestions(false)
        }
        return mutableListOf()
    }

    override fun onClickNegativeButton() {

    }

    override fun displaySuggestions(display: Boolean) {
        if (display) {
            binding.recyclerViewUserMention.visibility = View.VISIBLE
        } else {
            binding.recyclerViewUserMention.visibility = View.GONE
        }
    }

    override fun isDisplayingSuggestions(): Boolean {
        return binding.recyclerViewUserMention.visibility == View.VISIBLE
    }
}

