package com.amity.socialcloud.uikit.common.networkconfig

import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface AmityNetworkConfigApi {
    @GET("api/v3/network-settings/uikit")
    fun getNetworkConfig(): Single<JsonObject>
}