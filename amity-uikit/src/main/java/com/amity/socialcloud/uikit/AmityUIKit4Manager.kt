package com.amity.socialcloud.uikit

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.encryption.AmityDBEncryption
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.video.AmityStreamBroadcasterClient
import com.amity.socialcloud.sdk.video.AmityStreamPlayerClient
import com.amity.socialcloud.uikit.common.ad.AmityAdEngine
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.infra.db.AmityUIKitDB
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext

object AmityUIKit4Manager {

    val behavior = AmityUIKit4Behavior()

    fun setup(
        apiKey: String,
        endpoint: AmityEndpoint,
        dbEncryption: AmityDBEncryption = AmityDBEncryption.NONE
    ) {
        AmityCoreClient.setup(
            apiKey = apiKey,
            endpoint = endpoint,
            dbEncryption = dbEncryption
        )

        AmityStreamBroadcasterClient.setup(AmityCoreClient.getConfiguration())
        AmityStreamPlayerClient.setup(AmityCoreClient.getConfiguration())

        AmityUIKitDB.init()
        AmityAdEngine.init()
        AmityUIKitConfigController.setup(AmityAppContext.getContext())

        overrideCustomBehavior()
    }

    private fun overrideCustomBehavior() {
        behavior.viewStoryPageBehavior = AmityCustomViewStoryPageBehavior()
        behavior.viewStoryPageBehavior = AmityCustomViewStoryPageBehavior()
        behavior.communitySearchResultComponentBehavior =
            AmityCustomCommunitySearchResultComponentBehavior()
        behavior.userSearchResultComponentBehavior = AmityCustomUserSearchResultComponentBehavior()
        behavior.myCommunitiesComponentBehavior = AmityCustomMyCommunitiesComponentBehavior()
        behavior.postContentComponentBehavior = AmityCustomPostContentComponentBehavior()
        behavior.communityProfilePageBehavior = AmityCustomCommunityProfilePageBehavior()
    }
}