package com.amity.socialcloud.uikit.common.ad

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertSeparators
import androidx.paging.map
import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.social.story.AmityStory

class AmityAdInjector<T : Any>(
    placement: AmityAdPlacement,
    communityId: String?
) {
    private val ads = AmityAdEngine.getRecommendedAds(
        count = Integer.MAX_VALUE,
        placement = placement,
        communityId = communityId
    ).blockingGet()
    private var adIndex = 0
    private val frequencyType = AmityAdEngine.getAdFrequency(placement)?.getType() ?: "fixed"

    private val frequencyValue = AmityAdEngine.getAdFrequency(placement)?.getValue() ?: 0
    private var latestMapId = 0
    private val source: LinkedHashMap<Int, LinkedHashMap<String, String>> = linkedMapOf()

    private fun getId(item: T): String {
        return when (item) {
            is AmityComment -> item.getCommentId()
            is AmityPost -> item.getPostId()
            is AmityStory -> item.getStoryId()
            else -> ""
        }
    }

    private fun incrementAdIndex() {
        adIndex++
        if (adIndex >= ads.size) {
            adIndex = 0
        }
    }

    fun inject(pagingData: PagingData<T>): PagingData<AmityListItem> {
        val shouldInject = ads.isNotEmpty()
        if (!shouldInject) {
            return mapToListItem(pagingData)
        }
        val mapId = latestMapId
        latestMapId++
        var separateCount = 0
        var lastInjectedAtSeparateCount = 0
        var lastInjectedParentId = ""
        var hasAddedAllFromLastVersion = false
        return mapToListItem(pagingData
            .insertSeparators(
                TerminalSeparatorType.SOURCE_COMPLETE
            ) { before: T?, after: T? ->
                var separator: AmityAd? = null

                // check if separator should be ad or null
                if (before != null) {
                    if (source[mapId] == null) {
                        source[mapId] = linkedMapOf()
                    }
                    source[mapId]!![getId(before)] = ""
                    if (
                        mapId == 0
                        || (source[mapId - 1]?.entries?.firstOrNull { it.value.isNotEmpty() } == null)
                    ) {
                        val shouldInjectAd =
                            if (frequencyType == "fixed") {
                                Math.floorMod(
                                    separateCount,
                                    frequencyValue
                                ) == 0
                            } else separateCount == 1
                        if (shouldInjectAd) {
                            val ad = ads.get(adIndex)
                            incrementAdIndex()
                            source[mapId]!![getId(before)] = ad.getAdId()
                            separator = ad
                        }
                    } else {
                        val previousVersion = source[mapId - 1]
                        if (previousVersion != null) {
                            val alreadyInjectedAd =
                                previousVersion[getId(before)]?.isNotEmpty() ?: false
                            if (alreadyInjectedAd) {
                                // also inject at the same position in this version
                                source[mapId]!![getId(before)] = previousVersion[getId(before)]!!
                                lastInjectedAtSeparateCount = separateCount
                                lastInjectedParentId = getId(before)
                                if (previousVersion.entries.lastOrNull { it.value.isNotEmpty() }
                                        ?.let { it.key == lastInjectedParentId } == true) {
                                    hasAddedAllFromLastVersion = true
                                }
                                // return ad
                                val ad =
                                    ads.find { it.getAdId() == previousVersion[getId(before)]!! }
                                separator = ad
                            } else {
                                val shouldInjectAd =
                                    if (frequencyType == "fixed") {
                                        ((lastInjectedAtSeparateCount == 0)
                                                && (previousVersion.keys.isEmpty())
                                                && (separateCount == frequencyValue))
                                                || ((separateCount - lastInjectedAtSeparateCount == frequencyValue)
                                                && hasAddedAllFromLastVersion)
                                    } else false
                                if (shouldInjectAd) {
                                    val ad = ads.get(adIndex)
                                    incrementAdIndex()
                                    source[mapId]!![getId(before)] = ad.getAdId()
                                    lastInjectedAtSeparateCount = separateCount
                                    lastInjectedParentId = getId(before)
                                    // return ad
                                    separator = ad
                                }
                            }
                        }
                    }
                }
                // catch edge cases, where last item with ad is removed
                if (before != null && after != null && mapId > 0) {
                    val previousVersion = source[mapId - 1]
                    val previousVersionAfterIndex =
                        (previousVersion?.entries?.indexOfFirst { it.key == getId(before) }
                            ?: -1) + 1
                    if (previousVersion != null
                        && previousVersionAfterIndex > 0
                        && (previousVersion.keys.size - 1) > previousVersionAfterIndex
                    ) {
                        val previousVersionAfter =
                            previousVersion.entries.toList().get(previousVersionAfterIndex)
                        if (
                            previousVersionAfter.key != getId(after)
                            && (previousVersion.entries.lastOrNull { it.value.isNotEmpty() }
                                ?.let { it.key == previousVersionAfter.key } == true)
                        ) {
                            hasAddedAllFromLastVersion = true
                        }
                    }
                }
                separateCount++
                separator
            })
    }

    private fun mapToListItem(pagingData: PagingData<*>): PagingData<AmityListItem> {
        return pagingData.map { item ->
            when (item) {
                is AmityComment -> AmityListItem.CommentItem(item)
                is AmityPost -> AmityListItem.PostItem(item)
                is AmityStory -> AmityListItem.StoryItem(item)
                is AmityAd -> AmityListItem.AdItem(item)
                else -> AmityListItem.Separator
            }
        }
    }

}