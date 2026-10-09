package com.amity.socialcloud.uikit.sample.networklog

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivity
import com.amity.socialcloud.sdk.model.core.network.AmityNetworkActivityType
import com.amity.socialcloud.uikit.common.infra.networklog.AmityNetworkCaptureBridge
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.ArrayDeque
import java.util.Date
import java.util.Locale

/**
 * Thread-safe, bounded store behind the network log.
 *
 * Writes arrive on the OkHttp dispatcher, the MQTT callback thread and ExoPlayer's loader
 * thread; reads happen on the UI thread. The SDK sample app's equivalent gets this wrong
 * (an unbounded `mutableListOf` mutated from an IO scheduler) and is not a model to copy.
 *
 * Spec: front-end-tech-specs/UIKIT/components/AmityNetworkLogOverlay/v1.md
 */
object AmityNetworkLogCollector {

    /** Ring buffer size. Oldest entries are evicted first. */
    const val BUFFER_CAPACITY = 2000

    private val lock = Any()
    private val buffer = ArrayDeque<AmityNetworkActivity>()
    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

    private val _entries = MutableStateFlow<List<AmityNetworkLogEntry>>(emptyList())
    val entries: StateFlow<List<AmityNetworkLogEntry>> = _entries.asStateFlow()

    private val _captureCount = MutableStateFlow(0)
    val captureCount: StateFlow<Int> = _captureCount.asStateFlow()

    private val _lastStatus = MutableStateFlow<String?>(null)
    val lastStatus: StateFlow<String?> = _lastStatus.asStateFlow()

    private var disposable: Disposable? = null

    /**
     * Subscribes at application init, not when the viewer opens: the SDK stream has no
     * replay, so a late subscription silently loses the whole login burst.
     */
    fun start() {
        if (disposable != null) return
        disposable = AmityCoreClient.observeNetworkActivities()
            .subscribeOn(Schedulers.io())
            .subscribe({ record(it) }, { /* a diagnostic stream must not crash the app */ })
        AmityNetworkCaptureBridge.attach { record(it) }
    }

    fun stop() {
        disposable?.dispose()
        disposable = null
        AmityNetworkCaptureBridge.detach()
    }

    fun record(activity: AmityNetworkActivity) {
        synchronized(lock) {
            if (buffer.size >= BUFFER_CAPACITY) buffer.pollFirst()
            buffer.addLast(activity)
        }
        _captureCount.value = _captureCount.value + 1
        _lastStatus.value = activity.response?.statusCode?.toString()
            ?: activity.error?.let { "ERR" }
                    ?: "—"
        republish()
    }

    fun clear() {
        synchronized(lock) { buffer.clear() }
        _captureCount.value = 0
        _lastStatus.value = null
        republish()
    }

    fun snapshot(): List<AmityNetworkActivity> = synchronized(lock) { buffer.toList() }

    private fun republish() {
        val raw = snapshot()
        _entries.value = group(raw)
    }

    /**
     * Collapses high-volume signals so a livestream's ~1,500 segment requests never bury
     * the handful of REST calls that matter. Group runs are contiguous in time, so a later
     * burst of the same kind opens a new group rather than reopening a closed one.
     */
    private fun group(raw: List<AmityNetworkActivity>): List<AmityNetworkLogEntry> {
        val out = mutableListOf<AmityNetworkLogEntry>()
        var run = mutableListOf<AmityNetworkActivity>()
        var runType: AmityNetworkActivityType? = null

        fun flush() {
            if (run.isEmpty()) return
            val type = runType!!
            if (run.size < GROUP_THRESHOLD) {
                run.forEach { out += toEntry(it) }
            } else {
                val failures = run.count { (it.response?.statusCode ?: 0) >= 400 || it.error != null }
                out += AmityNetworkLogEntry(
                    id = "group-${type.name}-${run.first().id}",
                    variant = AmityNetworkLogRowVariant.GROUP,
                    type = type,
                    timestampLabel = timeFormat.format(Date(run.first().startedAt)),
                    chipLabel = chipLabel(type),
                    primaryLabel = "${groupName(type)} · ${run.size} requests",
                    statusLabel = "$failures fail",
                    statusClass = if (failures > 0) AmityNetworkLogStatusClass.ERROR
                    else AmityNetworkLogStatusClass.SUCCESS,
                    childCount = run.size,
                    failureCount = failures,
                    children = run.map { toEntry(it) },
                )
            }
            run = mutableListOf()
            runType = null
        }

        raw.forEach { activity ->
            val groupable = activity.type == AmityNetworkActivityType.MEDIA_SEGMENT ||
                    activity.type == AmityNetworkActivityType.IMAGE
            if (groupable) {
                if (runType != null && runType != activity.type) flush()
                runType = activity.type
                run.add(activity)
            } else {
                flush()
                out += toEntry(activity)
            }
        }
        flush()
        return out
    }

    private fun toEntry(a: AmityNetworkActivity): AmityNetworkLogEntry {
        val ts = timeFormat.format(Date(a.startedAt))
        val mqtt = a.mqtt
        val state = a.state
        return when {
            mqtt != null -> AmityNetworkLogEntry(
                id = a.id,
                variant = AmityNetworkLogRowVariant.MESSAGE,
                type = a.type,
                timestampLabel = ts,
                chipLabel = "MQTT",
                primaryLabel = mqtt.topic,
                statusLabel = "${mqtt.payload.length} B",
                statusClass = AmityNetworkLogStatusClass.NONE,
                activity = a,
            )
            state != null -> AmityNetworkLogEntry(
                id = a.id,
                variant = AmityNetworkLogRowVariant.STATE,
                type = a.type,
                timestampLabel = ts,
                chipLabel = if (a.type == AmityNetworkActivityType.MQTT_STATE) "CONN" else "EVT",
                primaryLabel = buildString {
                    append(state.source).append(" · ")
                    state.from?.let { append(it).append(" → ") }
                    append(state.to)
                    state.detail?.let { append("  ").append(it) }
                },
                statusLabel = "—",
                statusClass = AmityNetworkLogStatusClass.NONE,
                activity = a,
            )
            else -> {
                val status = a.response?.statusCode
                AmityNetworkLogEntry(
                    id = a.id,
                    variant = AmityNetworkLogRowVariant.REQUEST,
                    type = a.type,
                    timestampLabel = ts,
                    chipLabel = chipLabel(a.type),
                    primaryLabel = pathOf(a.request?.url ?: ""),
                    statusLabel = when {
                        a.error != null -> "failed"
                        status != null -> status.toString()
                        else -> "pending"
                    },
                    statusClass = when {
                        a.error != null -> AmityNetworkLogStatusClass.ERROR
                        status == null -> AmityNetworkLogStatusClass.IN_FLIGHT
                        status >= 500 -> AmityNetworkLogStatusClass.ERROR
                        status >= 400 -> AmityNetworkLogStatusClass.WARNING
                        status >= 300 -> AmityNetworkLogStatusClass.NEUTRAL
                        else -> AmityNetworkLogStatusClass.SUCCESS
                    },
                    activity = a,
                )
            }
        }
    }

    /** Path-first: every row in a session shares a host, so leading with it wastes width. */
    private fun pathOf(url: String): String = runCatching {
        val afterScheme = url.substringAfter("://")
        "/" + afterScheme.substringAfter('/', "")
    }.getOrDefault(url)

    private fun chipLabel(type: AmityNetworkActivityType) = when (type) {
        AmityNetworkActivityType.API -> "API"
        AmityNetworkActivityType.UPLOAD -> "UPLD"
        AmityNetworkActivityType.DOWNLOAD -> "DOWN"
        AmityNetworkActivityType.MEDIA_SEGMENT -> "HLS"
        AmityNetworkActivityType.IMAGE -> "IMG"
        AmityNetworkActivityType.MQTT_MESSAGE -> "MQTT"
        AmityNetworkActivityType.MQTT_STATE -> "CONN"
        AmityNetworkActivityType.STREAM_STATE -> "EVT"
    }

    private fun groupName(type: AmityNetworkActivityType) =
        if (type == AmityNetworkActivityType.MEDIA_SEGMENT) "HLS segments" else "Image loads"

    private const val GROUP_THRESHOLD = 3
}
