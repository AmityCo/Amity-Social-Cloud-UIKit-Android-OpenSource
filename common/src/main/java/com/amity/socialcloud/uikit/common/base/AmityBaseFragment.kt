package com.amity.socialcloud.uikit.common.base

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.disposables.CompositeDisposable

open class AmityBaseFragment : RxFragment() {
    protected var disposable: CompositeDisposable = CompositeDisposable()
    var consumeBackPress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenBackPress()
    }

    private fun listenBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (consumeBackPress) {
                    handleBackPress()
                } else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    open fun handleBackPress() {
        backPressFragment()
    }

    fun backPressFragment() {
        consumeBackPress = false
        activity?.onBackPressed()
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }

    fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                val status = ContextCompat.checkSelfPermission(
                        it,
                        permission
                )
                return (status == PackageManager.PERMISSION_GRANTED)
            }
        }
        return true
    }

    fun requestPermission(permission: String, requestCode: Int) {
        (activity as? Activity)?.let { activity ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    requestCode
                )
            } else{
                requestPermissions(arrayOf(permission), requestCode)
            }
        }
    }

    fun requestPermission(permission: Array<String>, requestCode: Int) {
        (activity as? Activity)?.let { activity ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    activity,
                    permission,
                    requestCode
                )
            } else{
                requestPermissions(permission, requestCode)
            }
        }

    }
}