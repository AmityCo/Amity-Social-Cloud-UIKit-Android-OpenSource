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
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


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

        val userId = AmityCoreClient.getUserId()
        if (userId.isNotEmpty()) {
            binding.etUserId.setText(userId)
            binding.etUserName.setText(userId)

            registerDevice(userId)
        }

        setContentView(binding.root)

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

            btnEnv.setOnClickListener {
                val env = Environment(
                    SamplePreferences.getApiKey().get(),
                    SamplePreferences.getHttpUrl().get(),
                    SamplePreferences.getSocketUrl().get(),
                    SamplePreferences.getMqttBroker().get()
                )
                changeEnvContract.launch(env)
            }
        }
    }

    private fun registerDevice(userId: String, displayName: String? = "") {
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

}
