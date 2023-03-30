package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentParentMediaGalleryBinding
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityMediaGalleryTarget
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityParentMediaGalleryViewModel
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_INCLUDE_DELETED
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_TARGET_ID
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_TARGET_TYPE
import com.google.android.material.tabs.TabLayoutMediator

class AmityMediaGalleryFragment : AmityBaseFragment() {

    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter
    private lateinit var binding: AmityFragmentParentMediaGalleryBinding
    private val viewModel: AmityParentMediaGalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentParentMediaGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabLayout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultTarget = AmityMediaGalleryTarget.COMMUNITY("")
        viewModel.targetType =
            arguments?.getString(EXTRA_PARAM_TARGET_TYPE) ?: defaultTarget.getName()
        viewModel.targetId =
            arguments?.getString(EXTRA_PARAM_TARGET_ID) ?: defaultTarget.id
        viewModel.includeDelete =
            arguments?.getBoolean(EXTRA_PARAM_INCLUDE_DELETED) ?: false
    }

    private fun setUpTabLayout() {
        fragmentStateAdapter = AmityFragmentStateAdapter(childFragmentManager, this.lifecycle)
        fragmentStateAdapter.setFragmentList(
            arrayListOf(photoGalleryFragment(), videoGalleryFragment())
        )
        binding.mediaGalleryViewpager.adapter = fragmentStateAdapter
        binding.mediaGalleryViewpager.isUserInputEnabled = false
        setupCustomTabView()
    }

    private fun setupCustomTabView() {
        TabLayoutMediator(
            binding.mediaGalleryTabLayout,
            binding.mediaGalleryViewpager
        ) { _, _ -> }.attach()
        val photoTabTextView =
            View.inflate(requireContext(), R.layout.amity_view_media_gallery_tab, null) as TextView
        val videoTabTextView =
            View.inflate(requireContext(), R.layout.amity_view_media_gallery_tab, null) as TextView
        photoTabTextView.text = fragmentStateAdapter.getTitle(0)
        videoTabTextView.text = fragmentStateAdapter.getTitle(1)
        binding.mediaGalleryTabLayout.getTabAt(0)?.customView = photoTabTextView
        binding.mediaGalleryTabLayout.getTabAt(1)?.customView = videoTabTextView
        registerTabChangeListener(photoTabTextView, videoTabTextView)
    }

    private fun registerTabChangeListener(photoTabTextView: TextView, videoTabTextView: TextView) {
        val videoTitle = requireActivity().getString(R.string.amity_general_videos)
        val photoTitle = requireActivity().getString(R.string.amity_general_photos)
        binding.mediaGalleryViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (fragmentStateAdapter.getTitle(position) == photoTitle) {
                    photoTabTextView.setTextColor(selectedTextColor())
                    photoTabTextView.setBackgroundDrawable(selectedTextBackground())
                    videoTabTextView.setTextColor(unselectedTextColor())
                    videoTabTextView.setBackgroundDrawable(unselectedTextBackground())
                } else if (fragmentStateAdapter.getTitle(position) == videoTitle) {
                    photoTabTextView.setTextColor(unselectedTextColor())
                    photoTabTextView.setBackgroundDrawable(unselectedTextBackground())
                    videoTabTextView.setTextColor(selectedTextColor())
                    videoTabTextView.setBackgroundDrawable(selectedTextBackground())
                }
            }
        })
    }

    private fun photoGalleryFragment() = AmityFragmentStateAdapter.AmityPagerModel(
        requireActivity().getString(R.string.amity_general_photos),
        postGalleryFragment(AmityPost.DataType.IMAGE)
    )

    private fun videoGalleryFragment() = AmityFragmentStateAdapter.AmityPagerModel(
        requireActivity().getString(R.string.amity_general_videos),
        postGalleryFragment(AmityPost.DataType.VIDEO)
    )

    private fun selectedTextColor() = resources.getColor(R.color.amityColorWhite)

    private fun unselectedTextColor() = resources.getColor(R.color.amityPlaceHolderDarkColor)

    private fun selectedTextBackground() = ResourcesCompat.getDrawable(
        resources,
        R.drawable.amity_rounded_primary_color_bg,
        null
    )

    private fun unselectedTextBackground() = ResourcesCompat.getDrawable(
        resources,
        R.drawable.amity_rounded_grey_color_bg,
        null
    )

    private fun postGalleryFragment(postType: AmityPost.DataType): Fragment {
        return AmityMediaPostFragment.newInstance()
            .postType(postType)
            .includeDelete(false)
            .build(getTarget())
    }

    private fun getTarget(): AmityMediaGalleryTarget {
        return if (viewModel.targetType == AmityMediaGalleryTarget
                .USER(viewModel.targetId)
                .getName()
        ) {
            AmityMediaGalleryTarget.USER(viewModel.targetId)
        } else {
            AmityMediaGalleryTarget.COMMUNITY(viewModel.targetId)
        }
    }

    class Builder internal constructor() {
        private var includeDelete = false

        fun build(
            target: AmityMediaGalleryTarget
        ): AmityMediaGalleryFragment {
            return AmityMediaGalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PARAM_TARGET_TYPE, target.getName())
                    putString(EXTRA_PARAM_TARGET_ID, target.id)
                    putBoolean(EXTRA_PARAM_INCLUDE_DELETED, includeDelete)
                }
            }
        }

        fun includeDelete(includeDelete: Boolean): Builder {
            this.includeDelete = includeDelete
            return this
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

}