package com.amity.socialcloud.uikit.sample.discoverywidget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.community.compose.discoverywidget.AmityDiscoveryWidgetComponent

/**
 * The widget's failure modes are mostly about *not* rendering, which no existing sample screen can
 * isolate: each control here forces one of them on demand.
 */
class AmityDiscoveryWidgetTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DiscoveryWidgetTestScreen() }
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, AmityDiscoveryWidgetTestActivity::class.java)
    }
}

private const val DEFAULT_TOPIC = "beauty"

@Composable
private fun DiscoveryWidgetTestScreen() {
    // Pre-filled so the harness is one tap from a live pool; any slug can be typed over it.
    var topicIdInput by remember { mutableStateOf(DEFAULT_TOPIC) }
    var appliedTopicId by remember { mutableStateOf("") }
    var showHeader by remember { mutableStateOf(true) }
    var threshold by remember { mutableIntStateOf(3) }
    var lastClick by remember { mutableStateOf("") }
    var loadToken by remember { mutableIntStateOf(0) }
    val appContext = LocalContext.current
    val activity = appContext as? android.app.Activity

    fun load() {
        appliedTopicId = topicIdInput.trim()
        loadToken++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Discovery Widget", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                "Session: ${AmityCoreClient.getCurrentUserType()} · " +
                    "state ${AmityCoreClient.getCurrentSessionState()}",
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
            )
            Button(onClick = { activity?.finish() }) { Text("Switch", fontSize = 12.sp) }
        }

        OutlinedTextField(
            value = topicIdInput,
            onValueChange = { topicIdInput = it },
            label = { Text("Topic slug (try a bogus one for 404)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = { load() },
            enabled = topicIdInput.isNotBlank(),
        ) { Text("Load topic") }

        if (lastClick.isNotEmpty()) {
            Text("last card click → $lastClick", fontSize = 12.sp)
        }

        HorizontalDivider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = showHeader, onCheckedChange = { showHeader = it })
            Text("showHeader", modifier = Modifier.padding(start = 8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("minVisibilityThreshold: $threshold")
            Button(onClick = { if (threshold > 1) threshold-- }) { Text("−") }
            Button(onClick = { threshold++ }) { Text("+") }
        }

        HorizontalDivider()

        // Tinted so an Absent widget is distinguishable from a widget that rendered nothing visible.
        // The widget's heading paints in the UIKit theme's base colour, so the host has to supply
        // the matching ground -- a light host under a dark theme renders the title invisible. A
        // border keeps the host's bounds visible without fighting the theme.
        val host = Modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
            .border(1.dp, Color(0x55FF0000))

        if (appliedTopicId.isEmpty()) {
            Box(host) { Text("Load a topic to render the widget", fontSize = 12.sp) }
        } else {
            Box(host) {
                key(loadToken) {
                    AmityDiscoveryWidgetComponent(
                        topicId = appliedTopicId,
                        showHeader = showHeader,
                        minVisibilityThreshold = threshold,
                        // A click has no destination in the harness, so surface it instead --
                        // otherwise the read-only card is indistinguishable from a dead one.
                        onCardClick = { clickedTopicId, post ->
                            lastClick = "topic=$clickedTopicId  post=${post.getPostId()}"
                            Toast.makeText(appContext, lastClick, Toast.LENGTH_LONG).show()
                        },
                    )
                }
            }
        }
    }
}
