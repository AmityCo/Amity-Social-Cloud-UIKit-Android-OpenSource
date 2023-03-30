package com.amity.socialcloud.uikit.community.followrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.FragmentAmityFollowRequestsBinding
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment

class AmityFollowRequestsFragment : RxFragment() {

    private var _binding: FragmentAmityFollowRequestsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: AmityFollowRequestsViewModel
    lateinit var followRequestsAdapter: AmityFollowRequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAmityFollowRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityFollowRequestsViewModel::class.java)
        setUpRecyclerView()
        binding.refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        binding.refreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setUpRecyclerView() {
        followRequestsAdapter = AmityFollowRequestsAdapter(requireContext())
        binding.rvFollowRequests.apply {
            adapter = followRequestsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        getPendingRequests()
    }

    private fun getPendingRequests() {
        viewModel.getFollowRequests(
            onSuccess = {
                binding.refreshLayout.isRefreshing = false
                binding.errorLayout.root.visibility = View.GONE
                followRequestsAdapter.submitData(lifecycle, it)
            },
            onError = {
                binding.errorLayout.root.visibility = View.VISIBLE
            }
        ).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun refresh() {
        binding.refreshLayout.isRefreshing = true
        getPendingRequests()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Builder internal constructor() {
        private var userId = ""

        fun build(activity: AppCompatActivity): AmityFollowRequestsFragment {
            val fragment = AmityFollowRequestsFragment()
            fragment.viewModel =
                ViewModelProvider(activity).get(AmityFollowRequestsViewModel::class.java)
            fragment.viewModel.userId = userId
            return fragment
        }

        internal fun setUserId(userId: String): Builder {
            this.userId = userId
            return this
        }
    }

    companion object {
        fun newInstance(userId: String): Builder {
            return Builder().setUserId(userId)
        }
    }
}