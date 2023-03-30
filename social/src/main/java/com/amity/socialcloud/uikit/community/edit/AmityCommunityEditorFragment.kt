package com.amity.socialcloud.uikit.community.edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.ui.view.AmityCommunityCreateBaseFragment
import com.amity.socialcloud.uikit.community.ui.viewModel.AmityCreateCommunityViewModel
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AmityCommunityEditorFragment : AmityCommunityCreateBaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfile()
    }

    private fun loadProfile() {
        disposable.add(viewModel.getCommunityDetail()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                viewModel.setCommunityDetails(it)
                renderAvatar()
            }.doOnError {

            }.subscribe()
        )
    }

    override fun renderAvatar() {
        if(viewModel.avatarUrl.get().isNullOrEmpty()) {
            super.renderAvatar()
        } else {
            Glide.with(requireContext())
                .load(viewModel.avatarUrl.get())
                .centerCrop()
                .into(binding.ccAvatar)
        }
    }

    class Builder internal constructor(){
        private lateinit var communityId: String

        fun build(activity: AppCompatActivity): AmityCommunityEditorFragment {
            val fragment = AmityCommunityEditorFragment()
            fragment.viewModel = ViewModelProvider(activity).get(AmityCreateCommunityViewModel::class.java)
            fragment.viewModel.communityId.set(communityId)
            fragment.viewModel.savedCommunityId = communityId
            return fragment
        }

        internal fun communityId(communityId: String): Builder {
            this.communityId = communityId
            return this
        }

    }

    companion object {

        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }

        @Deprecated("Use communityId instead")
        fun newInstance(community: AmityCommunity): Builder {
            return Builder().communityId(community.getCommunityId())
        }
    }
}