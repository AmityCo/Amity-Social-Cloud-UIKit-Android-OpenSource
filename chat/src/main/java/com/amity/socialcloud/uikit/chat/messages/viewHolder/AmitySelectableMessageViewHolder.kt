package com.amity.socialcloud.uikit.chat.messages.viewHolder

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmitySelectableMessageViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class AmitySelectableMessageViewHolder(
    itemView: View,
    private val itemViewModel: AmitySelectableMessageViewModel,
    private val context: Context
) : AmityChatMessageBaseViewHolder(itemView, itemViewModel) {

    init {
        addViewModelListener()
    }

    private fun addViewModelListener() {
        itemViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.MESSAGE_LONG_PRESS -> showPopUp()
                AmityEventIdentifier.DELETE_MESSAGE -> {
                    showDeleteDialog()
                    itemViewModel.triggerEvent(AmityEventIdentifier.DISMISS_POPUP)
                }
                AmityEventIdentifier.REPORT_MESSAGE -> {
                    reportMessage()
                    itemViewModel.triggerEvent(AmityEventIdentifier.DISMISS_POPUP)
                }
                AmityEventIdentifier.UNREPORT_MESSAGE -> {
                    unreportMessage()
                    itemViewModel.triggerEvent(AmityEventIdentifier.DISMISS_POPUP)
                }
                AmityEventIdentifier.FAILED_MESSAGE -> {
                    showFailedMessageDialog()
                }
                else -> {

                }
            }
        }

    }

    abstract fun showPopUp()

    abstract fun setMessageData(item: AmityMessage)

    override fun setMessage(message: AmityMessage) {
        setMessageData(message)
    }

    private fun showDeleteDialog() {
        AmityAlertDialogUtil.showDialog(context, context.getString(R.string.amity_delete_msg),
            context.getString(R.string.amity_dlt_dlg_body), context.getString(R.string.amity_delete),
            context.getString(R.string.amity_cancel),
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    deleteMessage()
                } else {
                    dialog.cancel()
                }
            })
    }

    private fun showFailedMessageDialog() {
        AmityAlertDialogUtil.showDialog(context, context.getString(R.string.amity_delete_msg),
            context.getString(R.string.amity_failed_dlg_body), context.getString(R.string.amity_delete),
            context.getString(R.string.amity_cancel),
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    deleteMessage()
                } else {
                    dialog.cancel()
                }
            })
    }

    private fun deleteMessage() {
        itemViewModel.deleteMessage()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnComplete {
                itemViewModel.triggerEvent(
                    AmityEventIdentifier.MESSAGE_DELETE_SUCCESS,
                    itemViewModel.amityMessage?.getMessageId()!!
                )
            }?.doOnError {
                showDeleteFailedDialog()
            }?.subscribe()
    }

    private fun showDeleteFailedDialog() {
        AmityAlertDialogUtil.showDialog(context, context.getString(R.string.amity_unable_to_delete),
            context.getString(R.string.amity_try_again), context.getString(R.string.amity_ok),
            null,
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    dialog.cancel()
                }
            })
    }

    private fun reportMessage() {
        itemViewModel.amityMessage?.report()?.flag()
            ?.subscribe(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    CoroutineScope(Dispatchers.Main).launch {
                        val snackBar = Snackbar.make(
                            itemView,
                            context.getString(R.string.amity_report_msg), Snackbar.LENGTH_SHORT
                        )
                        snackBar.show()
                    }
                }

                override fun onError(e: Throwable) {

                }
            })
    }

    private fun unreportMessage() {
        itemViewModel.amityMessage?.report()?.unflag()
            ?.subscribe(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    CoroutineScope(Dispatchers.Main).launch {
                        val snackBar = Snackbar.make(
                            itemView,
                            context.getString(R.string.amity_report_msg), Snackbar.LENGTH_SHORT
                        )
                        snackBar.show()
                    }
                }

                override fun onError(e: Throwable) {

                }
            })
    }
}