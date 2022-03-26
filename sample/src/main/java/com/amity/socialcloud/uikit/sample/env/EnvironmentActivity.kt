package com.amity.socialcloud.uikit.sample.env

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract

import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.AmityRegionalEndpoint
import com.amity.socialcloud.uikit.sample.R
import com.amity.socialcloud.uikit.sample.databinding.ActivityEnvironmentBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EnvironmentActivity : AppCompatActivity() {

    private val binding: ActivityEnvironmentBinding by lazy {
        ActivityEnvironmentBinding.inflate(layoutInflater)
    }

    private var disposable: Disposable? = null
    private lateinit var environment: Environment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        environment = intent.getParcelableExtra(ChangeEnvironmentContract.EXTRA_ENV)!!
        getCurrentEnvironment()

        binding.btnSubmit.setOnClickListener {
            val newApiKey = binding.etApiKey.text.toString()
            val newUrl = binding.etUrl.text.toString()
            if (newApiKey.isEmpty() || newUrl.isEmpty()) {
                Toast.makeText(this, "Api key or URL can not be blank", Toast.LENGTH_SHORT).show()
            }else {
                updatePreference(newApiKey, newUrl)
            }
        }
    }

    private fun getCurrentEnvironment() {
        binding.environmentGroup.apply {
            when (getCurrentOption()) {
                R.id.radioDev -> {
                    check(R.id.radioDev)
                    setDefaultValues(R.id.radioDev)
                }
                R.id.radioStaging -> {
                    check(R.id.radioStaging)
                    setDefaultValues(R.id.radioStaging)
                }
                R.id.radioProduction -> {
                    check(R.id.radioProduction)
                    setDefaultValues(R.id.radioProduction)
                }
                R.id.radioEU -> {
                    check(R.id.radioEU)
                    setDefaultValues(R.id.radioEU)
                }
                R.id.radioUS -> {
                    check(R.id.radioUS)
                    setDefaultValues(R.id.radioUS)
                }
                else -> {
                    check(R.id.radioCustom)
                    setDefaultValues(R.id.radioCustom)
                }
            }
        }
    }

    private fun getCurrentOption() : Int {
        return when(environment.httpUrl) {
            SampleUrl.get(SampleEnv.DEV) -> {
                if(environment.apiKey == SampleAPIKey.get(SampleEnv.DEV)) R.id.radioDev else R.id.radioCustom
            }
            SampleUrl.get(SampleEnv.STAGING) -> {
                if(environment.apiKey == SampleAPIKey.get(SampleEnv.STAGING)) R.id.radioStaging else R.id.radioCustom
            }
            AmityRegionalEndpoint.SG -> {
                if(environment.apiKey == SampleAPIKey.get(SampleEnv.PRODUCTION_SG)) R.id.radioProduction else R.id.radioCustom
            }
            AmityRegionalEndpoint.EU -> {
                if(environment.apiKey == SampleAPIKey.get(SampleEnv.PRODUCTION_EU)) R.id.radioEU else R.id.radioCustom
            }
            AmityRegionalEndpoint.US -> {
                if(environment.apiKey == SampleAPIKey.get(SampleEnv.PRODUCTION_US)) R.id.radioUS else R.id.radioCustom
            }
            else -> {
                R.id.radioCustom
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        setDefaultValues(view.id)
    }

    private fun setDefaultValues(id: Int) {
        var apiKey = ""
        var url = ""
        when(id) {
            R.id.radioDev -> {
                apiKey = SampleAPIKey.get(SampleEnv.DEV)
                url = SampleUrl.get(SampleEnv.DEV)
            }
            R.id.radioStaging -> {
                apiKey = SampleAPIKey.get(SampleEnv.STAGING)
                url = SampleUrl.get(SampleEnv.STAGING)
            }
            R.id.radioProduction -> {
                apiKey = SampleAPIKey.get(SampleEnv.PRODUCTION_SG)
                url = SampleUrl.get(SampleEnv.PRODUCTION_SG)
            }
            R.id.radioEU -> {
                apiKey = SampleAPIKey.get(SampleEnv.PRODUCTION_EU)
                url = AmityRegionalEndpoint.EU
            }
            R.id.radioUS -> {
                apiKey = SampleAPIKey.get(SampleEnv.PRODUCTION_US)
                url = AmityRegionalEndpoint.US
            }
            R.id.radioCustom -> {
                apiKey = environment.apiKey
                url = environment.httpUrl
            }
        }
        binding.etApiKey.setText(apiKey)
        binding.etUrl.setText(url)
    }

    private fun updatePreference(apiKey: String, url: String) {
        environment.apply {
            this.apiKey = apiKey
            this.httpUrl = url
            this.socketUrl = url
        }

        disposable = AmityCoreClient.setup(apiKey, url, url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                val intent = Intent().apply {
                    putExtra(ChangeEnvironmentContract.EXTRA_ENV, environment)
                }
                this.setResult(Activity.RESULT_OK, intent)
                this.finish()
            }.doOnError {
                Toast.makeText(this, "Error changing environment", Toast.LENGTH_SHORT).show()
            }
            .subscribe()
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    class ChangeEnvironmentContract: ActivityResultContract<Environment, Environment?>() {

        companion object {
            const val EXTRA_ENV = "EXTRA_ENV"
        }

        override fun createIntent(context: Context, env: Environment): Intent {
            return Intent(context, EnvironmentActivity::class.java).apply {
                putExtra(EXTRA_ENV, env)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Environment? {
            return if (resultCode == Activity.RESULT_OK) intent?.getParcelableExtra(EXTRA_ENV)
            else null
        }
    }

}