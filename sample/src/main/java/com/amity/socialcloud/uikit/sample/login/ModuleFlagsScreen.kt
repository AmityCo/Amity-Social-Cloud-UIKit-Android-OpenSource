package com.amity.socialcloud.uikit.sample.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.config.AmityUIKitModuleAvailability
import com.amity.socialcloud.uikit.sample.login.theme.SampleCardBg as CardBg
import com.amity.socialcloud.uikit.sample.login.theme.SampleChevron as Chevron
import com.amity.socialcloud.uikit.sample.login.theme.SampleHeldInk as HeldInk
import com.amity.socialcloud.uikit.sample.login.theme.SampleInk as Ink
import com.amity.socialcloud.uikit.sample.login.theme.SampleLabel as Label
import com.amity.socialcloud.uikit.sample.login.theme.SampleLinkBlue as LinkBlue
import com.amity.socialcloud.uikit.sample.login.theme.SampleMuted as Muted
import com.amity.socialcloud.uikit.sample.login.theme.SamplePageBg as PageBg

private val StatusOn = Color(0xFF34C759)
private val StatusOff = Color(0xFFC7C7CC)
private val NameOff = Color(0xFF8E8E93)

/**
 * The Phase 1 modules as the network reports them, and why each is on or off.
 *
 * Read-only: the network's module settings are the only source that withholds
 * a module, as on Web. To see one withheld, change it in OPS on a network set
 * to `enforce` and relaunch.
 */
@Composable
fun ModuleFlagsScreen(
    onBack: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(setOf<String>()) }

    // The entitlements land after login, which is after this screen has already
    // composed — and the gate holds them in a plain field, not in Compose state.
    // Without this the screen keeps showing the answers it computed on its first
    // frame and reports "No answer yet" for the whole launch. This is the same
    // callback AmityBasePage registers.
    var revision by remember { mutableIntStateOf(0) }
    DisposableEffect(Unit) {
        AmityUIKitConfigController.registerChangeCallback("sample-module-flags") { revision++ }
        onDispose { AmityUIKitConfigController.unregisterChangeCallback("sample-module-flags") }
    }

    val verdicts = remember(revision) {
        ModuleFlags.order.associate { it.key to ModuleFlags.verdict(it.key) }
    }
    val entitlement = remember(revision) { ModuleFlags.entitlementSummary() }
    val withheld = remember(revision) { ModuleFlags.withheldByNetwork() }
    val offCount = ModuleFlags.order.count { verdicts[it.key]?.isAvailable == false }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text("‹ Back", fontSize = 15.sp, color = LinkBlue,
            modifier = Modifier.clickable { onBack() })

        Spacer(Modifier.height(14.dp))
        Text("Phase 1 modules", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Ink)
        Text(
            if (offCount == 0) "All ${ModuleFlags.order.size} on"
            else "$offCount of ${ModuleFlags.order.size} off",
            fontSize = 11.sp, color = Muted, modifier = Modifier.padding(top = 4.dp),
        )

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("NETWORK ENTITLEMENTS", fontSize = 11.sp, color = Label)
                Text(
                    entitlement.first, fontSize = 13.sp, color = Ink,
                    modifier = Modifier.padding(top = 4.dp),
                )
                entitlement.second?.let {
                    Text(it, fontSize = 11.sp, color = Muted, modifier = Modifier.padding(top = 3.dp))
                }
                if (withheld.isNotEmpty()) {
                    Text(
                        "Withheld: ${withheld.joinToString(", ")}.",
                        fontSize = 11.sp, color = HeldInk,
                        modifier = Modifier.padding(top = 3.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column {
                ModuleFlags.order.forEachIndexed { index, row ->
                    ModuleRow(
                        key = row.key,
                        verdict = verdicts[row.key],
                        isExpanded = row.key in expanded,
                        onExpand = {
                            expanded = if (row.key in expanded) expanded - row.key
                            else expanded + row.key
                        },
                    )
                    if (index != ModuleFlags.order.lastIndex) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 14.dp)
                                .height(1.dp)
                                .background(PageBg),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            "Read-only: what the network's module settings say, as the UIKit resolves them. " +
                "To change one, edit Module Entitlement in OPS and relaunch.",
            fontSize = 11.sp, color = NameOff,
        )
        Text(
            "Clip is part of Post and follows it.",
            fontSize = 11.sp, color = NameOff, modifier = Modifier.padding(top = 6.dp),
        )

        Spacer(Modifier.height(20.dp))
    }
}

/**
 * The entry cards' one-line summary, kept current: the entitlement lands after
 * login, in a plain field the gate holds, so without the change callback a card
 * keeps the line it computed on its first frame for the whole launch.
 */
@Composable
fun rememberModuleSummary(callbackKey: String): String {
    var revision by remember { mutableIntStateOf(0) }
    DisposableEffect(callbackKey) {
        AmityUIKitConfigController.registerChangeCallback(callbackKey) { revision++ }
        onDispose { AmityUIKitConfigController.unregisterChangeCallback(callbackKey) }
    }
    return remember(revision) { ModuleFlags.summaryLine() }
}

@Composable
private fun ModuleRow(
    key: String,
    verdict: AmityUIKitModuleAvailability?,
    isExpanded: Boolean,
    onExpand: () -> Unit,
) {
    val available = verdict?.isAvailable ?: true
    val reason = ModuleFlags.reasonOf(verdict)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpand() }
            .padding(horizontal = 14.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (available) StatusOn else StatusOff),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    ModuleFlags.label(key),
                    fontSize = 15.sp,
                    color = if (available) Ink else NameOff,
                )
                Spacer(Modifier.width(6.dp))
                Text(if (isExpanded) "▾" else "▸", fontSize = 11.sp, color = Chevron)
            }
            reason?.let {
                Text(
                    it,
                    fontSize = 11.sp,
                    color = HeldInk,
                    modifier = Modifier.padding(top = 3.dp, start = 16.dp),
                )
            }
            if (isExpanded) Detail(key = key)
        }
        // A state, not a control: there is nothing here to switch.
        Text(
            if (available) "On" else "Off",
            fontSize = 13.sp,
            color = if (available) Ink else NameOff,
        )
    }
}

@Composable
private fun Detail(key: String) {
    Column(modifier = Modifier.padding(top = 8.dp, start = 16.dp, bottom = 2.dp)) {
        DetailRow("Network", ModuleFlags.networkValue(key))
        ModuleFlags.requirementText(key)?.let { DetailRow("Requires", it) }
        ModuleFlags.note(key)?.let { DetailRow("About", it) }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(label, fontSize = 11.sp, color = Label, modifier = Modifier.width(96.dp))
        Text(value, fontSize = 11.sp, color = Ink, modifier = Modifier.weight(1f))
    }
}
