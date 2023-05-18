package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentReactionListBinding
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityReactionListViewModel

class AmityReactionListFragment : AmityBaseFragment() {

    private lateinit var binding: AmityFragmentReactionListBinding
    private lateinit var viewModel: AmityReactionListViewModel
    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentReactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(AmityReactionListViewModel::class.java)

        setupTabs()
    }

    private fun setupTabs() {
        fragmentStateAdapter = AmityFragmentStateAdapter(childFragmentManager, this.lifecycle)
        fragmentStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    "All ${formattedReactionCount()}",
                    getSingleReactionFragment(null)
                ),
                /*
                 * To show extra tab for each reaction type
                 *
                AmityFragmentStateAdapter.AmityPagerModel(
                    "Like ${formattedReactionCount("like")}",
                    getSingleReactionFragment("like")
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    "Love ${formattedReactionCount("love")}",
                    getSingleReactionFragment("love")
                )
                 */
            )
        )

        binding.reactionsTabLayout.setAdapter(fragmentStateAdapter)
    }

    private fun formattedReactionCount(reactionName: String? = null): String {
        return viewModel.getReactionCount(reactionName).readableNumber()
    }

    private fun getSingleReactionFragment(reactionName: String?): Fragment {
        return AmitySingleReactionFragment.newInstance(
            viewModel.referenceType,
            viewModel.referenceId,
            reactionName
        ).build(activity as AppCompatActivity)
    }


    class Builder internal constructor() {

        private lateinit var referenceType: AmityReactionReferenceType
        private var referenceId: String = ""

        fun build(activity: AppCompatActivity): AmityReactionListFragment {
            val fragment = AmityReactionListFragment()
            val viewModel = ViewModelProvider(activity).get(AmityReactionListViewModel::class.java)

            viewModel.referenceType = referenceType
            viewModel.referenceId = referenceId

            return fragment
        }

        internal fun referenceType(referenceType: AmityReactionReferenceType): Builder {
            this.referenceType = referenceType
            return this
        }

        internal fun referenceId(referenceId: String): Builder {
            this.referenceId = referenceId
            return this
        }
    }

    companion object {
        fun newInstance(
            referenceType: AmityReactionReferenceType,
            referenceId: String
        ): Builder {
            return Builder()
                .referenceType(referenceType)
                .referenceId(referenceId)
        }
    }
}