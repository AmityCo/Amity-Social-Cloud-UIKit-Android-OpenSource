package com.amity.socialcloud.uikit.community.compose.post.autoplay

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/** Tuning for feed autoplay. */
object AmityFeedAutoPlayConfig {

    /** Share of the frame, or of the screen for a frame taller than it, that must be visible. */
    @Volatile
    var visibleThreshold: Float = 0.5f

    /**
     * Time the most visible frame must stay the same before playback starts. Zero: the pick starts
     * at once (SOL-PDT-3429 R-ONE-AT-A-TIME, 2026-11-04).
     */
    @Volatile
    var settleDelayMs: Long = 0L

    /**
     * Time a starting video may load before the spinner shows. A quicker start goes from the
     * thumbnail straight to the video. A stall while playing shows the spinner at once.
     */
    @Volatile
    var startSpinnerDelayMs: Long = 1_000L
}

/** The member's own choice of when feed videos autoplay, saved on the device. */
enum class AmityFeedAutoPlayChoice(val stringKey: String, val testTag: String) {
    WIFI_AND_DATA("amity_social_autoplay_setting_wifi_and_data", "autoplay_setting/wifi_and_data"),
    WIFI_ONLY("amity_social_autoplay_setting_wifi_only", "autoplay_setting/wifi_only"),
    NEVER("amity_social_autoplay_setting_never", "autoplay_setting/never");

    companion object {
        /** With nothing saved, videos autoplay on any connection (PM, 2026-10-30). */
        val DEFAULT = WIFI_AND_DATA
    }
}

/**
 * Whether feed videos may autoplay now.
 *
 * The network switch and the member's choice must both allow it for the current connection, the
 * device must be online, and it must not be saving data. Offline shows the thumbnail, not the
 * playback-failed fallback.
 */
fun isFeedAutoPlayAllowed(
    isNetworkSwitchOn: Boolean,
    choice: AmityFeedAutoPlayChoice,
    isOnWifi: Boolean,
    isDataSaverOn: Boolean,
    isOnline: Boolean = true,
): Boolean {
    if (!isNetworkSwitchOn || isDataSaverOn || !isOnline) return false
    return when (choice) {
        AmityFeedAutoPlayChoice.WIFI_AND_DATA -> true
        AmityFeedAutoPlayChoice.WIFI_ONLY -> isOnWifi
        AmityFeedAutoPlayChoice.NEVER -> false
    }
}

/**
 * Decides whether feed videos autoplay, from the network switch, the member's choice, the
 * connection and Data Saver. Every feed host reads [isAllowed].
 */
object AmityFeedAutoPlayGate {

    private const val TAG = "AmityFeedAutoPlay"
    private const val PREFS = "amity_feed_autoplay"
    private const val KEY_CHOICE = "choice"
    private const val RECONNECT_READ_DELAY_MS = 1_000L
    private const val SWITCH_READ_RETRIES = 3
    private const val SWITCH_READ_RETRY_DELAY_SECONDS = 2L

    private val _choice = MutableStateFlow(AmityFeedAutoPlayChoice.DEFAULT)
    val choice: StateFlow<AmityFeedAutoPlayChoice> = _choice.asStateFlow()

    private val _isAllowed = MutableStateFlow(false)
    val isAllowed: StateFlow<Boolean> = _isAllowed.asStateFlow()

    @Volatile
    private var isNetworkSwitchOn = false
    @Volatile
    private var isOnWifi = false
    @Volatile
    private var isDataSaverOn = false
    @Volatile
    private var isOnline = true

    private var appContext: Context? = null
    private var switchDisposable: Disposable? = null

    /** Starts watching once per process. Safe to call from every host. */
    @Synchronized
    fun start(context: Context) {
        if (appContext != null) return
        val app = context.applicationContext
        appContext = app
        _choice.value = readChoice(app)
        watchConnection(app)
        AmityFeedAutoPlaySession.observe()
        readNetworkSwitch()
    }

    fun setChoice(context: Context, choice: AmityFeedAutoPlayChoice) {
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CHOICE, choice.name)
            .apply()
        _choice.value = choice
        update()
    }

    /** Reads the saved choice before any feed has started the gate, for the profile menu. */
    fun loadChoice(context: Context) {
        _choice.value = readChoice(context.applicationContext)
    }

    @Synchronized
    internal fun onSessionEnded() {
        switchDisposable?.dispose()
        isNetworkSwitchOn = false
        update()
    }

    /**
     * The switch is read like the SDK's other network settings: each time the session is
     * established, that is at login, at a cold start and after a reconnect. Not on foreground.
     */
    internal fun onSessionEstablished() {
        readNetworkSwitch()
    }

    @Synchronized
    private fun readNetworkSwitch() {
        if (appContext == null) return
        switchDisposable?.dispose()
        switchDisposable = AmityCoreClient.getContentCheck()
            // The network can still be settling just after a reconnect. A read counts as failed
            // only after a few tries.
            .retryWhen { errors ->
                errors.zipWith(Flowable.range(1, SWITCH_READ_RETRIES)) { _, attempt -> attempt }
                    .flatMap { Flowable.timer(SWITCH_READ_RETRY_DELAY_SECONDS, TimeUnit.SECONDS) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { check ->
                    isNetworkSwitchOn = check.post()?.video?.autoPlay ?: false
                    Log.d(TAG, "network switch autoPlay=$isNetworkSwitchOn")
                    update()
                },
                { error ->
                    // A failed read counts as off until the next successful read.
                    Log.w(TAG, "could not read the network switch", error)
                    isNetworkSwitchOn = false
                    update()
                },
            )
    }

    private fun readChoice(context: Context): AmityFeedAutoPlayChoice {
        val saved = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_CHOICE, null)
        return AmityFeedAutoPlayChoice.entries.firstOrNull { it.name == saved } ?: AmityFeedAutoPlayChoice.DEFAULT
    }

    @SuppressLint("MissingPermission")
    private fun watchConnection(context: Context) {
        val connectivity = context.getSystemService(ConnectivityManager::class.java) ?: return
        try {
            refreshConnection(connectivity, connectivity.activeNetwork?.let(connectivity::getNetworkCapabilities))
            connectivity.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                    refreshConnection(connectivity, capabilities)
                }

                override fun onLost(network: Network) {
                    refreshConnection(connectivity, null)
                }
            })
        } catch (error: SecurityException) {
            Log.w(TAG, "cannot watch the connection; Wi-Fi only is treated as mobile data", error)
        }
        // Data Saver can change while the connection stays the same.
        ContextCompat.registerReceiver(
            context,
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    refreshConnection(connectivity, connectivity.activeNetwork?.let(connectivity::getNetworkCapabilities))
                }
            },
            IntentFilter(ConnectivityManager.ACTION_RESTRICT_BACKGROUND_CHANGED),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    private fun refreshConnection(connectivity: ConnectivityManager, capabilities: NetworkCapabilities?) {
        val wasOnline = isOnline
        isOnline = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        if (!wasOnline && isOnline) {
            // A reconnect does not change the SDK session state, so the switch is read again here
            // (SOL-PDT-3429 R-NEXT-SESSION). The short wait lets the new network settle first.
            Handler(Looper.getMainLooper()).postDelayed({ readNetworkSwitch() }, RECONNECT_READ_DELAY_MS)
        }
        isOnWifi = capabilities != null &&
            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        isDataSaverOn = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            connectivity.restrictBackgroundStatus == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED
        update()
    }

    private fun update() {
        _isAllowed.value = isFeedAutoPlayAllowed(
            isNetworkSwitchOn = isNetworkSwitchOn,
            choice = _choice.value,
            isOnWifi = isOnWifi,
            isDataSaverOn = isDataSaverOn,
            isOnline = isOnline,
        )
    }
}

/**
 * Sound state shared by every feed. A session starts at login or at app launch, and ends at logout
 * or when the app process ends. Backgrounding does not end it. Each new session starts muted; once
 * the viewer unmutes one video, every later video in the session plays with sound.
 */
object AmityFeedAutoPlaySession {

    private const val TAG = "AmityFeedAutoPlay"

    private val _isMuted = MutableStateFlow(true)
    val isMuted: StateFlow<Boolean> get() = _isMuted

    private var sessionDisposable: Disposable? = null

    fun setMuted(muted: Boolean) {
        _isMuted.value = muted
    }

    fun toggleMuted() {
        _isMuted.value = !_isMuted.value
    }

    /**
     * Watches the SDK session once per process. A process start is a new session already, since
     * this object starts muted; a logout ends the session and makes the next one read the network
     * switch again.
     */
    fun observe() {
        if (sessionDisposable != null) return
        sessionDisposable = AmityCoreClient.observeSessionState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { state ->
                    when (state) {
                        is SessionState.NotLoggedIn -> {
                            Log.d(TAG, "session ended: sound muted, positions cleared")
                            _isMuted.value = true
                            AmityFeedAutoPlayPositions.clear()
                            AmityFeedAutoPlayFrames.clear()
                            AmityFeedAutoPlayGate.onSessionEnded()
                        }

                        is SessionState.Established -> AmityFeedAutoPlayGate.onSessionEstablished()
                        else -> Unit
                    }
                },
                { error -> Log.w(TAG, "could not observe the session", error) },
            )
    }
}

/**
 * Where each video stopped, shared by every feed and the post detail page, so a video resumes where
 * it stopped when the viewer scrolls back, swipes back to it, or opens its post. A recorded room
 * keeps the segment it reached too. Carousels keep the page they show, so post detail opens a
 * carousel on the page the feed showed.
 */
object AmityFeedAutoPlayPositions {

    private class Position(val positionMs: Long, val hasEnded: Boolean, val itemIndex: Int)

    private val positions = ConcurrentHashMap<String, Position>()
    private val carouselPages = ConcurrentHashMap<String, Int>()

    fun save(key: String, positionMs: Long, hasEnded: Boolean, itemIndex: Int = 0) {
        positions[key] = Position(positionMs.coerceAtLeast(0L), hasEnded, itemIndex.coerceAtLeast(0))
    }

    fun positionOf(key: String): Long = positions[key]?.positionMs ?: 0L

    fun hasEnded(key: String): Boolean = positions[key]?.hasEnded == true

    fun itemIndexOf(key: String): Int = positions[key]?.itemIndex ?: 0

    fun saveCarouselPage(postId: String, page: Int) {
        carouselPages[postId] = page
    }

    fun carouselPageOf(postId: String): Int = carouselPages[postId] ?: 0

    fun clear() {
        positions.clear()
        carouselPages.clear()
    }
}

/**
 * The frame each video stopped on, for the session (SOL-PDT-3429 R-RESUME). A video scrolled away
 * shows this frame, not the post's thumbnail, until it plays again. Only the most recent frames are
 * kept, to bound memory. A logout clears them with the positions.
 */
object AmityFeedAutoPlayFrames {

    private const val MAX_FRAMES = 12

    private val frames = androidx.compose.runtime.mutableStateMapOf<String, androidx.compose.ui.graphics.ImageBitmap>()
    private val order = ArrayDeque<String>()

    fun save(key: String, frame: androidx.compose.ui.graphics.ImageBitmap) {
        order.remove(key)
        order.addLast(key)
        frames[key] = frame
        while (order.size > MAX_FRAMES) frames.remove(order.removeFirst())
    }

    fun frameOf(key: String): androidx.compose.ui.graphics.ImageBitmap? = frames[key]

    fun clear() {
        frames.clear()
        order.clear()
    }
}
