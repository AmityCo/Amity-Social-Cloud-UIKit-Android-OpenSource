package com.amity.socialcloud.uikit.community.members

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagedAdapter
import com.amity.socialcloud.uikit.common.common.views.bottomsheet.AmityBottomSheetListFragment
import com.amity.socialcloud.uikit.common.common.views.bottomsheet.AmityMenuItemClickListener
import com.amity.socialcloud.uikit.common.model.AmityMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemCommunityMembershipBinding
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AmityCommunityModeratorAdapter(
    private val context: Context,
    private val listener: AmityMemberClickListener,
    private val communityMemberViewModel: AmityCommunityMembersViewModel
) : AmityBaseRecyclerViewPagedAdapter<AmityCommunityMember>(diffCallBack) {

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AmityCommunityMember>() {
            override fun areItemsTheSame(
                oldItem: AmityCommunityMember,
                newItem: AmityCommunityMember
            ): Boolean = oldItem.getUserId() == newItem.getUserId()

            override fun areContentsTheSame(
                oldItem: AmityCommunityMember,
                newItem: AmityCommunityMember
            ): Boolean = oldItem == newItem
        }
    }

    override fun getLayoutId(position: Int, obj: AmityCommunityMember?): Int =
        R.layout.amity_item_community_membership

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val itemViewModel = AmityMembershipItemViewModel()
        itemViewModel.communityId = communityMemberViewModel.communityId
        return AmityModeratorViewHolder(view, context, listener, itemViewModel)
    }

    inner class AmityModeratorViewHolder(
        itemView: View, private val context: Context,
        private val listener: AmityMemberClickListener,
        private val itemViewModel: AmityMembershipItemViewModel
    ) : AmityCommunityMembersBaseViewHolder(
        itemView,
        context,
        itemViewModel,
        communityMemberViewModel
    ),
        Binder<AmityCommunityMember> {

        private val binding: AmityItemCommunityMembershipBinding? = DataBindingUtil.bind(itemView)

        override fun bind(data: AmityCommunityMember?, position: Int) {
            data?.let { member ->
                binding?.apply {
                    listener = this@AmityCommunityModeratorAdapter.listener
                    isJoined = communityMemberViewModel.isJoined.get()
                    communityMemberShip = data
                    avatarUrl = member.getUser()?.getAvatar()?.getUrl(AmityImage.Size.MEDIUM)
                    isMyUser = data.getUserId() == AmityCoreClient.getUserId()
                    isBanned = isBannedUser(data)

                    val banIcon = if (isBannedUser(data)) {
                        ContextCompat.getDrawable(context, R.drawable.amity_ic_ban)
                    } else {
                        null
                    }
                    tvMemberName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        null, null, banIcon, null
                    )

                    ivMore.setOnClickListener {
                        if (data.getUserId().isNotEmpty()) {
                            showBottomSheet(data)
                        } else {
                            //TODO Handle when user id is empty
                        }
                    }
                }
            }
        }

        private fun isBannedUser(communityMember: AmityCommunityMember?): Boolean {
            return communityMember?.isBanned() == true
                    || communityMember?.getUser()?.isGlobalBan() == true
        }

        private fun showBottomSheet(communityMembership: AmityCommunityMember) {
            itemViewModel.getBottomSheetModeratorTab(context, communityMembership)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .firstOrError()
                .doOnSuccess { items ->
                    val manager = (context as AppCompatActivity).supportFragmentManager
                    val fragment = AmityBottomSheetListFragment.newInstance(items)
                    fragment.show(manager, AmityBottomSheetListFragment.toString())
                    fragment.setMenuItemClickListener(object : AmityMenuItemClickListener {
                        override fun onMenuItemClicked(menuItem: AmityMenuItem) {
                            fragment.dismiss()
                            communityMembership.getUser()?.let { user ->
                                when (menuItem.id) {
                                    AmityConstants.ID_REPORT_USER -> sendReportUser(user, true)
                                    AmityConstants.ID_UN_REPORT_USER -> sendReportUser(user, false)
                                    AmityConstants.ID_REMOVE_USER -> showRemoveUserDialog(user)
                                    AmityConstants.ID_REMOVE_MODERATOR -> removeModerator(communityMembership)
                                }
                            }
                        }
                    })
                }
                .untilLifecycleEnd(view = itemView)
                .subscribe()
        }

    }

}