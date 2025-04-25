package com.amity.socialcloud.uikit.common.networkconfig

import android.util.Log
import com.amity.socialcloud.sdk.api.core.infra.AmitySharedInfra
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.infra.db.AmityUIKitDB
import com.amity.socialcloud.uikit.common.infra.db.entity.AmityNetworkConfig
import com.amity.socialcloud.uikit.common.infra.initializer.AmityAppContext
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

object AmityNetworkConfigService {

    fun init(uniqueKey: String) {
        Completable.fromCallable {
            val networkConfigDao = AmityUIKitDB.get().networkConfigDao()
            val networkConfig = networkConfigDao.getNetworkConfig()
            if(networkConfig == null) {
                networkConfigDao.insert(
                    AmityNetworkConfig(
                        id = uniqueKey.hashCode().toString(),
                        config = null
                    )
                )
            } else if(networkConfig.id != uniqueKey.hashCode().toString()) {
                    networkConfigDao.deleteAll()
                    networkConfigDao.insert(
                        AmityNetworkConfig(
                            id = uniqueKey.hashCode().toString(),
                            config = null
                        )
                    )
            } else {
                // retain config
            }
        }.subscribeOn(Schedulers.io())
            .doOnError {
                // Do nothing
            }
            .subscribe()
    }

    fun syncNetworkConfig(): Completable {
        return AmitySharedInfra().getApi(AmityNetworkConfigApi::class)
            .subscribeOn(Schedulers.io())
            .flatMap {
                it.getNetworkConfig()
            }.flatMapCompletable { responseJson ->
                val networkConfigDao = AmityUIKitDB.get().networkConfigDao()
                val networkConfig = networkConfigDao.getNetworkConfig()
                if(networkConfig == null) {
                    val exception = AmityException.create("Ensure setup() is called before syncNetworkConfig()", null, AmityError.BUSINESS_ERROR)
                    Completable.error(exception)
                }
                else {
                    val configJson = responseJson.getAsJsonObject("config")
                    val updatedConfig = AmityNetworkConfig(
                        id = networkConfig.id,
                        config = configJson
                    )
                    networkConfigDao.upsert(updatedConfig)
                    AmityUIKitConfigController.setup(AmityAppContext.getContext())
                    Completable.complete()
                }
            }
    }

    fun getNetworkConfig(): AmityNetworkConfig? {
        return AmityUIKitDB.get().networkConfigDao().getNetworkConfig()
    }

}