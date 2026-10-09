package com.amity.socialcloud.uikit.sample.networklog

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import kotlin.math.roundToInt

enum class AmityNetworkLogOverlayState { BEACON, DOCKED, FULL_SCREEN }

enum class AmityNetworkLogDockMode { PUSH, FLOAT }

/**
 * Which edge the docked panel occupies.
 *
 * This is what makes FLOAT worth having. PUSH resizes the app, so nothing is hidden and
 * the edge barely matters. FLOAT leaves the app pixel-identical to production, which means
 * something *is* hidden — so the tester has to be able to choose what. Without a position
 * control, float is push minus the inset, which is strictly worse.
 */
enum class AmityNetworkLogPanelPosition { TOP, BOTTOM }

/**
 * Keeps the network log visible while the tester drives the UIKit — the behaviour of a
 * browser's docked DevTools panel.
 *
 * The Android UIKit has 77 activities and no NavHost, so there is no container the sample
 * app owns that a page and a panel could share. The overlay therefore attaches above the
 * window, per Activity, from the Application — which also means hosting needs no UIKit
 * library change at all.
 *
 * Spec: front-end-tech-specs/UIKIT/components/AmityNetworkLogOverlay/v1.md
 */
object AmityNetworkLogOverlay {

    private var state by mutableStateOf(AmityNetworkLogOverlayState.BEACON)
    private var dockMode by mutableStateOf(AmityNetworkLogDockMode.PUSH)
    private var position by mutableStateOf(AmityNetworkLogPanelPosition.BOTTOM)
    private var heightFraction by mutableStateOf(DEFAULT_HEIGHT_FRACTION)
    private var enabled = false

    /**
     * Hides the whole overlay without stopping capture, so a tester can clear the screen
     * for a screenshot and still have the session's traffic when they turn it back on.
     */
    private var overlayVisible by mutableStateOf(true)

    fun setVisible(value: Boolean) {
        overlayVisible = value
        if (!value) state = AmityNetworkLogOverlayState.BEACON
    }

    fun isVisible(): Boolean = overlayVisible

    fun enable(app: Application, initiallyVisible: Boolean = true) {
        if (enabled) return
        enabled = true
        overlayVisible = initiallyVisible
        AmityNetworkLogCollector.start()
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) = attach(activity)
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }

    private fun attach(activity: Activity) {
        if (activity !is ComponentActivity) return
        val root = activity.findViewById<ViewGroup>(android.R.id.content) ?: return
        if (root.findViewWithTag<ComposeView>(TAG) != null) return

        val view = ComposeView(activity).apply {
            tag = TAG
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { AmityComposeTheme { OverlayContent(root) } }
        }
        root.addView(
            view,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            )
        )
    }

    /**
     * Push mode insets the host content so the app's own bottom chrome — nav bars, chat
     * composers — stays reachable. Those are the very controls that trigger the requests
     * being watched.
     */
    private fun applyInset(root: ViewGroup, topPx: Int = 0, bottomPx: Int = 0) {
        val host = root.getChildAt(0) ?: return
        if (host.paddingTop != topPx || host.paddingBottom != bottomPx) {
            host.setPadding(host.paddingLeft, topPx, host.paddingRight, bottomPx)
        }
    }

    @Composable
    private fun OverlayContent(root: ViewGroup) {
        val entries by AmityNetworkLogCollector.entries.collectAsState()
        val count by AmityNetworkLogCollector.captureCount.collectAsState()
        val lastStatus by AmityNetworkLogCollector.lastStatus.collectAsState()

        if (!overlayVisible) {
            applyInset(root)
            return
        }

        when (state) {
            AmityNetworkLogOverlayState.BEACON -> {
                applyInset(root)
                Beacon(count, lastStatus) { state = AmityNetworkLogOverlayState.DOCKED }
            }
            AmityNetworkLogOverlayState.DOCKED -> {
                Box(Modifier.fillMaxSize()) {
                    DockedPanel(
                        entries = entries,
                        count = count,
                        dockMode = dockMode,
                        position = position,
                        heightFraction = heightFraction,
                        onToggleDock = {
                            dockMode = if (dockMode == AmityNetworkLogDockMode.PUSH)
                                AmityNetworkLogDockMode.FLOAT else AmityNetworkLogDockMode.PUSH
                        },
                        onTogglePosition = {
                            position = if (position == AmityNetworkLogPanelPosition.BOTTOM)
                                AmityNetworkLogPanelPosition.TOP else AmityNetworkLogPanelPosition.BOTTOM
                        },
                        onResize = { delta ->
                            heightFraction = (heightFraction + delta)
                                .coerceIn(MIN_HEIGHT_FRACTION, MAX_HEIGHT_FRACTION)
                        },
                        onExpand = { state = AmityNetworkLogOverlayState.FULL_SCREEN },
                        onCollapse = { state = AmityNetworkLogOverlayState.BEACON },
                        onHeight = { px ->
                            if (dockMode == AmityNetworkLogDockMode.PUSH) {
                                if (position == AmityNetworkLogPanelPosition.BOTTOM) {
                                    applyInset(root, bottomPx = px)
                                } else {
                                    applyInset(root, topPx = px)
                                }
                            } else {
                                applyInset(root)
                            }
                        },
                        modifier = Modifier.align(
                            if (position == AmityNetworkLogPanelPosition.BOTTOM)
                                Alignment.BottomCenter else Alignment.TopCenter
                        ),
                    )
                }
            }
            AmityNetworkLogOverlayState.FULL_SCREEN -> {
                applyInset(root)
                FullScreen(
                    entries = entries,
                    count = count,
                    onDismiss = { state = AmityNetworkLogOverlayState.DOCKED },
                )
            }
        }
    }

    @Composable
    private fun Beacon(count: Int, lastStatus: String?, onClick: () -> Unit) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 14.dp, bottom = 96.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.base)
                    .border(2.dp, AmityTheme.colors.background, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { _, drag ->
                            offsetX += drag.x
                            offsetY += drag.y
                        }
                    }
                    .clickable { onClick() },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$count",
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        // NOT baseInverse: that token is #000000 in the light theme, which
                        // renders black-on-black over a `base` surface. `background` is the
                        // token that actually inverts against `base` in both themes.
                        color = AmityTheme.colors.background,
                    )
                    Text(
                        lastStatus ?: "—",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = AmityTheme.colors.background,
                    )
                }
            }
        }
    }

    @Composable
    private fun DockedPanel(
        entries: List<AmityNetworkLogEntry>,
        count: Int,
        dockMode: AmityNetworkLogDockMode,
        position: AmityNetworkLogPanelPosition,
        heightFraction: Float,
        onToggleDock: () -> Unit,
        onTogglePosition: () -> Unit,
        onResize: (Float) -> Unit,
        onExpand: () -> Unit,
        onCollapse: () -> Unit,
        onHeight: (Int) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val density = androidx.compose.ui.platform.LocalDensity.current
        val config = androidx.compose.ui.platform.LocalConfiguration.current
        val screenHeightDp = config.screenHeightDp.dp
        val heightDp = screenHeightDp * heightFraction

        val atBottom = position == AmityNetworkLogPanelPosition.BOTTOM
        val systemInset = if (atBottom) {
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        } else {
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        }
        onHeight(with(density) { (heightDp + systemInset).roundToPx() })

        Column(
            modifier = modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    if (atBottom) WindowInsets.navigationBars else WindowInsets.statusBars
                )
                .height(heightDp)
                .background(AmityTheme.colors.background)
                .border(width = 1.dp, color = AmityTheme.colors.baseShade4),
        ) {
            // A panel docked at the top puts its grip and header at the bottom edge, so the
            // drag handle always sits on the boundary with the app.
            if (atBottom) {
                ResizeGrip(atBottom = true, screenHeightDp = screenHeightDp, onResize = onResize)
                PanelHeader(count, dockMode, position, onToggleDock, onTogglePosition, onExpand, onCollapse)
                AmityNetworkLogPanel(entries = entries, modifier = Modifier.fillMaxSize())
            } else {
                AmityNetworkLogPanel(entries = entries, modifier = Modifier.weight(1f))
                PanelHeader(count, dockMode, position, onToggleDock, onTogglePosition, onExpand, onCollapse)
                ResizeGrip(atBottom = false, screenHeightDp = screenHeightDp, onResize = onResize)
            }
        }
    }

    /**
     * Drag to resize (REQ-015). Position is a separate explicit toggle rather than a
     * gesture on this grip: overloading one handle with both would mean a resize drag that
     * ran slightly too far teleported the panel to the other edge.
     */
    @Composable
    private fun ResizeGrip(
        atBottom: Boolean,
        screenHeightDp: androidx.compose.ui.unit.Dp,
        onResize: (Float) -> Unit,
    ) {
        val density = androidx.compose.ui.platform.LocalDensity.current
        Box(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
                .pointerInput(atBottom, screenHeightDp) {
                    detectVerticalDragGestures { _, dragPx ->
                        val dragDp = with(density) { dragPx.toDp() }
                        // Dragging away from the panel's own edge makes it taller.
                        val signed = if (atBottom) -dragDp else dragDp
                        onResize(signed / screenHeightDp)
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .width(40.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(AmityTheme.colors.baseShade3)
            )
        }
    }

    @Composable
    private fun PanelHeader(
        count: Int,
        dockMode: AmityNetworkLogDockMode,
        position: AmityNetworkLogPanelPosition,
        onToggleDock: () -> Unit,
        onTogglePosition: () -> Unit,
        onExpand: () -> Unit,
        onCollapse: () -> Unit,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    "NETWORK",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.baseShade1,
                )
                Text(
                    "$count",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = AmityTheme.colors.baseShade2,
                )
                Spacer(Modifier.weight(1f))
                // A word reads clearer than a glyph for a mode toggle, so this stays
                // labelled — but it takes the same 32dp height as the icon buttons so the
                // row has one baseline and one rhythm.
                DockModePill(dockMode, onToggleDock)
                IconAction(
                    if (position == AmityNetworkLogPanelPosition.BOTTOM)
                        Icons.Filled.VerticalAlignTop else Icons.Filled.VerticalAlignBottom,
                    if (position == AmityNetworkLogPanelPosition.BOTTOM)
                        "Move panel to top" else "Move panel to bottom",
                    onTogglePosition,
                )
                IconAction(Icons.Filled.DeleteOutline, "Clear captured log") {
                    AmityNetworkLogCollector.clear()
                }
                IconAction(Icons.Filled.OpenInFull, "Expand to full screen", onExpand)
                IconAction(Icons.Filled.KeyboardArrowDown, "Minimise to beacon", onCollapse)
            }
        }
    }

    /**
     * Uniform 32dp circular target with an 18dp vector icon, matching the UIKit's own
     * header convention. Unicode glyphs were used first and did not work: each glyph fills
     * a different fraction of its em box and sits on a different optical centre, so a row
     * of them reads as mismatched sizes on inconsistent baselines however the font size is
     * tuned. A vector icon in a fixed box is the only way to get them to line up.
     */
    @Composable
    private fun IconAction(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        contentDescription: String,
        onClick: () -> Unit,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(AmityTheme.colors.backgroundShade1)
                .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = AmityTheme.colors.baseShade1,
                modifier = Modifier.size(18.dp),
            )
        }
    }

    @Composable
    private fun DockModePill(mode: AmityNetworkLogDockMode, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AmityTheme.colors.backgroundShade1)
                .clickable { onClick() }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (mode == AmityNetworkLogDockMode.PUSH) "push" else "float",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = AmityTheme.colors.baseShade1,
            )
        }
    }

    @Composable
    private fun FullScreen(entries: List<AmityNetworkLogEntry>, count: Int, onDismiss: () -> Unit) {
        Column(
            Modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background)
                // Full screen covers the whole window, so it owns both insets.
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconAction(Icons.Filled.CloseFullscreen, "Back to docked panel", onDismiss)
                Text(
                    "Network Log",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmityTheme.colors.base,
                    modifier = Modifier.weight(1f).padding(start = 12.dp),
                )
                Text(
                    "$count",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = AmityTheme.colors.baseShade2,
                )
            }
            AmityNetworkLogPanel(
                entries = entries,
                modifier = Modifier.fillMaxSize(),
                showSearch = true,
            )
        }
    }

    private const val TAG = "amity_network_log_overlay"
    private const val DEFAULT_HEIGHT_FRACTION = 0.36f
    private const val MIN_HEIGHT_FRACTION = 0.20f
    private const val MAX_HEIGHT_FRACTION = 0.75f
}
