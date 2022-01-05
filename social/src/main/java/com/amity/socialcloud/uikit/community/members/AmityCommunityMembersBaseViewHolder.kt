package com.amity.socialcloud.uikit.community.members

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.error.AmityException
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.home.activity.AmityCommunityHomePageActivity
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers

abstract class AmityCommunityMembersBaseViewHolder(
    itemView: View, private val context: Context,
    private val itemViewModel: AmityMembershipItemViewModel,
    private val communityMemberViewModel: AmityCommunityMembersViewModel
) : RecyclerView.ViewHolder(itemView) {

    fun sendReportUser(ekoUser: AmityUser, isReport: Boolean) {
        val viewModel = if (isReport) {
            itemViewModel.reportUser(ekoUser)
        } else {
            itemViewModel.unReportUser(ekoUser)
        }
        viewModel.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(itemView)
            .doOnComplete {
                showDialogSentReportMessage(isReport)
            }.doOnError {
                handleNoPermissionError(it)
            }.subscribe()
    }

    private fun showDialogSentReportMessage(isReport: Boolean) {
        val messageSent = if (isReport) {
            R.string.amity_report_sent
        } else {
            R.string.amity_unreport_sent
        }
        Snackbar.make(
            (context as AppCompatActivity).findViewById(android.R.id.content),
            context.getString(messageSent),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun showRemoveUserDialog(ekoUser: AmityUser) {
        AmityAlertDialogUtil.showDialog(context,
            context.getString(R.string.amity_remove_from_community),
            context.getString(R.string.amity_remove_user_msg),
            context.getString(R.string.amity_remove),
            context.getString(R.string.amity_cancel),
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    removeUser(ekoUser)
                } else {
                    dialog?.cancel()
                }
            })
    }

    private fun removeUser(ekoUser: AmityUser) {
        val list = listOf(ekoUser.getUserId())
        itemViewModel.removeUser(list).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(itemView)
            .doOnComplete {
                communityMemberViewModel.updateSelectedMembersList(
                    AmitySelectMemberItem(
                        ekoUser.getUserId(),
                        ekoUser.getAvatar()?.getUrl(AmityImage.Size.MEDIUM) ?: "",
                        ekoUser.getDisplayName()
                            ?: context.getString(R.string.amity_anonymous),
                        ekoUser.getDescription(),
                        false
                    )
                )
            }.doOnError {
                handleNoPermissionError(it)
            }.subscribe()
    }

    fun removeModerator(communityMember: AmityCommunityMember) {
        val roles = communityMember.getRoles()
        val userId = communityMember.getUserId()

        val moderatorRoles = roles.filter { it == AmityConstants.MODERATOR_ROLE
                || it == AmityConstants.CHANNEL_MODERATOR_ROLE
                || it == AmityConstants.COMMUNITY_MODERATOR_ROLE }

        itemViewModel.removeRole(moderatorRoles, listOf(userId))
            .doOnError {
                handleNoPermissionError(it)
            }
            .doOnComplete { }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(itemView)
            .subscribe()
    }

    fun handleNoPermissionError(exception: Throwable) {
        if (exception is AmityException) {
            if (exception.code == AmityConstants.NO_PERMISSION_ERROR_CODE) {
                AmityAlertDialogUtil.showNoPermissionDialog(context,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog?.dismiss()
                        checkUserRole()
                    })
            } else {
                Log.e("EkoCommBaseViewHolder", "${exception.message}")
            }
        } else {
            Log.e("EkoCommBaseViewHolder", "${exception.message}")
        }
    }

    private fun checkUserRole() {
        itemViewModel.getCommunityDetail().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .untilLifecycleEnd(itemView)
            .firstOrError()
            .doOnSuccess {
                if (!it.isJoined()) {
                    val intent = Intent(
                        context,
                        AmityCommunityHomePageActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    (context as AppCompatActivity).startActivity(intent)
                }
            }.doOnError {
                Log.e("EkoCommBaseViewHolder", "checkUserRole: ${it.localizedMessage}")
            }.subscribe()
    }
}