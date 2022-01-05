package com.amity.socialcloud.uikit.community.home.fragments

import androidx.databinding.ObservableBoolean
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel

class AmityCommunityHomeViewModel : AmityBaseViewModel() {
    var isSearchMode = ObservableBoolean(false)
    val emptySearchString = ObservableBoolean(true)
}