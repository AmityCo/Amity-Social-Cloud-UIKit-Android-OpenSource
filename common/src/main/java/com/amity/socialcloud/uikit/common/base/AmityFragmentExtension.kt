package com.amity.socialcloud.uikit.common.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

fun <T: ViewModel>Fragment.createViewModel(viewModelStoreOwner: ViewModelStoreOwner = (requireActivity() as AppCompatActivity).getFragmentViewModelStoreOwner(this),
                                           clazz: Class<T>) : T {
    return ViewModelProvider(viewModelStoreOwner).get(clazz)
}