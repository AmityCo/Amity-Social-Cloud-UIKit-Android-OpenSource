package com.amity.socialcloud.uikit.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner

class FragmentStoreOwnerMap: ViewModel() {
    val owners : HashMap<String, FragmentStoreOwner>  = hashMapOf<String, FragmentStoreOwner>()
}