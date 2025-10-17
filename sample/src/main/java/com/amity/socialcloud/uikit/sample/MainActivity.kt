package com.amity.socialcloud.uikit.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.AccessTokenRenewal
import com.amity.socialcloud.sdk.model.core.session.SessionHandler
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.sample.databinding.AmityActivityMainBinding
import com.amity.socialcloud.uikit.sample.env.Environment
import com.amity.socialcloud.uikit.sample.env.EnvironmentActivity
import com.amity.socialcloud.uikit.sample.env.SamplePreferences
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : RxAppCompatActivity() {

    private val binding: AmityActivityMainBinding by lazy {
        AmityActivityMainBinding.inflate(layoutInflater)
    }

    private val changeEnvContract = registerForActivityResult(
        EnvironmentActivity.ChangeEnvironmentContract()
    ) {
        if (it != null) {
            SamplePreferences.getApiKey().set(it.apiKey)
            SamplePreferences.getHttpUrl().set(it.httpUrl)
            SamplePreferences.getSocketUrl().set(it.socketUrl)
            SamplePreferences.getMqttBroker().set(it.mqttBroker)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userId = AmityCoreClient.getUserId()
        if (userId.isNotEmpty()) {
            binding.etUserId.setText(userId)
            binding.etUserName.setText(userId)

            registerDevice(userId)
        }

        binding.apply {
            btnLogin.setOnClickListener {
                if (etUserId.text.isNotEmpty() && etUserName.text.isNotEmpty()) {
                    registerDevice(
                        etUserId.text.toString().trim(),
                        etUserName.text.toString().trim()
                    )
                } else {
                    findViewById<View>(android.R.id.content).showSnackBar(
                        "Enter userId and Display Name",
                        Snackbar.LENGTH_SHORT
                    )
                }
            }
            etSecureVisitorUrl.setText("https://472sfz2bt3cddangdvv5nlyjjq0pnshz.lambda-url.ap-southeast-1.on.aws")
            btnVisitorLogin.setOnClickListener {
                val secureVisitorUrl = etSecureVisitorUrl.text.toString().trim()
                if (secureVisitorUrl.isNotEmpty()) {
                    val retrofitInstance = SampleRetrofitProvider.getInstance(SamplePreferences.getHttpUrl().get())
                    val api = retrofitInstance.create(SecureService::class.java)
                    val visitorDeviceId = AmityCoreClient.getVisitorDeviceId()
                    val expiresAt = DateTime.now().plusDays(30).toDateTime(DateTimeZone.UTC)
                    api
                        .getAuthSignature(
                            url = secureVisitorUrl,
                            deviceId = visitorDeviceId,
                            authSignatureExpiresAt = expiresAt
                        )
                        .enqueue(object : Callback<JsonObject> {
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            val json: JsonObject? = response.body()
                            val authSignature =  try {
                                json?.get("signature")?.asString ?: ""
                            } catch (e: Exception) {
                                ""
                            }
                            registerDevice(
                                null,
                                etUserName.text.toString().trim(),
                                authSignature = authSignature,
                                authSignatureExpiresAt = expiresAt
                            )
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Timber.e(t, "API call failed")
                        }
                    })
                } else {
                    registerDevice(
                        null,
                        etUserName.text.toString().trim()
                    )
                }
            }

            btnEnv.setOnClickListener {
                val env = Environment(
                    SamplePreferences.getApiKey().get(),
                    SamplePreferences.getHttpUrl().get(),
                    SamplePreferences.getSocketUrl().get(),
                    SamplePreferences.getMqttBroker().get()
                )
                SampleRetrofitProvider.reset() // Reset Retrofit when environment changes
                changeEnvContract.launch(env)
            }
        }
    }

    private fun registerDevice(
        userId: String?,
        displayName: String? = "",
        authSignature: String? = null,
        authSignatureExpiresAt: DateTime? = null,
    ) {
        // Adding delay to avoid race condition when setup SDK and user is registered immediately
        Single.just(true)
            .delay(200, TimeUnit.MILLISECONDS)
            .flatMapCompletable {
                if (userId.isNullOrEmpty()) {
                    AmityCoreClient.loginAsVisitor(object : SessionHandler {
                        override fun sessionWillRenewAccessToken(renewal: AccessTokenRenewal) {
                            renewal.renew()
                        }
                    })
                        .apply {
                            if (!displayName.isNullOrEmpty()) {
                                displayName(displayName)
                            }
                            if (!authSignature.isNullOrEmpty() && authSignatureExpiresAt != null) {
                                authSignature(authSignature)
                                authSignatureExpiresAt(authSignatureExpiresAt)
                            }
                        }
                        .build()
                        .submit()
                } else {
                    AmityCoreClient.login(userId, object : SessionHandler {
                        override fun sessionWillRenewAccessToken(renewal: AccessTokenRenewal) {
                            renewal.renew()
                        }
                    })
                        .apply {
                            if (!displayName.isNullOrEmpty()) {
                                displayName(displayName)
                            }
                        }
                        .build()
                        .submit()
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    registerForPushNotifications()
                    val intent = Intent(this, AmitySettingActivity::class.java)
                    startActivity(intent)
                }
                .doOnError {
                    findViewById<View>(android.R.id.content).showSnackBar("Could not register user " + it.message)
                }
                .untilLifecycleEnd(this)
                .subscribe()
    }

    private fun registerForPushNotifications() {
        AmityCoreClient.registerPushNotification()
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                Timber.e("registerForPushNotifications: success for userId ${AmityCoreClient.getUserId()}")
            }
            .subscribe()
    }

    // Retrofit API interface
    interface SecureService {
        @GET
        fun getAuthSignature(@Url url: String, @Query("deviceId") deviceId: String, @Query("authSignatureExpiresAt") authSignatureExpiresAt: DateTime): Call<JsonObject>
    }
}
