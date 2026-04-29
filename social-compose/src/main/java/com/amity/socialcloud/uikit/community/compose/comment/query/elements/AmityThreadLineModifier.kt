package com.amity.socialcloud.uikit.community.compose.comment.query.elements

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * Holds the mutable state needed for thread-line rendering.
 *
 * Create once per L1 comment via `remember { ThreadLineState() }` and share
 * between the avatar modifier, the connector-box modifier, and every child
 * branch-position modifier.
 */
@Stable
class ThreadLineState(
    /** Y positions (relative to connector-box) of each branch target (reply avatar center / load-more center). */
    val branchYPositions: SnapshotStateList<Float> = SnapshotStateList(),
) {
    /** Root-Y of the bottom edge of the L1 avatar. */
    var avatarBottomYRoot: Float = 0f
    /** Root-Y of the top edge of the connector Box. */
    var threadBoxTopYRoot: Float = 0f
}

// ── Individual modifiers ───────────────────────────────────────────────

/**
 * Attach to the L1 avatar composable to track its bottom-Y in root coordinates.
 */
fun Modifier.trackAvatarBottom(state: ThreadLineState): Modifier =
    this.onGloballyPositioned { coords ->
        state.avatarBottomYRoot = coords.positionInRoot().y + coords.size.height
    }

/**
 * Attach to the Box that wraps reply children.
 * Draws the vertical trunk + curved branches behind its content and
 * tracks its own top-Y so the trunk can start from below the L1 avatar.
 */
fun Modifier.threadLineConnector(
    state: ThreadLineState,
    lineColor: Color,
    density: Density,
): Modifier {
    val lineXPx = with(density) { (-24).dp.toPx() }
    val branchEndXPx = with(density) { 8.dp.toPx() }
    val strokeWidthPx = with(density) { 1.dp.toPx() }
    val radiusPx = with(density) { 8.dp.toPx() }

    return this
        .graphicsLayer(clip = false)
        .onGloballyPositioned { coords ->
            state.threadBoxTopYRoot = coords.positionInRoot().y
        }
        .drawBehind {
            val positions = state.branchYPositions
            if (positions.isNotEmpty()) {
                val sorted = positions.sorted()
                val lastY = sorted.last()
                val lineStartY = state.avatarBottomYRoot - state.threadBoxTopYRoot

                // Vertical trunk: from below avatar to where the last curve starts
                drawLine(
                    color = lineColor,
                    start = Offset(lineXPx, lineStartY),
                    end = Offset(lineXPx, lastY - radiusPx),
                    strokeWidth = strokeWidthPx,
                )

                // Curved branch for each reply
                sorted.forEach { y ->
                    val path = Path().apply {
                        moveTo(lineXPx, y - radiusPx)
                        quadraticBezierTo(lineXPx, y, lineXPx + radiusPx, y)
                    }
                    drawPath(path, lineColor, style = Stroke(strokeWidthPx))

                    // Horizontal segment after the curve
                    if (lineXPx + radiusPx < branchEndXPx) {
                        drawLine(
                            color = lineColor,
                            start = Offset(lineXPx + radiusPx, y),
                            end = Offset(branchEndXPx, y),
                            strokeWidth = strokeWidthPx,
                        )
                    }
                }
            }
        }
}

/**
 * Attach to each reply-item Box to report its avatar-center Y
 * (relative to the parent layout) into [state.branchYPositions]
 * at the given [index].
 */
fun Modifier.trackBranchPosition(
    state: ThreadLineState,
    index: Int,
    density: Density,
): Modifier = this.onGloballyPositioned { coordinates ->
    val itemY = coordinates.positionInParent().y
    val avatarCenterY = itemY + with(density) { 20.dp.toPx() }
    val positions = state.branchYPositions
    while (positions.size <= index) {
        positions.add(0f)
    }
    positions[index] = avatarCenterY
}

/**
 * Attach to a single-preview reply Box (collapsed state in AmityReplyCommentContainer)
 * to report its avatar-center Y at index 0.
 */
fun Modifier.trackSingleReplyPosition(
    state: ThreadLineState,
    density: Density,
): Modifier = this.onGloballyPositioned { coordinates ->
    val itemY = coordinates.positionInParent().y
    val avatarCenterY = itemY + with(density) { 20.dp.toPx() }
    val positions = state.branchYPositions
    if (positions.isEmpty()) positions.add(avatarCenterY)
    else positions[0] = avatarCenterY
}

/**
 * Attach to the collapsed "View replies" bar Box to report its vertical centre
 * at a specific [index] in [state.branchYPositions].
 */
fun Modifier.trackCollapsedBarPositionAtIndex(
    state: ThreadLineState,
    index: Int,
): Modifier = this.onGloballyPositioned { coordinates ->
    val itemY = coordinates.positionInParent().y
    val centerY = itemY + coordinates.size.height / 2f
    val positions = state.branchYPositions
    while (positions.size <= index) {
        positions.add(0f)
    }
    positions[index] = centerY
}
