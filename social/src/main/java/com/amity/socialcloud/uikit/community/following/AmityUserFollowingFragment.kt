package com.amity.socialcloud.uikit.community.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityUserFollowingFragmentBinding
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AmityUserFollowingFragment : RxFragment() {

    private var _binding: AmityUserFollowingFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: AmityUserFollowingViewModel
    private var textChangeDisposable: Disposable? = null
    private val textChangeSubject: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }
    private lateinit var followingAdapter: AmityFollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AmityUserFollowingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityUserFollowingViewModel::class.java)
        setUpRecyclerView()
        subscribeSearchStringChange()
        binding.refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        binding.refreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setUpRecyclerView() {
        val userClickListener = object : AmityUserClickListener {
            override fun onClickUser(user: AmityUser) {
                AmitySocialUISettings.globalUserClickListener.onClickUser(
                    this@AmityUserFollowingFragment,
                    user
                )
            }
        }
        followingAdapter = AmityFollowingAdapter(requireContext(), userClickListener)
        binding.rvUserFollowing.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        getFollowing()
    }

    private fun getFollowing() {
        viewModel.getFollowingList {
            binding.refreshLayout.isRefreshing = false
            followingAdapter.submitData(lifecycle, it)
        }.untilLifecycleEnd(this)
            .subscribe()
    }

    private fun refresh() {
        binding.refreshLayout.isRefreshing = true
        getFollowing()
    }

    private fun subscribeSearchStringChange() {
        binding.etSearch.doAfterTextChanged {
            if (it != null) {
                textChangeSubject.onNext(it.toString())
            }
        }
        textChangeDisposable = textChangeSubject.debounce(300, TimeUnit.MILLISECONDS)
            .map {
                if (it.isEmpty()) {
                    getFollowing()
                } else {
                    searchFollowing(it)
                }
            }
            .subscribe()
    }

    private fun searchFollowing(input: String) {

    }

    override fun onDestroy() {
        if (textChangeDisposable?.isDisposed == false) {
            textChangeDisposable?.dispose()
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class Builder internal constructor() {
        var userId: String = ""

        fun build(activity: AppCompatActivity): AmityUserFollowingFragment {
            val fragment = AmityUserFollowingFragment()
            fragment.viewModel =
                ViewModelProvider(activity).get(AmityUserFollowingViewModel::class.java)
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