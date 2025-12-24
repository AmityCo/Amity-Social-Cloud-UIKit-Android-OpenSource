package com.amity.socialcloud.uikit.community.compose.event.setup.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityTimezoneListBottomSheet(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    shouldShow: Boolean,
    onDismiss: () -> Unit,
    onTimezoneSelected: (TimeZone) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    val timezones = remember {
        TimeZone.getAvailableIDs()
            .map { TimeZone.getTimeZone(it) }
            .sortedBy { it.rawOffset }
    }

    if (shouldShow) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .background(
                                color = AmityTheme.colors.baseShade3,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                }
            }
        ) {
            AmityBaseElement(
                pageScope = pageScope,
                elementId = "timezone_list"
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) {
                    items(timezones) { timezone ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = TimezoneFormatter.format(timezone),
                                style = AmityTheme.typography.bodyLegacy,
                                color = AmityTheme.colors.base,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableWithoutRipple {
                                        onTimezoneSelected(timezone)
                                        scope.launch {
                                            sheetState.hide()
                                        }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                onDismiss()
                                            }
                                        }
                                    }
                                    .padding(16.dp)
                            )
                            
                            HorizontalDivider(
                                color = AmityTheme.colors.baseShade4,
                                thickness = 1.dp
                            )
                        }
                    }
                    
                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

object TimezoneFormatter {
    fun format(timezone: TimeZone): String {
        val offsetMillis = timezone.rawOffset
        val totalSeconds = offsetMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = Math.abs((totalSeconds % 3600) / 60)
        
        val sign = if (hours >= 0) "+" else ""
        val offset = if (minutes > 0) {
            String.format("%s%02d:%02d", sign, hours, minutes)
        } else {
            String.format("%s%02d:00", sign, hours)
        }
        
        // Get a readable name for the timezone
        val name = timezone.getDisplayName(false, TimeZone.LONG, Locale.getDefault())
        
        // Get city from identifier (e.g., "Asia/Shanghai" -> "Shanghai")
        val city = timezone.id.split("/").lastOrNull() ?: timezone.id
        
        return "(GMT $offset) $name - $city"
    }
}
