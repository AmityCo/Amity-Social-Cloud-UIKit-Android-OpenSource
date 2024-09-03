package com.amity.socialcloud.uikit.common.base

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class FragmentStoreOwner : ViewModelStoreOwner {
    private val store = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return store
    }
}