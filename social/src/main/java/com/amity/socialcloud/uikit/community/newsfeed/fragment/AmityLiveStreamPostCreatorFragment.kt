package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.video.AmityStreamBroadcaster
import com.amity.socialcloud.sdk.video.AmityStreamBroadcasterConfiguration
import com.amity.socialcloud.sdk.video.StreamBroadcaster
import com.amity.socialcloud.sdk.video.model.AmityBroadcastResolution
import com.amity.socialcloud.sdk.video.model.AmityStreamBroadcasterState
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentLiveStreamPostCreatorBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionPagingDataAdapter
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityUserMentionViewHolder
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityLiveStreamPostCreatorViewModel
import com.amity.socialcloud.uikit.community.views.createpost.AmityPostComposeView
import com.bumptech.glide.Glide
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.operators.flowable.FlowableInterval
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


private const val REQUEST_LIVE_STREAM_CAMERA_PERMISSIONS = 20001
private const val REQUEST_LIVE_STREAM_STORAGE_PERMISSIONS = 20002


class AmityLiveStreamPostCreatorFragment : RxFragment() {


    private lateinit var binding: AmityFragmentLiveStreamPostCreatorBinding

    private val viewModel: AmityLiveStreamPostCreatorViewModel by viewModels()
    
    private var durationDisposable: Disposable? = null
    private var streamBroadcaster: StreamBroadcaster? = null
    private var broadcasterConfig = AmityStreamBroadcasterConfiguration.Builder()
        .setOrientation(Configuration.ORIENTATION_PORTRAIT)
        .setResolution(AmityBroadcastResolution.HD_720P)
        .build()

    private var communityId: String? = null
    private var duration = 0L
    private var streamBroadcasterState: AmityStreamBroadcasterState =
        AmityStreamBroadcasterState.IDLE()
    
    private val descriptionUserMentionAdapter by lazy { AmityUserMentionAdapter() }
    private val descriptionUserMentionPagingDataAdapter by lazy { AmityUserMentionPagingDataAdapter() }
    private val searchDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    private lateinit var imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentLiveStreamPostCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        communityId = arguments?.getString(EXTRA_PARAM_COMMUNITY_ID) ?: ""
        viewModel.communityId = communityId
        viewModel.observeCommunity(communityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .untilLifecycleEnd(this)
                .subscribe()
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStreamBroadcaster()
        registerImagePickerResult()
    }

    private fun grantCameraPermissions(requestCode: Int, onPermissionGrant: () -> Unit) {
        if (hasPermission(Manifest.permission.CAMERA) && hasPermission(Manifest.permission.RECORD_AUDIO)) {
            onPermissionGrant()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ), requestCode
            )
        }
    }

    private fun grantStoragePermission(requestCode: Int, onPermissionGrant: () -> Unit) {
        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            onPermissionGrant()
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
        }
    }

    private fun registerImagePickerResult() {
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    uploadThumbnail(uri)
            }
        }
    }

    @ExperimentalPagingApi
    private fun setupStreamBroadcaster() {
        grantCameraPermissions(REQUEST_LIVE_STREAM_CAMERA_PERMISSIONS) {
            setupView()
            initBroadcaster()
        }
    }

    private fun initBroadcaster() {
        streamBroadcaster = AmityStreamBroadcaster.Builder(binding.amityCamera)
            .setConfiguration(broadcasterConfig)
            .build()
        streamBroadcaster?.startPreview()
        subscribeBroadcastStatus()
    }

    @ExperimentalPagingApi
    private fun setupView() {
        binding.descriptionEdittext.run {
            style.apply {
                hint = R.string.amity_video_stream_description_hint
                mentionColor = R.color.amityColorAthensGray
            }.let { livestreamStyle ->
                setViewStyle(livestreamStyle)
            }
        }
        binding.iconSwapCam.setOnClickListener { streamBroadcaster?.switchCamera() }
        binding.iconPublishedSwapCam.setOnClickListener { streamBroadcaster?.switchCamera() }
        binding.iconClose.setOnClickListener { activity?.finish() }
        binding.togglePublish.setOnClickListener { startStreaming() }
        binding.iconAddThumbnail.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                grantStoragePermission(
                    REQUEST_LIVE_STREAM_STORAGE_PERMISSIONS
                ) { openImagePicker() }
            } else {
                openImagePicker()
            }
        }
        binding.thumbnailContainer.setOnClickListener {
            presentEditThumbnailDialog()
        }
        setupProfile()
        setupUserMention()
    }

    private fun setupProfile() {
        viewModel.getTargetProfile { title, imageUrl ->
            if (title != null || imageUrl != null) {
                binding.communityContainer.visibility = View.VISIBLE
                binding.communityTitle.text = title
                Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.amity_ic_default_community_avatar_circular)
                    .into(binding.communityAvatar)
            }
        }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun onStreamConnected() {
        binding.iconPublishedSwapCam.visibility = View.VISIBLE
        binding.toggleStop.visibility = View.VISIBLE
        binding.togglePublish.visibility = View.GONE
        binding.toggleStop.setOnClickListener { showStopStreamingDialog() }
    }

    private fun showStopStreamingDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.amity_video_stream_stop_confirmation_title)
            .setMessage(R.string.amity_video_stream_stop_confirmation_description)
            .setNeutralButton(R.string.amity_cancel) { _, _ -> }
            .setNegativeButton(R.string.amity_general_stop) { _, _ -> stopStreaming() }
            .show()
    }

    @ExperimentalPagingApi
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            showPermissionErrorDialog()
            return
        }
        when (requestCode) {
            REQUEST_LIVE_STREAM_CAMERA_PERMISSIONS -> {
                setupView()
                initBroadcaster()
            }
            REQUEST_LIVE_STREAM_STORAGE_PERMISSIONS -> {
                openImagePicker()
            }
        }
    }

    private fun showPermissionErrorDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle(R.string.amity_general_error)
            .setMessage(R.string.amity_general_error_permission)
            .setNeutralButton(R.string.amity_general_understand) { _, _ -> activity?.finish() }
            .show()
    }

    private fun subscribeBroadcastStatus() {
        streamBroadcaster?.getStateFlowable()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnNext {
                streamBroadcasterState = it
                when (it) {
                    is AmityStreamBroadcasterState.CONNECTING -> {
                        showReconnectLabel()
                    }
                    is AmityStreamBroadcasterState.DISCONNECTED -> {
                        showDisconnectError()
                    }
                    is AmityStreamBroadcasterState.CONNECTED -> {
                        onStreamConnected()
                    }
                    else -> {}
                }
            }
            ?.subscribe()
    }

    private fun startStreaming() {
        binding.creationContainer.visibility = View.GONE
        binding.togglePublish.visibility = View.GONE
        showReconnectLabel()
        startCounting()
        viewModel.createLiveStreamingPost(title = getVideoTitle(),
            description = getVideoDescription(),
            onCreateCompleted = { streamId ->
                streamId?.let {
                    streamBroadcaster?.startPublish(it)
                }
            },
            onCreateFailed = { showErrorDialog() },
            descriptionUserMentions = binding.descriptionEdittext.getUserMentions()
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun stopStreaming() {
        disposeStream()
        val resultIntent = Intent()
        resultIntent.putExtra(
            EXTRA_PARAM_POST_ID,
            viewModel.createdPostId
        )
        activity?.setResult(Activity.RESULT_OK, resultIntent)
        activity?.finish()
    }

    override fun onDestroy() {
        streamBroadcaster?.stopPreview()
        disposeStream()
        super.onDestroy()
    }

    private fun disposeStream() {
        if (durationDisposable?.isDisposed == false) {
            streamBroadcaster?.stopPublish()
            durationDisposable?.dispose()
        }
    }

    private fun showErrorDialog() {

    }
    
    private fun showErrorDialog(title: String, message: String) {
        AmityAlertDialogUtil.showDialog(requireContext(), title, message,
                resources.getString(R.string.amity_done),
                null,
                DialogInterface.OnClickListener { dialog, which ->
                    AmityAlertDialogUtil.checkConfirmDialog(
                            isPositive = which,
                            confirmed = dialog::cancel
                    )
                })
    }

    private fun getVideoDescription(): String {
        val inputText = binding.descriptionEdittext.text.toString()
        return if (inputText.isNotEmpty()) inputText else " "
    }

    private fun getVideoTitle(): String {
        val inputText = binding.titleEdittext.text.toString()
        return if (inputText.isNotEmpty()) inputText else " "
    }

    private fun showReconnectLabel() {
        binding.liveLabel.text = getString(R.string.amity_video_stream_connecting)
        binding.liveLabel.visibility = View.VISIBLE
    }

    private fun showDisconnectError() {
//        showToast("Rtmp disconnected")
    }

    private fun startCounting() {
        durationDisposable = FlowableInterval(0, 1, TimeUnit.SECONDS, Schedulers.io())
            .filter { streamBroadcasterState is AmityStreamBroadcasterState.CONNECTED }
            .map { duration++ }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                val durationMs = it * 1000
                val second = durationMs / 1000 % 60
                val min = durationMs / 1000 / 60
                binding.liveLabel.text = getString(
                    R.string.amity_v4_create_livestream_live_timer_status,
                    min.format(),
                    second.format()
                )
            }
            .subscribe()
    }

    private fun Long.format(): String {
        return String.format("%02d", this)
    }

    private fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val status = ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            )
            return (status == PackageManager.PERMISSION_GRANTED)
        }
        return true
    }

    private fun openImagePicker() {
        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadThumbnail(uri: Uri) {
        viewModel.uploadThumbnail(uri = uri,
            onUploading = { presentUploadingThumbnail(uri) },
            onUploadCompleted = { presentUploadedThumbnail(uri) },
            onUploadFailed = { presentUploadFailedThumbnail() })
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun presentUploadingThumbnail(uri: Uri) {
        binding.thumbnailContainer.visibility = View.VISIBLE
        binding.thumbnailProgressbar.visibility = View.VISIBLE
        binding.iconAddThumbnail.visibility = View.GONE
        binding.togglePublish.isEnabled = false
        binding.togglePublish.isClickable = false
        binding.togglePublish.alpha = 0.4f
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .dontAnimate()
            .into(binding.thumbnailImageview)
    }

    private fun presentUploadedThumbnail(uri: Uri) {
        binding.thumbnailProgressbar.visibility = View.GONE
        binding.togglePublish.isEnabled = true
        binding.togglePublish.isClickable = true
        binding.togglePublish.alpha = 1f
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .dontAnimate()
            .into(binding.thumbnailImageview)
    }

    private fun presentUploadFailedThumbnail() {
        removeThumbnail()
        binding.root.showSnackBar(msg = getString(R.string.amity_image_upload_error))
    }

    private fun removeThumbnail() {
        binding.thumbnailContainer.visibility = View.GONE
        binding.thumbnailProgressbar.visibility = View.GONE
        binding.iconAddThumbnail.visibility = View.VISIBLE
        binding.togglePublish.isEnabled = true
        binding.togglePublish.isClickable = true
        binding.togglePublish.alpha = 1f
        viewModel.removeThumbnail()
    }

    private fun presentEditThumbnailDialog() {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        val options =
            arrayListOf(
                BottomSheetMenuItem(
                    titleResId = R.string.amity_video_stream_thumbnail_change,
                    action = {
                        openImagePicker()
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    colorResId = R.color.amityColorRed,
                    titleResId = R.string.amity_video_stream_thumbnail_remove,
                    action = {
                        removeThumbnail()
                        bottomSheet.dismiss()
                    }
                )
            )

        bottomSheet.show(options)
    }
    
    @ExperimentalPagingApi
    private fun setupUserMention() {
        setupUserMentionComposeView(COMPOSE.DESCRIPTION)
    }
    
    @ExperimentalPagingApi
    private fun setupUserMentionComposeView(composeView: COMPOSE) {
        val suggestionView: RecyclerView = getCurrentSuggestionView(composeView)
        val suggestionAdapter = getSuggestionAdapter(composeView)
        val suggestionPagingAdapter = getSuggestionPagingAdapter(composeView)
        getCurrentComposeView(composeView).apply {
            setSuggestionsVisibilityManager(object: SuggestionsVisibilityManager{
                override fun displaySuggestions(display: Boolean) {
                    displaySuggestions(suggestionView, display)
                }
                
                override fun isDisplayingSuggestions(): Boolean {
                    return isDisplayingSuggestions(suggestionView)
                }
            })
            setQueryTokenReceiver {
                onQueryReceived(composeView, it)
            }
        }
        suggestionView.layoutManager = LinearLayoutManager(requireContext())
        suggestionView.adapter = suggestionAdapter
    
        suggestionAdapter.setListener(object :
                AmityUserMentionAdapter.AmityUserMentionAdapterListener {
            override fun onClickUserMention(userMention: AmityUserMention) {
                insertUserMention(composeView, userMention)
            }
        })
    
        suggestionPagingAdapter.setListener(object :
                AmityUserMentionViewHolder.AmityUserMentionListener {
            override fun onClickUserMention(userMention: AmityUserMention) {
                insertUserMention(composeView, userMention)
            }
        })
    }

    private fun insertUserMention(composeView: COMPOSE, userMention: AmityUserMention) {
        displaySuggestions(getCurrentSuggestionView(composeView), false)
        searchDisposable.clear()
        getCurrentComposeView(composeView).insertMention(userMention)
    }

    private fun displaySuggestions(recyclerView: RecyclerView, display: Boolean) {
        if (display) {
            recyclerView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.GONE
        }
    }

    private fun isDisplayingSuggestions(recyclerView: RecyclerView): Boolean {
        return recyclerView.visibility == View.VISIBLE
    }

    @ExperimentalPagingApi
    private fun onQueryReceived(composeView: COMPOSE, queryToken: QueryToken): MutableList<String> {
        val suggestionView: RecyclerView = getCurrentSuggestionView(composeView)
        val suggestionAdapter = getSuggestionAdapter(composeView)
        val suggestionPagingAdapter = getSuggestionPagingAdapter(composeView)
        if (queryToken.tokenString.startsWith(AmityUserMention.CHAR_MENTION)) {
            searchDisposable.clear()
            val disposable = if (viewModel.community?.isPublic() == false) {
                suggestionView.swapAdapter(suggestionPagingAdapter, true)
                viewModel.searchCommunityUsersMention(viewModel.community?.getCommunityId()!!,
                        queryToken.keywords, onResult = {
                    suggestionPagingAdapter.submitData(lifecycle, it)
                    displaySuggestions(suggestionView, true)
                }).subscribe()
            } else {
                suggestionView.swapAdapter(suggestionAdapter, true)
                viewModel.searchUsersMention(queryToken.keywords, onResult = {
                    suggestionAdapter.submitData(lifecycle, it)
                    displaySuggestions(suggestionView, true)
                }).subscribe()
            }
            searchDisposable.add(disposable)
        } else {
            displaySuggestions(suggestionView, false)
        }
        return mutableListOf()
    }

    private fun getCurrentComposeView(composeView: COMPOSE): AmityPostComposeView {
        return when(composeView) {
            COMPOSE.DESCRIPTION -> binding.descriptionEdittext
        }
    }
    
    private fun getCurrentSuggestionView(composeView: COMPOSE): RecyclerView {
        return when(composeView) {
            COMPOSE.DESCRIPTION -> binding.recyclerViewDescriptionUserMention
        }
    }
    
    private fun getSuggestionAdapter(composeView: COMPOSE): AmityUserMentionAdapter {
        return when(composeView) {
            COMPOSE.DESCRIPTION -> descriptionUserMentionAdapter
        }
    }
    
    private fun getSuggestionPagingAdapter(composeView: COMPOSE): AmityUserMentionPagingDataAdapter {
        return when(composeView) {
            COMPOSE.DESCRIPTION -> descriptionUserMentionPagingDataAdapter
        }
    }
    
    internal enum class COMPOSE { DESCRIPTION }

    class Builder internal constructor() {
        private var communityId: String? = null

        fun build(): AmityLiveStreamPostCreatorFragment {
            return AmityLiveStreamPostCreatorFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_COMMUNITY_ID, this@Builder.communityId)
                }
            }
        }

        internal fun onMyFeed(): Builder {
            return this
        }

        internal fun communityId(communityId: String): Builder {
            this.communityId = communityId
            return this
        }

    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    
        private const val TITLE_CHAR_LIMIT = 30
    }

}

const val EXTRA_PARAM_COMMUNITY_ID = "EXTRA_PARAM_COMMUNITY_ID"
const val EXTRA_PARAM_POST_ID = "EXTRA_PARAM_POST_ID"
