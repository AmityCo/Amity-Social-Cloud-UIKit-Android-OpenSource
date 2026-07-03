package com.amity.socialcloud.uikit.sample.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.core.session.AccessTokenRenewal
import com.amity.socialcloud.sdk.model.core.session.SessionHandler
import com.amity.socialcloud.uikit.AmityUIKit4Manager
import com.amity.socialcloud.uikit.sample.SampleRetrofitProvider
import com.amity.socialcloud.uikit.sample.env.SampleAPIKey
import com.amity.socialcloud.uikit.sample.env.SampleBroker
import com.amity.socialcloud.uikit.sample.env.SampleUploadUrl
import com.amity.socialcloud.uikit.sample.env.SampleUrl
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val DEFAULT_USER_ID = "android-test"
    }

    private val _config = MutableStateFlow(LoginConfig())
    val config: StateFlow<LoginConfig> = _config.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _loggedInUserId = MutableStateFlow("")
    val loggedInUserId: StateFlow<String> = _loggedInUserId.asStateFlow()

    private val disposables = CompositeDisposable()

    init {
        val saved = LoginPreferences.loadConfig(application)
        val region = saved.apiRegion
        _config.value = saved.copy(
            apiKey = saved.apiKey.ifEmpty { SampleAPIKey.get(region.envKey) },
            uploadUrl = saved.uploadUrl.ifEmpty { SampleUploadUrl.get(region.envKey) },
        )
    }

    fun updateUserId(value: String) = _config.update { it.copy(userId = value) }
    fun updateDisplayName(value: String) = _config.update { it.copy(displayName = value) }
    fun updateUserType(value: UserType) = _config.update { it.copy(userType = value) }

    fun updateApiRegion(region: ApiRegion) {
        _config.update {
            it.copy(
                apiRegion = region,
                apiKey = SampleAPIKey.get(region.envKey),
                uploadUrl = SampleUploadUrl.get(region.envKey),
            )
        }
    }

    fun updateApiKey(value: String) = _config.update { it.copy(apiKey = value) }
    fun resetApiKey() {
        _config.update { it.copy(apiKey = SampleAPIKey.get(it.apiRegion.envKey)) }
    }

    fun updateUploadUrl(value: String) = _config.update { it.copy(uploadUrl = value) }
    fun updateSecureMode(value: Boolean) = _config.update { it.copy(secureMode = value) }
    fun updateAuthSignatureUrl(value: String) = _config.update { it.copy(authSignatureUrl = value) }
    fun updateAuthSignatureExpiresAt(millis: Long) = _config.update { it.copy(authSignatureExpiresAtMillis = millis) }
    fun updateVisitorCanViewClip(value: Boolean) = _config.update { it.copy(visitorCanViewClip = value) }
    fun updateHideExplore(value: Boolean) = _config.update { it.copy(hideExplore = value) }
    fun updateSocialCommunityCreation(value: Boolean) = _config.update { it.copy(socialCommunityCreationButtonVisible = value) }
    fun updateTheme(value: AppTheme) = _config.update { it.copy(theme = value) }
    fun updateSyncNetworkConfig(value: Boolean) = _config.update { it.copy(syncNetworkConfig = value) }

    fun envHasChanged(): Boolean {
        val c = _config.value
        val ctx = getApplication<Application>()
        return c.apiKey != LoginPreferences.getLastSetupApiKey(ctx)
                || c.apiRegion.name != LoginPreferences.getLastSetupRegion(ctx)
                || c.uploadUrl != LoginPreferences.getLastSetupUploadUrl(ctx)
    }

    fun resolvedUserId(): String {
        val uid = _config.value.userId.trim()
        return uid.ifEmpty { DEFAULT_USER_ID }
    }

    fun login() {
        val c = _config.value
        val ctx = getApplication<Application>()
        _loginState.value = LoginState.Loading

        LoginPreferences.saveConfig(ctx, c)

        if (envHasChanged()) {
            setupAndLogin(c)
        } else {
            performLogin(c)
        }
    }

    private fun setupAndLogin(c: LoginConfig) {
        val endpoint = buildEndpoint(c)
        val ctx = getApplication<Application>()

        val d = AmityCoreClient.setup(c.apiKey, endpoint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                AmityUIKit4Manager.setup(apiKey = c.apiKey, endpoint = endpoint)
                LoginPreferences.saveLastSetupEnv(ctx, c.apiKey, c.apiRegion.name, c.uploadUrl)
                SampleRetrofitProvider.reset()
                performLogin(c)
            }
            .doOnError { e ->
                _loginState.value = LoginState.Error("Setup failed: ${e.message}")
            }
            .subscribe({}, { e ->
                _loginState.value = LoginState.Error("Setup failed: ${e.message}")
            })
        disposables.add(d)
    }

    private fun buildEndpoint(c: LoginConfig): AmityEndpoint {
        return when (c.apiRegion) {
            ApiRegion.SG -> AmityEndpoint.SG
            ApiRegion.EU -> AmityEndpoint.EU
            ApiRegion.US -> AmityEndpoint.US
            ApiRegion.STAGING -> AmityEndpoint.CUSTOM(
                httpEndpoint = SampleUrl.get(c.apiRegion.envKey),
                mqttEndpoint = SampleBroker.get(c.apiRegion.envKey),
                uploadEndpoint = c.uploadUrl.ifEmpty { SampleUploadUrl.get(c.apiRegion.envKey) },
            )
        }
    }

    private fun performLogin(c: LoginConfig) {
        val sessionHandler = object : SessionHandler {
            override fun sessionWillRenewAccessToken(renewal: AccessTokenRenewal) {
                renewal.renew()
            }
        }

        val userId = resolvedUserId()
        val displayName = c.displayName.trim().ifEmpty { null }

        when (c.userType) {
            UserType.VISITOR -> loginAsVisitor(c, sessionHandler)
            UserType.SIGNED_IN -> {
                val loginBuilder = AmityCoreClient.login(userId, sessionHandler)
                    .apply {
                        if (displayName != null) displayName(displayName)
                        if (c.secureMode) {
                            fetchAndApplyAuthToken(c, this)
                            return
                        }
                    }

                val d = Single.just(true)
                    .delay(200, TimeUnit.MILLISECONDS)
                    .flatMapCompletable {
                        loginBuilder.build().submit()
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        onLoginSuccess(c, userId)
                    }, { e ->
                        _loginState.value = LoginState.Error("Login failed: ${e.message}")
                    })
                disposables.add(d)
            }
        }
    }

    private fun loginAsVisitor(c: LoginConfig, sessionHandler: SessionHandler) {
        if (c.secureMode && c.authSignatureUrl.isNotEmpty()) {
            val httpUrl = SampleUrl.get(c.apiRegion.envKey)
            val retrofitInstance = SampleRetrofitProvider.getInstance(httpUrl)
            val api = retrofitInstance.create(SecureService::class.java)
            val visitorDeviceId = AmityCoreClient.getVisitorDeviceId()
            val expiresAt = DateTime(c.authSignatureExpiresAtMillis)

            api.getAuthSignature(
                url = c.authSignatureUrl,
                deviceId = visitorDeviceId,
                authSignatureExpiresAt = expiresAt
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val signature = try {
                        response.body()?.get("signature")?.asString ?: ""
                    } catch (_: Exception) { "" }

                    val d = Single.just(true)
                        .delay(200, TimeUnit.MILLISECONDS)
                        .flatMapCompletable {
                            AmityCoreClient.loginAsVisitor(sessionHandler)
                                .apply {
                                    if (signature.isNotEmpty()) {
                                        authSignature(signature)
                                        authSignatureExpiresAt(expiresAt)
                                    }
                                }
                                .build()
                                .submit()
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            onLoginSuccess(c, "visitor")
                        }, { e ->
                            _loginState.value = LoginState.Error("Visitor login failed: ${e.message}")
                        })
                    disposables.add(d)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    _loginState.value = LoginState.Error("Auth signature fetch failed: ${t.message}")
                }
            })
        } else {
            val d = Single.just(true)
                .delay(200, TimeUnit.MILLISECONDS)
                .flatMapCompletable {
                    AmityCoreClient.loginAsVisitor(sessionHandler)
                        .build()
                        .submit()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onLoginSuccess(c, "visitor")
                }, { e ->
                    _loginState.value = LoginState.Error("Visitor login failed: ${e.message}")
                })
            disposables.add(d)
        }
    }

    private fun fetchAndApplyAuthToken(c: LoginConfig, loginBuilder: Any) {
        // For secure signed-in login, fetch auth token then submit
        // This is a simplified placeholder — real implementation depends on authSignatureUrl
        _loginState.value = LoginState.Error("Secure signed-in login requires auth token endpoint configuration")
    }

    private fun onLoginSuccess(c: LoginConfig, userId: String) {
        _loggedInUserId.value = userId
        if (c.syncNetworkConfig) {
            val d = AmityUIKit4Manager.syncNetworkConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _loginState.value = LoginState.Success
                }, {
                    _loginState.value = LoginState.Success
                })
            disposables.add(d)
        } else {
            _loginState.value = LoginState.Success
        }
        AmityCoreClient.registerPushNotification()
            .subscribeOn(Schedulers.io())
            .subscribe({}, {})
    }

    fun logout() {
        val d = AmityCoreClient.unregisterPushNotification()
            .andThen(AmityCoreClient.logout())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _loginState.value = LoginState.Idle
                _loggedInUserId.value = ""
            }, {
                _loginState.value = LoginState.Idle
                _loggedInUserId.value = ""
            })
        disposables.add(d)
    }

    fun secureLogout() {
        val d = AmityCoreClient.unregisterPushNotification()
            .andThen(AmityCoreClient.secureLogout())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _loginState.value = LoginState.Idle
                _loggedInUserId.value = ""
            }, {
                _loginState.value = LoginState.Idle
                _loggedInUserId.value = ""
            })
        disposables.add(d)
    }

    fun resyncNetworkConfig(onComplete: () -> Unit, onError: (String) -> Unit) {
        val d = AmityUIKit4Manager.syncNetworkConfig()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onComplete()
            }, { e ->
                onError(e.message ?: "Sync failed")
            })
        disposables.add(d)
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    interface SecureService {
        @GET
        fun getAuthSignature(
            @Url url: String,
            @Query("deviceId") deviceId: String,
            @Query("authSignatureExpiresAt") authSignatureExpiresAt: DateTime
        ): Call<JsonObject>
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
