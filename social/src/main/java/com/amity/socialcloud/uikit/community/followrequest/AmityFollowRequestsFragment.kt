package com.amity.socialcloud.uikit.community.followrequest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.uikit.community.R
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_amity_follow_requests.*

class AmityFollowRequestsFragment : RxFragment(R.layout.fragment_amity_follow_requests) {
    lateinit var viewModel: AmityFollowRequestsViewModel
    lateinit var followRequestsAdapter: AmityFollowRequestsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AmityFollowRequestsViewModel::class.java)
        setUpRecyclerView()
        refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        refreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setUpRecyclerView() {
        followRequestsAdapter = AmityFollowRequestsAdapter(requireContext())
        rvFollowRequests.apply {
            adapter = followRequestsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        getPendingRequests()
    }

    private fun getPendingRequests() {
        viewModel.getFollowRequests(
            onSuccess = {
                refreshLayout.isRefreshing = false
                errorLayout.visibility = View.GONE
                followRequestsAdapter.submitList(it)
            },
            onError = {
                errorLayout.visibility = View.VISIBLE
            }
        ).untilLifecycleEnd(this)
            .subscribe()
    }

    private fun refresh() {
        refreshLayout.isRefreshing = true
        getPendingRequests()
    }

    class Builder internal constructor() {
        private var userId = ""

        fun build(activity: AppCompatActivity): AmityFollowRequestsFragment {
            val fragment = AmityFollowRequestsFragment()
            fragment.viewModel = ViewModelProvider(activity).get(AmityFollowRequestsViewModel::class.java)
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