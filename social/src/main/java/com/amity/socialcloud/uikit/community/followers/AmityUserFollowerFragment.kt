package com.amity.socialcloud.uikit.community.followers

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.amity_user_follower_fragment.*
import java.util.concurrent.TimeUnit

class AmityUserFollowerFragment :
    RxFragment(R.layout.amity_user_follower_fragment) {
    lateinit var viewModel: AmityUserFollowerViewModel
    private var textChangeDisposable: Disposable? = null
    private val textChangeSubject: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }
    private lateinit var followerAdapter: AmityFollowerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AmityUserFollowerViewModel::class.java)
        setupRecyclerView()
        subscribeSearchStringChange()
        refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        refreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun setupRecyclerView() {
        val userClickListener = object: AmityUserClickListener {
            override fun onClickUser(user: AmityUser) {
                AmitySocialUISettings.globalUserClickListener.onClickUser(this@AmityUserFollowerFragment, user)
            }
        }

        followerAdapter = AmityFollowerAdapter(requireContext(),
            userClickListener,
            viewModel.isSelf())
        rvUserFollowers.apply {
            adapter = followerAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        getFollowers()
    }

    private fun getFollowers() {
        viewModel.getFollowersList {
            refreshLayout.isRefreshing = false
            followerAdapter.submitList(it)
        }.untilLifecycleEnd(this)
            .subscribe()
    }

    private fun subscribeSearchStringChange() {
        etSearch.doAfterTextChanged {
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
        refreshLayout.isRefreshing = true
        getFollowers()
    }

    override fun onDestroyView() {
        if (textChangeDisposable?.isDisposed == false) {
            textChangeDisposable?.dispose()
        }
        super.onDestroyView()
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