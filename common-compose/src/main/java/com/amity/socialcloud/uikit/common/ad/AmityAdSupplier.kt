package com.amity.socialcloud.uikit.common.ad

import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import kotlin.math.pow

object AdSupplier {
    fun recommendAds(
        count: Int,
        placement: AmityAdPlacement,
        communityId: String?,
        ads: List<AmityAd>
    ): List<AmityAd> {
        // calculate impression age
        val impressionAges = calculateImpressionAges(ads)
        // calculate score for all ads
        val scores = mutableMapOf<String, Double>()
        ads.forEach { ad ->
            val relevancy = if (ad.getAdTarget().getCommunityIds().contains(communityId)) 1 else 0
            val impressionAge = impressionAges[ad.getAdId()] ?: 0.0
            val score = relevancy + Math.E.pow(2 * impressionAge)
            scores[ad.getAdId()] = score
        }
        return selectAdsByWeightedRandomChoice(ads, scores, count)
    }

    private fun calculateImpressionAges(ads: List<AmityAd>): Map<String, Double> {
        val adRecencyRepository = AmityAdRecencyRepository()
        val recencySortedAds = ads.sortedByDescending { ad ->
            adRecencyRepository.getLastSeen(ad.getAdId())
        }
        val impressionAges = mutableMapOf<String, Double>()
        val maxLastSeen = adRecencyRepository.getLastSeen(recencySortedAds.firstOrNull()?.getAdId())
        val minLastSeen = adRecencyRepository.getLastSeen(recencySortedAds.lastOrNull()?.getAdId())

        if (maxLastSeen == minLastSeen) {
            recencySortedAds.forEach { ad ->
                impressionAges[ad.getAdId()] = 1.0
            }
        } else {
            recencySortedAds.forEachIndexed { index, ad ->
                val impressionAge = index.toDouble() / (recencySortedAds.size - 1)
                impressionAges[ad.getAdId()] = impressionAge
            }
        }
        return impressionAges
    }

    private fun selectAdsByWeightedRandomChoice(
        ads: List<AmityAd>,
        scores: Map<String, Double>,
        count: Int
    ): List<AmityAd> {
        val selectedAds = mutableListOf<AmityAd>()
        val adsCopy = ads.toMutableList()

        while (selectedAds.size < count && adsCopy.isNotEmpty()) {
            val totalScore = scores.values.sum()
            val weights = adsCopy.map { ad ->
                scores[ad.getAdId()] ?: (0.0 / totalScore)
            }
            val likelihoods = weights.map { it / weights.sum() }
            val selectedAdIndex = weightedRandomChoice(likelihoods)
            selectedAds.add(adsCopy[selectedAdIndex])
            adsCopy.removeAt(selectedAdIndex)
        }
        return selectedAds
    }

    private fun weightedRandomChoice(weights: List<Double>): Int {
        val randomValue = Math.random()
        var cumulativeWeight = 0.0
        for (i in weights.indices) {
            cumulativeWeight += weights[i]
            if (randomValue < cumulativeWeight) {
                return i
            }
        }
        return weights.size - 1
    }

}