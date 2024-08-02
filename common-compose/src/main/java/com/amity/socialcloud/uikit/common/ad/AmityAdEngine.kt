package com.amity.socialcloud.uikit.common.ad

import android.app.DownloadManager
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.core.ad.AmityAdFrequency
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.sdk.model.core.ad.AmityAdsSettings
import com.amity.socialcloud.sdk.model.core.ad.AmityNetworkAds
import com.amity.socialcloud.sdk.model.core.ad.analytics
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityAdAsset
import com.amity.socialcloud.uikit.common.infra.download.AmityDownloader
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.joda.time.DateTime

object AmityAdEngine {

    private var ads = listOf<AmityAd>()
    private var settings: AmityAdsSettings? = null
    private var disposable: CompositeDisposable = CompositeDisposable()

    fun init() {
        disposable.clear()
        disposable.add(
            AmityCoreClient.observeSessionState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                when (it) {
                    is SessionState.Established -> {
                        setup()
                    }

                    is SessionState.Terminated, SessionState.NotLoggedIn -> {
                        clearData()
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
        )
    }

    private fun clearData() {
        val allAssets = AmityAdAssetRepository().getAllAdAssets()
        allAssets.forEach {
            val id = it.downloadId
            if (id != -1L) {
                AmityDownloader.remove(id, it.fileUrl)
            }
        }
        AmityAdAssetRepository().deleteAll()
        AmityAdRecencyRepository().deleteAll()
        AmityAdTimeWindowTracker.clear()
    }

    private fun setup() {
        AmityCoreClient.newAdRepository()
            .getNetworkAds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                consumeNetworkAds(it)
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun consumeNetworkAds(networkAds: AmityNetworkAds) {
        settings = networkAds.getSettings()
        ads = networkAds.getAds()
        diffAssets(generateAssets(ads))
        refreshDownloadStatuses()
    }

    private fun generateAssets(ads: List<AmityAd>) = ads.flatMap { ad ->
        listOfNotNull(ad.getImage9_16()?.getUrl(), ad.getImage1_1()?.getUrl())
    }.map { AmityAdAsset(it) }

    private fun diffAssets(newAssets: List<AmityAdAsset>) {
        val repository = AmityAdAssetRepository()
        val oldAssets = repository.getAllAdAssets()
        val newAssetSet = newAssets.toSet()

        val assetsToDelete = oldAssets.filterNot { newAssetSet.contains(it) }
        val assetsToInsert = newAssets.filterNot { oldAssets.contains(it) }

        assetsToDelete.forEach {
            val id = it.downloadId
            if (id != -1L) {
                AmityDownloader.remove(id, it.fileUrl)
            }
            repository.deleteAdAsset(it.fileUrl)
        }

        assetsToInsert.forEach {
            repository.insertAdAsset(it)
        }
    }

    private fun refreshDownloadStatuses() {
        val repository = AmityAdAssetRepository()
        val assets = repository.getAllAdAssets()
        assets.forEach {
            if (it.downloadStatus == -1 || it.downloadStatus == DownloadManager.STATUS_FAILED) {
                val id = AmityDownloader.enqueue(it.fileUrl)
                repository.updateDownloadId(it.fileUrl, id)
                repository.updateDownloadStatus(id, AmityDownloader.getStatus(id))
            } else if (it.downloadStatus != DownloadManager.STATUS_SUCCESSFUL) {
                repository.updateDownloadStatus(it.downloadId, AmityDownloader.getStatus(it.downloadId))
            }
        }
    }

    fun getAdFrequency(placement: AmityAdPlacement): AmityAdFrequency? {
        val frequencySettings = settings?.getFrequency()
        return if (frequencySettings == null) {
            null
        } else {
            when (placement) {
                AmityAdPlacement.FEED -> frequencySettings.getFeedAdFrequency()
                AmityAdPlacement.STORY -> frequencySettings.getStoryAdFrequency()
                AmityAdPlacement.COMMENT -> frequencySettings.getCommentAdFrequency()
                else -> null
            }
        }
    }

    fun markSeen(ad: AmityAd, placement: AmityAdPlacement) {
        AmityAdRecencyRepository().updateLastSeen(ad.getAdId(), DateTime.now())
        if (getAdFrequency(placement)?.getType() == "time-window") {
            AmityAdTimeWindowTracker.markSeen(placement)
        }
        ad.analytics().markAsSeen(placement)
    }

    fun markClicked(ad: AmityAd, placement: AmityAdPlacement) {
        ad.analytics().markLinkAsClicked(placement)
    }

    fun getRecommendedAds(
        count: Int,
        placement: AmityAdPlacement,
        communityId: String?
    ): Single<List<AmityAd>> {
        return getApplicableAds(placement, communityId)
            .map {
                if (it.isEmpty()) {
                    return@map it
                } else {
                    AdSupplier.recommendAds(count, placement, communityId, it)
                }
            }
    }

    private fun getApplicableAds(placement: AmityAdPlacement, communityId: String?): Single<List<AmityAd>> {
        return Single.fromCallable {
            if (settings == null
                || !settings!!.isEnabled()
                || (getAdFrequency(placement)?.getType() == "time-window" && AmityAdTimeWindowTracker.hasReachedTimeWindowLimit(placement))
            ) {
                emptyList<AmityAd>()
            } else {
                val readyAds = ads.filter {
                    it.getPlacements().contains(placement)
                            && (it.getEndAt() == null || it.getEndAt()?.isAfterNow == true)
                            && (getUrlByPlacement(it, placement)?.let { url ->
                        AmityAdAssetRepository().getAdAsset(url)?.downloadStatus == DownloadManager.STATUS_SUCCESSFUL
                    } ?: false)
                }

                // Attempt to return targeted ads first, then fallback to non-targeted ads
                val applicableAds = if (communityId != null) {
                    val targetedAds = readyAds.filter { it.getAdTarget().getCommunityIds().contains(communityId) }
                    targetedAds.ifEmpty { readyAds.filter { it.getAdTarget().getCommunityIds().isEmpty() } }
                } else {
                    readyAds.filter { it.getAdTarget().getCommunityIds().isEmpty() }
                }

                applicableAds
            }
        }
    }

    private fun getUrlByPlacement(ad: AmityAd, placement: AmityAdPlacement): String? {
        return when (placement) {
            AmityAdPlacement.FEED -> ad.getImage1_1()?.getUrl()
            AmityAdPlacement.STORY -> ad.getImage9_16()?.getUrl()
            AmityAdPlacement.COMMENT -> ad.getImage1_1()?.getUrl()
            AmityAdPlacement.CHAT -> ad.getImage1_1()?.getUrl()
            AmityAdPlacement.CHATLIST -> ad.getImage1_1()?.getUrl()
            else -> null
        }
    }

    fun isEnabled(): Boolean {
        return settings?.isEnabled() ?: false
    }

}