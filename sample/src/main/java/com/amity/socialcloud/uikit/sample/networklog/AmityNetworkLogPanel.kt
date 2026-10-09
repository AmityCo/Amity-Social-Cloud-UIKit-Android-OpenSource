package com.amity.socialcloud.uikit.sample.networklog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * The timeline surface, shared by the docked panel and the full-screen page.
 *
 * Spec: front-end-tech-specs/UIKIT/pages/AmityNetworkLogPage/v1.md
 */
@Composable
fun AmityNetworkLogPanel(
    entries: List<AmityNetworkLogEntry>,
    modifier: Modifier = Modifier,
    showSearch: Boolean = false,
) {
    var activeFilters by remember { mutableStateOf(emptySet<AmityNetworkLogFilter>()) }
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(emptySet<String>()) }
    var detail by remember { mutableStateOf<AmityNetworkLogEntry?>(null) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    val counts = remember(entries) {
        AmityNetworkLogFilter.entries.associateWith { f ->
            entries.sumOf { e ->
                if (AmityNetworkLogFilter.of(e.type) == f) (e.childCount ?: 1) else 0
            }
        }
    }

    val visible = remember(entries, activeFilters, query) {
        entries.filter { e ->
            val filterOk = activeFilters.isEmpty() || AmityNetworkLogFilter.of(e.type) in activeFilters
            val queryOk = query.isBlank() ||
                    e.primaryLabel.contains(query, ignoreCase = true) ||
                    e.chipLabel.contains(query, ignoreCase = true)
            filterOk && queryOk
        }
    }

    val current = detail
    if (current != null) {
        AmityNetworkLogDetail(entry = current, onBack = { detail = null }, modifier = modifier)
        return
    }

    Column(modifier = modifier.background(AmityTheme.colors.background)) {
        if (showSearch) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(AmityTheme.colors.backgroundShade1)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        color = AmityTheme.colors.base,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text(
                                "Search URL, topic or body",
                                fontSize = 14.sp,
                                color = AmityTheme.colors.baseShade2,
                            )
                        }
                        inner()
                    },
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            FilterChip("All", entries.sumOf { it.childCount ?: 1 }, activeFilters.isEmpty()) {
                activeFilters = emptySet()
            }
            AmityNetworkLogFilter.entries.forEach { f ->
                FilterChip(f.label, counts[f] ?: 0, f in activeFilters) {
                    activeFilters = if (f in activeFilters) activeFilters - f else activeFilters + f
                }
            }
        }

        Divider(color = AmityTheme.colors.baseShade4, thickness = 1.dp)

        if (visible.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (entries.isEmpty()) "No network activity captured yet."
                    else "No entries match the current filters.",
                    fontSize = 14.sp,
                    color = AmityTheme.colors.baseShade2,
                )
            }
        } else {
            val listState = rememberLazyListState()
            // Auto-scroll follows the newest entry, and suspends the moment the tester
            // scrolls away — otherwise reading anything during active traffic is impossible
            // (REQ-016). Being "at the end" is the signal; no gesture plumbing needed.
            val atEnd by remember {
                derivedStateOf {
                    val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    last >= listState.layoutInfo.totalItemsCount - 2
                }
            }
            LaunchedEffect(visible.size, atEnd) {
                if (atEnd && visible.isNotEmpty()) {
                    listState.scrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
                }
            }
            Box(Modifier.fillMaxSize()) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                visible.forEach { entry ->
                    item(key = entry.id) {
                        AmityNetworkLogRow(
                            entry = entry,
                            isExpanded = entry.id in expanded,
                            onClick = {
                                if (entry.variant == AmityNetworkLogRowVariant.GROUP) {
                                    expanded = if (entry.id in expanded) expanded - entry.id
                                    else expanded + entry.id
                                } else {
                                    detail = entry
                                }
                            },
                        )
                        Divider(color = AmityTheme.colors.baseShade4, thickness = 0.5.dp)
                    }
                    if (entry.id in expanded) {
                        items(entry.children.take(GROUP_RENDER_CAP), key = { "c-${it.id}" }) { child ->
                            AmityNetworkLogRow(entry = child, indent = true, onClick = { detail = child })
                            Divider(color = AmityTheme.colors.baseShade4, thickness = 0.5.dp)
                        }
                        if (entry.children.size > GROUP_RENDER_CAP) {
                            item(key = "more-${entry.id}") {
                                Text(
                                    "${entry.children.size - GROUP_RENDER_CAP} more…",
                                    fontSize = 12.5.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = AmityTheme.colors.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 26.dp, top = 6.dp, bottom = 6.dp),
                                )
                            }
                        }
                    }
                }
            }
            if (!atEnd) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AmityTheme.colors.base)
                        .clickable {
                            scope.launch {
                                listState.animateScrollToItem(
                                    listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1
                                )
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                ) {
                    Text(
                        "Jump to latest",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmityTheme.colors.background,
                    )
                }
            }
            }
        }
    }
}

@Composable
private fun FilterChip(label: String, count: Int, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .then(
                if (selected) Modifier.background(AmityTheme.colors.base)
                else Modifier.border(1.dp, AmityTheme.colors.baseShade4, RoundedCornerShape(11.dp))
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp),
    ) {
        Text(
            buildAnnotatedString {
                append(label)
                withStyle(SpanStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)) {
                    append("  $count")
                }
            },
            fontSize = 10.5.sp,
            // `background`, not `baseInverse`: baseInverse is #000000 in the light
            // theme, which is invisible on the `base`-filled selected chip.
            color = if (selected) AmityTheme.colors.background else AmityTheme.colors.baseShade1,
            maxLines = 1,
        )
    }
}

private const val GROUP_RENDER_CAP = 20
