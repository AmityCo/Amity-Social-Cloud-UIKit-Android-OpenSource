package com.amity.socialcloud.uikit.common.base

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.amity.socialcloud.uikit.common.utils.AmityThemeUtil
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

/**
 * Base activity to be extended by all activities of application.
 * @author sumitlakra
 * @date 06/01/2020
 */
abstract class AmityBaseActivity<T : ViewDataBinding, V : AmityBaseViewModel> :
    RxAppCompatActivity() {

    protected lateinit var mViewDataBinding: T
    private var mViewModel: V? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    abstract fun getBindingVariable(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        AmityThemeUtil.setCurrentTheme(this)
        super.onCreate(savedInstanceState)
        performDataBinding()
    }


    /**
     * to change app theme between day/night mode
     * @author sumitlakra
     * @date 06/01/2020
     */
    fun changeTheme() {
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * Function to get ViewDataBinding
     * @return [T] ViewDataBinding
     * @author sumitlakra
     * @date 06/01/2020
     */
    fun getViewDataBinding(): T = mViewDataBinding

    /**
     * Function to execute ViewDataBinding
     * @author Sumit Lakra
     * @date 12/07/19
     */
    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.executePendingBindings()
    }

    fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val status = ContextCompat.checkSelfPermission(
                this,
                permission
            )
            return (status == PackageManager.PERMISSION_GRANTED)
        }
        return true
    }

    fun requestPermission(permission: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    permission
                ), requestCode
            )
        } else{
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    fun requestPermission(permission: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                    this, permission, requestCode
            )
        } else{
            requestPermissions(permission, requestCode)
        }
    }


}