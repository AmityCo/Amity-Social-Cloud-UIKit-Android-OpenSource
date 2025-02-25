package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentPostTargetSelectionBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityLiveStreamPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPollPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostDetailsActivity
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityCreatePostCommunitySelectionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostCommunitySelectionListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityPostTargetViewModel
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_CREATION_TYPE
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityPostTargetPickerFragment : AmityBaseFragment(),
    AmityCreatePostCommunitySelectionListener {

    private val viewModel: AmityPostTargetViewModel by viewModels()
    private lateinit var communityAdapter: AmityCreatePostCommunitySelectionAdapter
    private lateinit var binding: AmityFragmentPostTargetSelectionBinding
    private val TAG = AmityPostTargetPickerFragment::class.java.canonicalName
    private val adapterDisposables: CompositeDisposable = CompositeDisposable()

    private val createGenericPost =
        registerForActivityResult(
            AmityPostCreatorActivity
                .AmityCreateCommunityPostActivityContract()
        ) {
            if(it != null){
                getCommunity()
                handleBackPress()
            }
        }

    private val createLiveStreamPost =
        registerForActivityResult(
            AmityLiveStreamPostCreatorActivity
                .AmityCreateLiveStreamPostActivityContract()
        ) { createdPostId ->
            createdPostId?.let {
                val intent =
                    AmityPostDetailsActivity.newIntent(requireContext(), createdPostId, null, null)
                startActivity(intent)
            }
            if (createdPostId != null) {
                getCommunity()
                handleBackPress()
            }
        }

    private val createPollPost = registerForActivityResult(
        AmityPollPostCreatorActivity
            .AmityPollCreatorActivityContract()
    ) {
        if (it != null) {
            getCommunity()
            handleBackPress()
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_post_target_selection,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postCreationType =
            arguments?.getString(EXTRA_PARAM_POST_CREATION_TYPE) ?: POST_CREATION_TYPE_GENERIC
        initRecyclerView()
        initProfileImage()
        binding.clMyTimeline.setOnClickListener { launchPostCreator() }
    }

    private fun launchPostCreator() {
        when (viewModel.postCreationType) {
            POST_CREATION_TYPE_GENERIC -> {
                createGenericPost.launch(null)
            }
            POST_CREATION_TYPE_LIVE_STREAM -> {
                createLiveStreamPost.launch(null)
            }
            POST_CREATION_TYPE_POLL -> {
                createPollPost.launch(null)
            }
        }
    }

    private fun initProfileImage() {
        viewModel.getUser({
            val user = it
            val imageURL = user.getAvatar()?.getUrl(AmityImage.Size.SMALL)
            Glide.with(this)
                .load(imageURL)
                .placeholder(R.drawable.amity_ic_default_profile_large)
                .centerCrop()
                .into(binding.avProfile)
        }, {
            // render default image
            Glide.with(this)
                .load("")
                .placeholder(R.drawable.amity_ic_default_profile_large)
                .centerCrop()
                .into(binding.avProfile)
        } )
    }

    private fun initRecyclerView() {
        communityAdapter = AmityCreatePostCommunitySelectionAdapter(adapterDisposables,this)
//        adapter.addLoadStateListener { loadState ->
//            if (loadState.source.refresh is LoadState.NotLoading) {
//                handleCommunitySectionVisibility()
//            }
//        }
        binding.rvCommunity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = communityAdapter
            hasFixedSize()
        }
        getCommunity()
    }

    private fun getCommunity() {
        viewModel.getCommunityList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                communityAdapter.submitData(lifecycle, it)
                binding.separator.visibility = View.VISIBLE
                binding.tvCommunityLabel.visibility = View.VISIBLE
            }.doOnError {
                Log.e(TAG, "initRecyclerView: ${it.localizedMessage}")
            }.subscribe()
    }

    private fun handleCommunitySectionVisibility() {
        val communitySectionVisibility = if (communityAdapter.itemCount > 0) View.VISIBLE else View.GONE
        binding.separator.visibility = communitySectionVisibility
        binding.tvCommunityLabel.visibility = communitySectionVisibility
    }


    override fun onClickCommunity(community: AmityCommunity, position: Int) {
        when (viewModel.postCreationType) {
            POST_CREATION_TYPE_GENERIC -> {
                createGenericPost.launch(community.getCommunityId())
            }
            POST_CREATION_TYPE_LIVE_STREAM -> {
                createLiveStreamPost.launch(community.getCommunityId())
            }
            POST_CREATION_TYPE_POLL -> {
                createPollPost.launch(community.getCommunityId())
            }
        }
    }

    class Builder internal constructor() {
        fun build(
            postCreationType: String
        ): AmityPostTargetPickerFragment {
            return AmityPostTargetPickerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_POST_CREATION_TYPE, postCreationType)
                }
            }
        }
    }

    override fun onDestroy() {
        adapterDisposables.clear()
        super.onDestroy()
    }
}

const val POST_CREATION_TYPE_GENERIC = "POST_CREATION_TYPE_GENERIC"
const val POST_CREATION_TYPE_LIVE_STREAM = "POST_CREATION_TYPE_LIVE_STREAM"
const val POST_CREATION_TYPE_POLL = "POST_CREATION_TYPE_LIVE_POLL"