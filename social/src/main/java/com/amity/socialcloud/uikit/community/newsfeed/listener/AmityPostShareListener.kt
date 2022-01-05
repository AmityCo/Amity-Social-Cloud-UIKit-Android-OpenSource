package com.amity.socialcloud.uikit.community.newsfeed.listener

import com.amity.socialcloud.uikit.common.utils.AmitySingleLiveData
import com.amity.socialcloud.uikit.community.utils.AmityShareType

interface AmityPostShareListener {

    val shareToMyTimelineActionRelay: AmitySingleLiveData<Unit>
    val shareToGroupActionRelay: AmitySingleLiveData<Unit>
    val shareToExternalAppActionRelay: AmitySingleLiveData<Unit>

    fun observeShareToMyTimelinePage(): AmitySingleLiveData<Unit> = shareToMyTimelineActionRelay
    fun observeShareToPage(): AmitySingleLiveData<Unit> = shareToGroupActionRelay
    fun observeShareToExternalApp(): AmitySingleLiveData<Unit> = shareToExternalAppActionRelay

    fun navigateShareTo(type: AmityShareType) {
        when (type) {
            AmityShareType.MY_TIMELINE -> {
                shareToMyTimelineActionRelay.postValue(Unit)
            }
            AmityShareType.GROUP -> {
                shareToGroupActionRelay.postValue(Unit)
            }
            AmityShareType.EXTERNAL -> {
                shareToExternalAppActionRelay.postValue(Unit)
            }
        }
    }

}