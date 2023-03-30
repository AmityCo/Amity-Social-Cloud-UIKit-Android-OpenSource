package com.amity.socialcloud.uikit.community.followers

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.follow.AmityFollowRelationship
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseRecyclerViewPagingDataAdapter
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.bottomsheet.AmityBottomSheetListFragment
import com.amity.socialcloud.uikit.common.common.views.bottomsheet.AmityMenuItemClickListener
import com.amity.socialcloud.uikit.common.model.AmityMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemUserFollowerBinding
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd

class AmityFollowerAdapter(
    private val context: Context,
    private val listener: AmityUserClickListener,
    private val isSelf: Boolean
) : AmityBaseRecyclerViewPagingDataAdapter<AmityFollowRelationship>(userDiffUtil) {

    companion object {
        val userDiffUtil = object : DiffUtil.ItemCallback<AmityFollowRelationship>() {

            override fun areItemsTheSame(
                oldItem: AmityFollowRelationship,
                newItem: AmityFollowRelationship
            ): Boolean =
                oldItem.getSourceUser()?.getUserId() == newItem.getSourceUser()?.getUserId()

            override fun areContentsTheSame(
                oldItem: AmityFollowRelationship,
                newItem: AmityFollowRelationship
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun getLayoutId(position: Int, obj: AmityFollowRelationship?): Int =
        R.layout.amity_item_user_follower

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val itemViewModel = AmityFollowersItemViewModel()
        return AmityFollowersItemViewHolder(view, context, itemViewModel, listener, isSelf)
    }

    inner class AmityFollowersItemViewHolder(
        itemView: View,
        private val context: Context,
        private val itemViewModel: AmityFollowersItemViewModel,
        private val clickListener: AmityUserClickListener,
        private val isSelf: Boolean
    ) : AmityFollowersBaseViewHolder(itemView, context, itemViewModel),
        Binder<AmityFollowRelationship> {

        private val binding: AmityItemUserFollowerBinding? = DataBindingUtil.bind(itemView)

        override fun bind(data: AmityFollowRelationship?, position: Int) {
            data?.let {
                binding?.apply {
                    user = data.getSourceUser()
                    isSelf = data.getSourceUser()?.getUserId() == AmityCoreClient.getUserId()
                    ivAvatar.setOnClickListener {
                        user?.let {
                            clickListener.onClickUser(it)
                        }
                    }
                    tvMemberName.setOnClickListener {
                        user?.let {
                            clickListener.onClickUser(it)
                        }
                    }
                    val banIcon = if (user?.isGlobalBan() == true) {
                        ContextCompat.getDrawable(context, R.drawable.amity_ic_ban)
                    } else {
                        null
                    }
                    tvMemberName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        null, null, banIcon, null
                    )
                }
                binding?.ivMore?.setOnClickListener {
                    showBottomSheet(data.getSourceUser()!!)
                }
            }
        }

        private fun showBottomSheet(user: AmityUser) {
            val manager = (context as AppCompatActivity).supportFragmentManager
            val items = arrayListOf<AmityMenuItem>()
            if (user.isFlaggedByMe()) {
                items.add(
                    AmityMenuItem(
                        AmityConstants.ID_UN_REPORT_USER,
                        context.getString(R.string.amity_un_report_user)
                    )
                )
            } else {
                items.add(
                    AmityMenuItem(
                        AmityConstants.ID_REPORT_USER,
                        context.getString(R.string.amity_report_user)
                    )
                )
            }
            if (isSelf) {
                items.add(
                    AmityMenuItem(
                        AmityConstants.ID_REMOVE_USER,
                        context.getString(R.string.amity_remove_user),
                        true
                    )
                )
            }

            val fragment = AmityBottomSheetListFragment.newInstance(items)
            fragment.show(manager, AmityBottomSheetListFragment.toString())
            fragment.setMenuItemClickListener(object : AmityMenuItemClickListener {
                override fun onMenuItemClicked(menuItem: AmityMenuItem) {
                    fragment.dismiss()
                    when (menuItem.id) {
                        AmityConstants.ID_REPORT_USER -> sendReportUser(user, true)
                        AmityConstants.ID_UN_REPORT_USER -> sendReportUser(user, false)
                        AmityConstants.ID_REMOVE_USER -> showRemoveUserDialog(user)
                    }
                }
            })
        }

        fun showRemoveUserDialog(user: AmityUser) {
            AmityAlertDialogUtil.showDialog(
                context,
                String.format(
                    context.getString(R.string.amity_remove_follower),
                    user.getDisplayName()
                ),
                String.format(
                    context.getString(R.string.amity_remove_follower_msg),
                    user.getDisplayName()
                ),
                context.getString(R.string.amity_remove),
                context.getString(R.string.amity_cancel),
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        removeUser(user)
                    }
                    dialog.cancel()
                })
        }

        private fun removeUser(user: AmityUser) {
            itemViewModel.removeUser(user.getUserId())
                .doOnComplete {
                    itemView.showSnackBar(context.getString(R.string.amity_removed))
                }.doOnError {
                    itemView.showSnackBar(context.getString(R.string.amity_unable_to_remove))
                }
                .untilLifecycleEnd(itemView).subscribe()
        }
    }
}