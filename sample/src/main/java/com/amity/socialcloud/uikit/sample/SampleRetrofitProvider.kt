package com.amity.socialcloud.uikit.sample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SampleRetrofitProvider {
    @Volatile
    private var retrofit: Retrofit? = null

    fun getInstance(baseUrl: String): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .also { retrofit = it }
        }
    }

    fun reset() {
        retrofit = null
    }
}

