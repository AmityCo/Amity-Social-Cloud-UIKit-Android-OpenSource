package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.imagepreview.AmityImagePreviewActivity
import com.amity.socialcloud.uikit.common.imagepreview.AmityPreviewImage
import com.amity.socialcloud.uikit.common.utils.safeCast
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentMediaPostBinding
import com.amity.socialcloud.uikit.community.databinding.AmityViewPrivateUserProfileBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityVideoPlayerActivity
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityPostGalleryAdapter
import com.amity.socialcloud.uikit.community.newsfeed.events.PostGalleryClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityMediaGalleryTarget
import com.amity.socialcloud.uikit.community.newsfeed.model.TARGET_USER
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityMediaPostViewModel
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AmityMediaPostFragment : AmityBaseFragment() {

    lateinit var viewModel: AmityMediaPostViewModel
    private lateinit var adapter: AmityPostGalleryAdapter
    private lateinit var binding: AmityFragmentMediaPostBinding
    private var isObservingClickEvent = false
    private val emptyStatePublisher = PublishSubject.create<Boolean>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentMediaPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(this).get(AmityMediaPostViewModel::class.java)
        viewModel.targetType = arguments?.getString(GALLERY_TARGET_TYPE_ARG) ?: TARGET_USER
        viewModel.targetId = arguments?.getString(GALLERY_TARGET_ID_ARG) ?: ""
        viewModel.includeDelete = arguments?.getBoolean(GALLERY_INCLUDE_DELETED_ARG) ?: false
        viewModel.postType =
            arguments?.getString(GALLERY_POST_TYPE_ARG) ?: AmityPost.DataType.IMAGE.getApiKey()
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        getPosts()
    }

    private fun setupRecyclerview() {
        adapter = viewModel.createAdapter()
        binding.postGalleryRecyclerview.layoutManager =
            GridLayoutManager(requireContext(), GALLERY_SPAN_COUNT)
        binding.postGalleryRecyclerview.adapter = adapter
        adapter.addLoadStateListener { loadStates ->
            when (val refreshState = loadStates.mediator?.refresh) {
                is LoadState.NotLoading -> {
                    handleLoadedState(adapter.itemCount, loadStates)
                }
                is LoadState.Error -> {
                    handleErrorState(AmityError.from(refreshState.error))
                }
                else -> {}
            }
        }
        if (!isObservingClickEvent) {
            observePostGalleryClickEvents()
            isObservingClickEvent = true
        }
        emptyStatePublisher.toFlowable(BackpressureStrategy.BUFFER)
            .debounce(300, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(this)
            .doOnNext { shouldShowEmptyState ->
                if (shouldShowEmptyState) {
                    handleEmptyState()
                }
            }
            .subscribe()
    }

    @ExperimentalPagingApi
    private fun getPosts() {
        viewModel.getPosts { adapter.submitData(lifecycle, it) }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun handleLoadedState(itemCount: Int,  loadStates: CombinedLoadStates) {
        binding.postGalleryProgressBar.visibility = View.GONE
        binding.postGalleryEmptyView.visibility = View.GONE
        binding.postGalleryRecyclerview.visibility = View.VISIBLE
        if (loadStates.source.refresh is LoadState.NotLoading
            && loadStates.append.endOfPaginationReached
            && itemCount < 1
        ) {
            if (!emptyStatePublisher.hasComplete()) {
                emptyStatePublisher.onNext(true)
            }
        } else if (loadStates.source.refresh is LoadState.NotLoading
            && loadStates.append.endOfPaginationReached
            && itemCount > 0) {
            if (!emptyStatePublisher.hasComplete()) {
                emptyStatePublisher.onNext(false)
            }
        }
    }

    private fun handleErrorState(error: AmityError) {
        binding.postGalleryProgressBar.visibility = View.GONE
        if (error == AmityError.PERMISSION_DENIED) {
            handleErrorState(getPrivateProfileView())
        }
    }

    private fun handleErrorState(emptyView: View) {
        binding.postGalleryProgressBar.visibility = View.GONE
        binding.postGalleryErrorView.removeAllViews()
        binding.postGalleryErrorView.addView(emptyView)
        binding.postGalleryErrorView.visibility = View.VISIBLE
    }

    private fun handleEmptyState() {
        binding.postGalleryEmptyView.visibility = View.VISIBLE
        binding.postGalleryRecyclerview.visibility = View.GONE
        when (viewModel.postType) {
            AmityPost.DataType.IMAGE.getApiKey() -> {
                binding.postGalleryEmptyTextview.setText(R.string.amity_gallery_no_photos)
                binding.postGalleryEmptyImageview.setImageResource(R.drawable.amity_ic_photo_empty)
            }
            AmityPost.DataType.VIDEO.getApiKey() -> {
                binding.postGalleryEmptyTextview.setText(R.string.amity_gallery_no_videos)
                binding.postGalleryEmptyImageview.setImageResource(R.drawable.amity_ic_video_empty)
            }
        }
    }

    private fun getPrivateProfileView(): View {
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = AmityViewPrivateUserProfileBinding.inflate(
            inflater,
            requireView().parent as ViewGroup,
            false
        )
        return binding.root
    }

    private fun observePostGalleryClickEvents() {
        viewModel.getPostGalleryClickEvents(
            onReceivedEvent = {
                when (it) {
                    is PostGalleryClickEvent.Video -> openVideoPlayer(it.post)
                    is PostGalleryClickEvent.Image -> openImagePreview(it.post)
                }
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun openVideoPlayer(post: AmityPost) {
        safeCast<AmityPost.Data.VIDEO>(post.getData())?.let { videoData ->
            videoData.getVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    val videoIntent = AmityVideoPlayerActivity
                        .newIntent(context = requireContext(), videoUrl = it.getUrl().toString())
                    requireContext().startActivity(videoIntent)
                }
                .subscribe()
        }
    }

    private fun openImagePreview(post: AmityPost) {
        safeCast<AmityPost.Data.IMAGE>(post.getData())?.let { imageData ->
            val intent =
                AmityImagePreviewActivity.newIntent(
                    context = requireContext(),
                    imagePosition = 0,
                    showImageCount = false,
                    imageAmities = arrayListOf(
                        AmityPreviewImage(imageData.getImage()?.getUrl(AmityImage.Size.LARGE) ?: "")
                    )
                )
            requireContext().startActivity(intent)
        }
    }


    class Builder internal constructor() {
        private var includeDelete = false
        private var postType = AmityPost.DataType.IMAGE.getApiKey()

        fun build(target: AmityMediaGalleryTarget): AmityMediaPostFragment {
            val fragment = AmityMediaPostFragment()
            fragment.arguments = Bundle().apply {
                this.putString(GALLERY_TARGET_TYPE_ARG, target.getName())
                this.putString(GALLERY_TARGET_ID_ARG, target.id)
                this.putString(GALLERY_POST_TYPE_ARG, postType)
                this.putBoolean(GALLERY_INCLUDE_DELETED_ARG, includeDelete)
            }
            return fragment
        }

        fun includeDelete(includeDelete: Boolean): Builder {
            this.includeDelete = includeDelete
            return this
        }

        fun postType(postType: AmityPost.DataType): Builder {
            this.postType = postType.getApiKey()
            return this
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }
}

private const val GALLERY_SPAN_COUNT = 2
private const val GALLERY_TARGET_TYPE_ARG = "GALLERY_TARGET_TYPE_ARG"
private const val GALLERY_TARGET_ID_ARG = "GALLERY_TARGET_ID_ARG"
private const val GALLERY_POST_TYPE_ARG = "GALLERY_POST_TYPE_ARG"
private const val GALLERY_INCLUDE_DELETED_ARG = "GALLERY_INCLUDE_DELETED_ARG"
