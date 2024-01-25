package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentStoryTargetSelectionBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.STORY_CREATION
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityCreatePostCommunitySelectionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCreatePostCommunitySelectionListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityStoryTargetViewModel
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_STORY_CREATION_TYPE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@UnstableApi
class AmityStoryTargetPickerFragment : AmityBaseFragment(),
    AmityCreatePostCommunitySelectionListener {

    private val viewModel: AmityStoryTargetViewModel by viewModels()
    private lateinit var communityAdapter: AmityCreatePostCommunitySelectionAdapter
    private lateinit var binding: AmityFragmentStoryTargetSelectionBinding

    private val behavior by lazy {
        AmitySocialBehaviorHelper.storyTabComponentBehavior
    }

    private val createStory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                activity?.finish()
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
        binding = AmityFragmentStoryTargetSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.storyCreationType =
            arguments?.getString(EXTRA_PARAM_STORY_CREATION_TYPE) ?: STORY_CREATION
        initRecyclerView()
    }

    private fun initRecyclerView() {
        communityAdapter = AmityCreatePostCommunitySelectionAdapter(this)

        binding.rvCommunity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = communityAdapter
            addItemDecoration(
                AmityRecyclerViewItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.amity_padding_xs),
                    0, resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
                )
            )
            hasFixedSize()
        }
        getCommunity()
    }

    private fun getCommunity() {
        viewModel.getCommunityList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                communityAdapter.submitData(lifecycle, it)
                binding.tvCommunityLabel.visibility = View.VISIBLE
            }.doOnError {
            }.subscribe()
    }

    override fun onClickCommunity(community: AmityCommunity, position: Int) {
        when (viewModel.storyCreationType) {
            STORY_CREATION -> {
                behavior.goToCreateStoryPage(
                    context = requireContext(),
                    launcher = createStory,
                    community = community
                )
            }
        }
    }

    class Builder internal constructor() {
        fun build(
            storyCreationType: String
        ): AmityStoryTargetPickerFragment {
            return AmityStoryTargetPickerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_STORY_CREATION_TYPE, storyCreationType)
                }
            }
        }
    }
}