package com.amity.socialcloud.uikit.common.ui.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

/**
 * Optional embedded title/description row for [AmitySheet]'s drag-handle strip (Figma
 * `Header#3573:0`). Every consumer sampled so far renders the sheet with this hidden — pass
 * `null` (the [AmitySheet.header] default) unless a site's own design shows a title/description
 * bar directly beneath the drag pill.
 *
 * Models only the plain Title/Description variant (the Sheet atom spec § Sheet Header). The
 * profile-rich Displayname/status-glyph/Badge variant and the L/R action slots are documented on
 * the atom but have no adopting consumer yet — not implemented here, left for when one appears.
 *
 * @param title the centered header title. Binds `Text/Sheets/Header/Title/Default`.
 * @param description optional centered text below [title]. Binds
 * `Text/Sheets/Header/TextDescription/Default`; omitted entirely when null.
 */
data class AmitySheetHeaderContent(
    val title: String,
    val description: String? = null,
)

/**
 * AmitySheet — the atomic modal bottom-sheet / action-sheet container: a rounded-top-corner
 * surface (r20 on the top corners only, square at the bottom) built from a drag-handle strip
 * around a host-supplied [content] slot, with an optional embedded title/description [header] row
 * directly beneath the drag pill.
 *
 * A composite atom (the Sheet atom spec § Composition): it owns only the handle-strip surface, the
 * grab pill, and the Header's own text tokens. [content] is a literal host-defined slot — this
 * atom binds no token inside it; a themed wrapper around Material3's `ModalBottomSheet`.
 *
 * @param onDismissRequest called when the sheet is dismissed (swipe-down, scrim tap, back gesture).
 * @param sheetState the underlying Material3 sheet state — owned/created by the caller so each
 * site keeps its own `skipPartiallyExpanded`/show/hide lifecycle.
 * @param header when set, renders the embedded title/description row below the drag pill (Figma
 * `Header#3573:0`). Defaults to hidden — every chat consumer this atom was adopted from renders it
 * off (the Sheet atom spec § API Reference).
 * @param contentWindowInsets window insets applied to the sheet's content area.
 * @param content the sheet's body — a literal, host-defined Figma `Slot#3567:0`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmitySheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    header: AmitySheetHeaderContent? = null,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = SheetShape,
        containerColor = AmityTheme.token(AmityColorToken.SurfaceSheetsBackgroundGeneral),
        contentWindowInsets = contentWindowInsets,
        dragHandle = { AmitySheetHandle(header = header) },
        content = content,
    )
}

// ---------------------------------------------------------------------------
// Geometry constants (the Sheet atom spec § Geometry) — explicit, not derived.
// ---------------------------------------------------------------------------

private val SheetCornerRadius = 20.dp
private val SheetShape = RoundedCornerShape(topStart = SheetCornerRadius, topEnd = SheetCornerRadius)

private val HandleVerticalPadding = 12.dp
private val HandlePillWidth = 37.dp
private val HandlePillHeight = 4.dp

private val HeaderHorizontalPadding = 16.dp
private val HeaderVerticalPadding = 12.dp

@Composable
private fun AmitySheetHandle(header: AmitySheetHeaderContent?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = HandleVerticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(width = HandlePillWidth, height = HandlePillHeight)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(AmityTheme.token(AmityColorToken.SurfaceSheetsHandleDefault)),
            )
        }

        if (header != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HeaderHorizontalPadding, vertical = HeaderVerticalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = header.title,
                    style = AmityTheme.typography.titleLegacy.copy(
                        lineHeight = 24.sp,
                        color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                    ),
                )
                if (header.description != null) {
                    Text(
                        text = header.description,
                        // Regular 13/18 — captionLegacy is SemiBold by default, so only the weight
                        // needs overriding (same convention as AmityListItem/AmityBanner's own
                        // description rows).
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTextDescriptionDefault),
                        ),
                    )
                }
            }
            AmityDivider(variant = AmityDividerVariant.Post)
        }
    }
}
