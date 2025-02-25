package com.amity.snipet.verifier.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.AccessTokenRenewal
import com.amity.socialcloud.sdk.model.core.session.SessionHandler
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageFragment
import io.reactivex.rxjava3.schedulers.Schedulers


class AmityAuthentication {
    /* begin_sample_code
     gist_id: f42082cd7a09b63990fedb15a69bb1bf
     filename: AmityAuthentication.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/community-profile-page
     description: create session
     */
    fun login(userId: String, authToken: String) {
        AmityCoreClient.login(
            userId = userId,
            sessionHandler = object : SessionHandler {
                override fun sessionWillRenewAccessToken(renewal: AccessTokenRenewal) {
                    renewal.renew()
                }
            }
        ).authToken(authToken)
            .build()
            .submit()
            .subscribeOn(Schedulers.io())
            .doOnError {
                // Exception
            }
            .subscribe()

    }
    /* end_sample_code */
}