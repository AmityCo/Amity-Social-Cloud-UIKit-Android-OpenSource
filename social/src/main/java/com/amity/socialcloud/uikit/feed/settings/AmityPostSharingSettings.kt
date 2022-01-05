package com.amity.socialcloud.uikit.feed.settings

class AmityPostSharingSettings {

    var privateCommunityPostSharingTarget = listOf(AmityPostSharingTarget.OriginFeed)
    var publicCommunityPostSharingTarget = listOf(
        AmityPostSharingTarget.OriginFeed,
        AmityPostSharingTarget.MyFeed,
        AmityPostSharingTarget.PublicCommunity,
        AmityPostSharingTarget.PrivateCommunity
    )
    var myFeedPostSharingTarget = listOf(
        AmityPostSharingTarget.OriginFeed,
        AmityPostSharingTarget.MyFeed,
        AmityPostSharingTarget.PublicCommunity,
        AmityPostSharingTarget.PrivateCommunity
    )
    var userFeedPostSharingTarget = listOf(
        AmityPostSharingTarget.OriginFeed,
        AmityPostSharingTarget.MyFeed,
        AmityPostSharingTarget.PublicCommunity,
        AmityPostSharingTarget.PrivateCommunity
    )

}