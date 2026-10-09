package com.amity.socialcloud.uikit.sample.networklog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Detail view. Values wrap rather than truncate — the opposite of row behaviour, because a
 * row optimises for scanning many entries and detail optimises for reading one completely.
 *
 * No redaction: bearer tokens appear in full. Safe only because this ships in the sample
 * app alone and never reaches a release artifact.
 */
@Composable
fun AmityNetworkLogDetail(
    entry: AmityNetworkLogEntry,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val a = entry.activity

    Column(
        modifier = modifier
            .background(AmityTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                "‹ Log",
                fontSize = 15.sp,
                color = AmityTheme.colors.primary,
                modifier = Modifier.clickable { onBack() },
            )
            Text(
                entry.primaryLabel,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AmityTheme.colors.base,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
            )
            if (a?.request != null) {
                Text(
                    "⧉ cURL",
                    fontSize = 14.sp,
                    color = AmityTheme.colors.primary,
                    modifier = Modifier.clickable { copyCurl(context, entry) },
                )
            }
        }

        if (a == null) return@Column

        SectionHeader("Summary")
        Kv("type", a.type.serializedName)
        Kv("started", entry.timestampLabel)
        a.durationMs?.let { Kv("duration", "$it ms") }
        a.response?.let { Kv("status", "${it.statusCode} ${it.message ?: ""}") }
        a.error?.let { Kv("error", it) }

        a.mqtt?.let { mqtt ->
            SectionHeader("MQTT")
            Kv("topic", mqtt.topic)
            Kv("qos", mqtt.qos.toString())
            BodyBlock(mqtt.payload)
        }

        a.state?.let { state ->
            SectionHeader("State change")
            Kv("source", state.source)
            Kv("transition", "${state.from ?: "-"} → ${state.to}")
            state.detail?.let { Kv("detail", it) }
        }

        a.request?.let { req ->
            SectionHeader("Request")
            Kv("method", req.method)
            Kv("url", req.url)
            SectionHeader("Request headers")
            req.headers.forEach { (k, v) -> Kv(k, v.joinToString(", ")) }
            req.body?.let { BodySection("Request body", it.bytes, it) }
        }

        a.response?.let { res ->
            SectionHeader("Response headers")
            res.headers.forEach { (k, v) -> Kv(k, v.joinToString(", ")) }
            res.body?.let { BodySection("Response body", it.bytes, it) }
        }
    }
}

@Composable
private fun BodySection(
    title: String,
    bytes: ByteArray,
    body: com.amity.socialcloud.sdk.model.core.network.AmityCapturedBody,
) {
    SectionHeader(title)
    if (bytes.isEmpty()) {
        Text(
            "Body not captured (size recorded only).",
            fontSize = 12.sp,
            color = AmityTheme.colors.baseShade2,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
        )
    } else {
        BodyBlock(String(bytes))
    }
    if (body.isCompressed) {
        Text(
            "${body.wireLength} B wire · ${body.decodedLength} B decoded (${body.contentEncoding})",
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = AmityTheme.colors.baseShade1,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
        )
    }
    if (body.truncated && bytes.isNotEmpty()) {
        Text(
            "⚠ Truncated — showing ${bytes.size} B of ${body.decodedLength} B",
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = AmityNetworkLogPalette.status(AmityNetworkLogStatusClass.WARNING),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text.uppercase(),
        fontSize = 11.5.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        color = AmityTheme.colors.baseShade1,
        modifier = Modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.backgroundShade1)
            .padding(horizontal = 14.dp, vertical = 5.dp),
    )
}

@Composable
private fun Kv(key: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp)) {
        Text(
            key,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = AmityTheme.colors.baseShade2,
            modifier = Modifier.width(112.dp),
        )
        Text(
            value,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = AmityTheme.colors.base,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun BodyBlock(text: String) {
    Text(
        text,
        fontSize = 11.5.sp,
        fontFamily = FontFamily.Monospace,
        color = AmityTheme.colors.base,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AmityTheme.colors.backgroundShade1)
            .padding(9.dp),
    )
}

private fun copyCurl(context: Context, entry: AmityNetworkLogEntry) {
    val a = entry.activity ?: return
    val req = a.request ?: return
    val sb = StringBuilder("curl -X ${req.method} '${req.url}'")
    req.headers.forEach { (k, v) ->
        v.forEach { sb.append(" \\\n  -H '").append(k).append(": ").append(it).append("'") }
    }
    val body = req.body
    if (body != null && body.bytes.isNotEmpty()) {
        sb.append(" \\\n  --data '").append(String(body.bytes).replace("'", "'\\''")).append("'")
    } else if (req.method != "GET") {
        sb.append("  # body not captured")
    }
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("curl", sb.toString()))
    Toast.makeText(context, "Copied as cURL", Toast.LENGTH_SHORT).show()
}
