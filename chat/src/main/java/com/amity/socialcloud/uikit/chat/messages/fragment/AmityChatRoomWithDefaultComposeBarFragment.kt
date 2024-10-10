package com.amity.socialcloud.uikit.chat.messages.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityFragmentChatWithDefaultComposeBarBinding
import com.amity.socialcloud.uikit.chat.messages.adapter.AmityMessagePagingAdapter
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityChatRoomEssentialViewModel
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityMessageListViewModel
import com.amity.socialcloud.uikit.chat.util.MessageType
import com.amity.socialcloud.uikit.common.base.AmityPickerFragment
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.components.AmityAudioRecorderListener
import com.amity.socialcloud.uikit.common.components.AmityMessageListListener
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AmityChatRoomWithDefaultComposeBarFragment : AmityPickerFragment(),
    AmityAudioRecorderListener, AmityMessageListListener {

    private lateinit var essentialViewModel: AmityChatRoomEssentialViewModel

    private val messageListViewModel: AmityMessageListViewModel by viewModels()
    private lateinit var mAdapter: AmityMessagePagingAdapter
    private lateinit var binding: AmityFragmentChatWithDefaultComposeBarBinding
    private var msgSent = false
    private var viewHolderListener: AmityMessagePagingAdapter.CustomViewHolderListener? = null
    var recordPermissionGranted = false
    private var messageListDisposable: Disposable? = null
    private var currentCount = 0
    private var isImagePermissionGranted = false
    private var isReachBottom = true
    private lateinit var imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private val requiredPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_MEDIA_AUDIO
        )
    }

    private val recordPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = true
            permissions.entries.forEach {
                if (!it.value) {
                    isGranted = false
                }
            }
            recordPermissionGranted = isGranted
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        essentialViewModel =
            ViewModelProvider(requireActivity()).get(AmityChatRoomEssentialViewModel::class.java)
        messageListViewModel.channelID = essentialViewModel.channelId
        viewHolderListener = essentialViewModel.customViewHolder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_chat_with_default_compose_bar,
            container,
            false
        )
        binding.viewModel = messageListViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getChannelType()
        initToolBar()
        initRecyclerView()
        setRecorderTouchListener()
        setupComposebar()
        observeViewModelEvents()
        initMessageLoader()
        registerImagePickerResult()
//        observeRefreshStatus()
//        observeConnectionStatus()

    }

    private fun setupComposebar() {
        binding.apply {
            etMessage.setShape(
                null, null, null, null,
                R.color.amityColorBase, R.color.amityColorBase, AmityColorShade.SHADE4
            )
            recordBackground.setShape(
                null, null, null, null,
                R.color.amityColorBase, R.color.amityColorBase, AmityColorShade.SHADE4
            )
            etMessage.setOnClickListener {
                messageListViewModel.showComposeBar.set(false)
            }
            etMessage.setOnFocusChangeListener { _, _ ->
                messageListViewModel.showComposeBar.set(false)
            }
            recorderView.setAudioRecorderListener(this@AmityChatRoomWithDefaultComposeBarFragment)
        }
    }

    private fun initMessageLoader() {
//        messageListViewModel.messageLoader.load()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnComplete { hideLoadingView() }
//            .untilLifecycleEnd(this)
//            .subscribe()
    }

    /*
    private fun observeRefreshStatus() {
        messageListViewModel.observeRefreshStatus { resetMessageLoader() }
            .doOnError { }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun observeConnectionStatus() {
        messageListViewModel.observeConnectionStatus(
            onDisconnected = { presentDisconnectedView() },
            onReconnected = { presentReconnectedView() })
            .doOnError { }
            .untilLifecycleEnd(this)
            .subscribe()
    }
     */
    
    private fun registerImagePickerResult() {
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                AmityConstants.MAX_SELECTION_COUNT
            )
        ) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            for (uri in uris) {
                disposable.add(messageListViewModel.sendImageMessage(uri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        msgSent = true
                    }.doOnError {
                        msgSent = false
                    }.subscribe()
                )
            }
        }
    }

    private fun presentDisconnectedView() {
        if (essentialViewModel.enableConnectionBar) {
            binding.connectionView.visibility = View.VISIBLE
            binding.connectionTexview.setText(R.string.amity_no_internet)
            binding.connectionTexview.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.amityColorGrey
                )
            )
        }
    }

    private fun presentReconnectedView() {
        if (essentialViewModel.enableConnectionBar) {
            binding.connectionView.visibility = View.GONE
        }
    }

    private fun presentChatRefreshLoadingView() {
        binding.loadingView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.amityTranslucentBackground
            )
        )
        binding.loadingView.visibility = View.VISIBLE
    }

    private fun resetMessageLoader() {
        messageListViewModel.channelID = essentialViewModel.channelId
//        messageListViewModel.messageLoader.load()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { presentChatRefreshLoadingView() }
//            .doOnComplete { hideLoadingView() }
//            .untilLifecycleEnd(this)
//            .subscribe()
    }

    override fun onMessageClicked(position: Int) {
        binding.rvChatList.scrollToPosition(position)
    }

    private fun observeScrollingState(layoutManager: LinearLayoutManager) {
        binding.rvChatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItem = layoutManager.findLastVisibleItemPosition()
                mAdapter.firstCompletelyVisibleItem = firstVisibleItem
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    isReachBottom = true
                    mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
                } else if (isReachBottom) {
                    isReachBottom = false
                }
            }
        })
        latestMessageObserver.apply { mAdapter.registerAdapterDataObserver(this) }
    }


    override fun onResume() {
        super.onResume()
        setUpBackPress()
    }

    private fun setUpBackPress() {

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (messageListViewModel.showComposeBar.get()) {
                    messageListViewModel.showComposeBar.set(false)
                } else {
                    requireActivity().finish()
                }
            }
        })
    }

    private fun getChannelType() {
        disposable.add(messageListViewModel.getChannelType().take(1).subscribe { ekoChannel ->
            if (ekoChannel.getChannelType() == AmityChannel.Type.STANDARD) {
                binding.chatToolBar.ivAvatar.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.amity_ic_group
                    )
                )
            } else {
                binding.chatToolBar.ivAvatar.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.amity_ic_user
                    )
                )
            }

            if (ekoChannel.getChannelType() == AmityChannel.Type.CONVERSATION) {
                disposable.add(
                    messageListViewModel.getDisplayName()
                        .doOnNext { channelMembers ->
                            channelMembers.filter {
                                it.getUserId() != AmityCoreClient.getUserId()
                            }.map { channelMember ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    val title = channelMember.getUser()?.getDisplayName()
                                    val avatarUrl = channelMember.getUser()?.getAvatar()
                                        ?.getUrl(AmityImage.Size.SMALL)

                                    messageListViewModel.title.set(title)
                                    messageListViewModel.avatarUrl.set(avatarUrl)
                                }
                            }
                        }.subscribe()
                )
            } else {
                messageListViewModel.title.set(ekoChannel.getDisplayName())
                messageListViewModel.avatarUrl
                    .set(ekoChannel.getAvatar()?.getUrl(AmityImage.Size.SMALL))
            }
        })
    }

    private fun initToolBar() {
        binding.chatToolBar.apply {
            if (essentialViewModel.enableChatToolbar) {
                (activity as AppCompatActivity).supportActionBar?.displayOptions =
                    ActionBar.DISPLAY_SHOW_CUSTOM
                (activity as AppCompatActivity).setSupportActionBar(root as Toolbar)

                ivBack.setOnClickListener {
                    activity?.finish()
                }
                root.visibility = View.VISIBLE
            } else {
                root.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        mAdapter =
            AmityMessagePagingAdapter(
                messageListViewModel,
                viewHolderListener,
                this,
                activity?.baseContext!!
            )
        val layoutManager = LinearLayoutManager(activity).apply {
            this.reverseLayout = true
        }
        binding.rvChatList.apply {
            this.layoutManager = layoutManager
            adapter = mAdapter
            addItemDecoration(
                AmityRecyclerViewItemDecoration(
                    0,
                    0,
                    resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
                )
            )
            itemAnimator = null
            val percentage = 30F / 100
            val background = ColorUtils.setAlphaComponent(
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amityColorBase
                    ), AmityColorShade.SHADE4
                ), (percentage * 255).toInt()
            )
            setBackgroundColor(background)
            observeScrollingState(layoutManager)
            observeMessages()
            recycledViewPool.setMaxRecycledViews(MessageType.MESSAGE_ID_IMAGE_SENDER, 0)
        }
    }

    private fun observeMessages() {
        messageListDisposable =
            messageListViewModel.getAllMessages().subscribe { messageList ->
                mAdapter.submitData(lifecycle, messageList)
                messageListViewModel.isScrollable.set(binding.rvChatList.computeVerticalScrollRange() > binding.rvChatList.height)
            }
        messageListViewModel.startReading()
    }

    private val latestMessageObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (isReachBottom) {
                    scrollToLastPosition()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRecorderTouchListener() {
        binding.tvRecord.setOnTouchListener { _, event ->
            if (isRecorderPermissionGranted()) {
                binding.recorderView.onTouch(event)
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        messageListViewModel.isRecording.set(true)
                        binding.recorderView.circularReveal()

                    }

                    MotionEvent.ACTION_UP -> messageListViewModel.isRecording.set(false)
                }
            } else {
                requestRecorderPermission()
            }
            true
        }
    }


    private fun hideLoadingView() {
        binding.loadingView.visibility = View.GONE
    }


    private fun requestRecorderPermission() {
        recordPermission.launch(requiredPermissions)
    }

    private fun isRecorderPermissionGranted(): Boolean {
        var isGranted = true
        requiredPermissions.forEach {
            if (context?.checkCallingOrSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false
            }
        }
        recordPermissionGranted = isGranted
        return recordPermissionGranted
    }

    private fun scrollToLastPosition() {
        binding.rvChatList.scrollToPosition(0)
        isReachBottom = true
    }

    private fun observeViewModelEvents() {
        messageListViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.CAMERA_CLICKED -> takePicture()
                AmityEventIdentifier.PICK_FILE -> pickFile()
                AmityEventIdentifier.PICK_IMAGE -> pickMultipleImages()
                AmityEventIdentifier.MSG_SEND_ERROR -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        val snackBar =
                            Snackbar.make(
                                binding.rvChatList,
                                R.string.amity_failed_msg,
                                Snackbar.LENGTH_SHORT
                            )
                        snackBar.show()
                    }
                }

                AmityEventIdentifier.MSG_SEND_SUCCESS -> scrollToLastPosition()
                AmityEventIdentifier.TOGGLE_CHAT_COMPOSE_BAR -> toggleSoftKeyboard()
                AmityEventIdentifier.SHOW_AUDIO_RECORD_UI -> showAudioRecordUi()
                else -> {

                }
            }
        }
    }

    private fun showAudioRecordUi() {
        AmityAndroidUtil.hideKeyboard(binding.layoutParent)
        messageListViewModel.showComposeBar.set(false)
    }

    private fun toggleSoftKeyboard() {
        messageListViewModel.isVoiceMsgUi.set(false)
        if (AmityAndroidUtil.isSoftKeyboardOpen(binding.layoutParent)) {
            AmityAndroidUtil.hideKeyboard(binding.layoutParent)
            Handler(Looper.getMainLooper()).postDelayed({
                messageListViewModel.showComposeBar.set(true)
            }, 300)
        } else {
            if (messageListViewModel.showComposeBar.get()) {
                messageListViewModel.showComposeBar.set(false)
                binding.etMessage.requestFocus()
                AmityAndroidUtil.showKeyboard(binding.etMessage)
            } else {
                messageListViewModel.showComposeBar.set(true)
            }
        }

        if (messageListViewModel.keyboardHeight.get() == 0) {
            val height = AmityAndroidUtil.getKeyboardHeight(binding.layoutParent)
            if (height != null && height > 0) {
                messageListViewModel.keyboardHeight.set(height)
            }
        }
    }

    private fun pickMultipleImages() {
        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onFilePicked(data: Uri?) {
        view?.showSnackBar("$data", Snackbar.LENGTH_SHORT)
    }

    override fun onImagePicked(data: Uri?) {

    }

    override fun onPhotoClicked(file: File?) {
        if (file != null) {
            val photoUri = Uri.fromFile(file)
            disposable.add(messageListViewModel.sendImageMessage(photoUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    msgSent = true
                }.doOnError {
                    msgSent = false
                }.subscribe()
            )
            if (messageListViewModel.showComposeBar.get()) {
                messageListViewModel.showComposeBar.set(false)
            }
        }
    }

    override fun onFileRecorded(audioFile: File?) {
        messageListViewModel.isRecording.set(false)
        if (audioFile != null) {
            val audioFileUri = Uri.fromFile(audioFile)
            disposable.add(messageListViewModel.sendAudioMessage(audioFileUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    msgSent = true
                }.doOnError {
                    msgSent = false
                }.subscribe()
            )
        }
    }

    override fun showMessage() {
        val layout: View = layoutInflater.inflate(
            R.layout.amity_view_audio_msg_error,
            activity?.findViewById(R.id.errorMessageContainer)
        )
        val textView = layout.findViewById<TextView>(R.id.tvMessage)
        textView.setShape(null, null, null, null, R.color.amityColorBase, null, null)
        layout.showSnackBar("", Snackbar.LENGTH_SHORT)
    }


    private fun addImageToList(uri: Uri) {
        disposable.add(messageListViewModel.sendImageMessage(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                msgSent = true
            }.doOnError {
                msgSent = false
            }.subscribe()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.eko_chat_list, menu)
    }

    override fun onPause() {
        super.onPause()
        messageListViewModel.isRecording.set(false)
        mAdapter.pauseAndResetPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messageListViewModel.stopReading()
        mAdapter.releaseMediaPlayer()
        try {
            latestMessageObserver.apply { mAdapter.unregisterAdapterDataObserver(this) }
        } catch (e: IllegalStateException) {
            Timber.e(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        messageListViewModel.onAmityEventReceived.removeAllhandlers()
        if (messageListDisposable?.isDisposed == false) {
            messageListDisposable?.dispose()
        }
    }


    class Builder internal constructor(private val channelId: String) {

        private var enableChatToolbar = true
        private var enableConnectionBar = true
        private var customViewHolder: AmityMessagePagingAdapter.CustomViewHolderListener? = null

        fun enableChatToolbar(enable: Boolean): Builder {
            this.enableChatToolbar = enable
            return this
        }

        fun enableConnectionBar(enable: Boolean): Builder {
            this.enableConnectionBar = enable
            return this
        }

        fun customViewHolder(customViewHolder: AmityMessagePagingAdapter.CustomViewHolderListener): Builder {
            this.customViewHolder = customViewHolder
            return this
        }

        fun build(activity: AppCompatActivity): AmityChatRoomWithDefaultComposeBarFragment {
            val essentialViewModel =
                ViewModelProvider(activity).get(AmityChatRoomEssentialViewModel::class.java)
            essentialViewModel.channelId = channelId
            essentialViewModel.enableChatToolbar = enableChatToolbar
            essentialViewModel.enableConnectionBar = enableConnectionBar
            essentialViewModel.customViewHolder = customViewHolder
            return AmityChatRoomWithDefaultComposeBarFragment()
        }
    }


    companion object {
        internal fun newInstance(channelId: String): Builder {
            return Builder(channelId)
        }
    }
}

private const val PAGINATION_PRELOAD_THRESHOLD = 10
