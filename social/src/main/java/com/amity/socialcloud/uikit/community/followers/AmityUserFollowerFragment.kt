package com.amity.socialcloud.uikit.community.followers

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
import com.amity.socialcloud.uikit.community.databinding.AmityUserFollowerFragmentBinding
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AmityUserFollowerFragment : RxFragment() {
    private var _binding: AmityUserFollowerFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: AmityUserFollowerViewModel
    private var textChangeDisposable: Disposable? = null
    private val textChangeSubject: PublishSubject<String> by lazy {
        PublishSubject.create()
    }
    private lateinit var followerAdapter: AmityFollowerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AmityUserFollowerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AmityUserFollowerViewModel::class.java)
        setupRecyclerView()
        subscribeSearchStringChange()
        binding.refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        binding.refreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setupRecyclerView() {
        val userClickListener = object : AmityUserClickListener {
            override fun onClickUser(user: AmityUser) {
                AmitySocialUISettings.globalUserClickListener.onClickUser(
                    this@AmityUserFollowerFragment,
                    user
                )
            }
        }

        followerAdapter = AmityFollowerAdapter(
            requireContext(),
            userClickListener,
            viewModel.isSelf()
        )
        binding.rvUserFollowers.apply {
            adapter = followerAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        getFollowers()
    }

    private fun getFollowers() {
        viewModel.getFollowersList {
            binding.refreshLayout.isRefreshing = false
            followerAdapter.submitData(lifecycle, it)
        }.untilLifecycleEnd(this)
            .subscribe()
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
                    getFollowers()
                } else {
                    searchFollowers(it)
                }
            }
            .subscribe()
    }

    private fun searchFollowers(input: String) {

    }

    private fun refresh() {
        binding.refreshLayout.isRefreshing = true
        getFollowers()
    }

    override fun onDestroyView() {
        if (textChangeDisposable?.isDisposed == false) {
            textChangeDisposable?.dispose()
        }
        super.onDestroyView()
        _binding = null
    }

    class Builder internal constructor() {
        var userId: String = ""

        fun build(activity: AppCompatActivity): AmityUserFollowerFragment {
            val fragment = AmityUserFollowerFragment()
            fragment.viewModel =
                ViewModelProvider(activity).get(AmityUserFollowerViewModel::class.java)
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