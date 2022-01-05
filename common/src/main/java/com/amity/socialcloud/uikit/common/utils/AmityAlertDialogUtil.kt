package com.amity.socialcloud.uikit.common.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AmityAlertDialogUtil {

    fun showDialog(context: Context, title: String, msg: String, positiveButton: String,
                   negativeButton: String?, listener: DialogInterface.OnClickListener) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positiveButton) { dialog, _ ->
                    listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
                }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton) { dialog, _ ->
                listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)

            }
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.amityColorPrimary))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.amityColorPrimary))
        }
        dialog.show()
    }
    
    fun showDialog(context: Context, title: String, msg: String,
                   positiveButton: String, negativeButton: String?,
                   cancelable : Boolean, listener: DialogInterface.OnClickListener) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(title)
            .setMessage(msg)
            .setPositiveButton(positiveButton) { dialog, _ ->
                listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
            }
        if (negativeButton != null) {
            builder.setNegativeButton(negativeButton) { dialog, _ ->
                listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
            }
        }
            builder.setCancelable(cancelable)
        
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.amityColorPrimary))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.amityColorPrimary))
        }
        dialog.show()
    }

    fun showNoPermissionDialog(context: Context, listener: DialogInterface.OnClickListener) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(context.getString(R.string.amity_no_permission_title))
                .setMessage(context.getString(R.string.amity_no_permission_message))
                .setPositiveButton(context.getText(R.string.amity_ok)) { dialog, _ ->
                    listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
                }
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.amityColorPrimary))
        }
        dialog.show()
    }

    fun checkConfirmDialog(isPositive: Int, confirmed: () -> Unit, cancel: () -> Unit = {}) {
        if (isPositive == DialogInterface.BUTTON_POSITIVE) {
            confirmed.invoke()
        } else {
            cancel.invoke()
        }
    }

}