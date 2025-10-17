package com.amity.socialcloud.uikit.common.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

fun AppCompatActivity.getFragmentViewModelStoreOwner(fragment: Fragment) : ViewModelStoreOwner {
    val owners = ViewModelProvider(this)[FragmentStoreOwnerMap::class.java].owners
    var owner = owners.get(fragment.hashCode().toString())
    if(owner == null) {
        owner = FragmentStoreOwner()
        owners.put(fragment.hashCode().toString(), owner)
    }
    return owner
}

fun FragmentActivity.getFragmentViewModelStoreOwner(fragment: Fragment) : ViewModelStoreOwner {
    val owners = ViewModelProvider(this)[FragmentStoreOwnerMap::class.java].owners
    var owner = owners.get(fragment.hashCode().toString())
    if(owner == null) {
        owner = FragmentStoreOwner()
        owners.put(fragment.hashCode().toString(), owner)
    }
    return owner
}

fun <T: ViewModel>AppCompatActivity.createFragmentViewModel(fragment: Fragment, clazz: Class<T>) : T {
    return ViewModelProvider(getFragmentViewModelStoreOwner(fragment)).get(clazz)
}

fun <T: ViewModel>FragmentActivity.createFragmentViewModel(fragment: Fragment, clazz: Class<T>) : T {
    return ViewModelProvider(getFragmentViewModelStoreOwner(fragment)).get(clazz)
}