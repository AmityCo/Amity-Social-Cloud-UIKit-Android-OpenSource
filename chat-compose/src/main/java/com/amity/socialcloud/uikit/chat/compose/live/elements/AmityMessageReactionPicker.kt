package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.localization.LocalAmityCommonStringProvider
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReaction
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReactionItem
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReactionPopoverContext
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReactionPopoverItem
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReactionVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope

@Composable
fun AmityMessageReactionPicker(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    message: AmityMessage,
    show: Boolean,
    isCurrentUser: Boolean = false,
    onAddReaction: (AmityMessage, String) -> Unit,
    onRemoveReaction: (AmityMessage, String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (show) {
        AmityBaseElement(
            pageScope = pageScope,
            componentScope = componentScope,
            elementId = "message_reaction"
        ) {
            Popup(
                alignment = if (isCurrentUser) Alignment.BottomEnd else Alignment.BottomStart,
                onDismissRequest = onDismiss,
            ) {
                AmityMessageReactionPickerContent(
                    message = message,
                    onAddReaction = onAddReaction,
                    onRemoveReaction = onRemoveReaction,
                    onDismiss = onDismiss,
                )
            }
        }
    }
}

@Composable
fun AmityMessageReactionPickerContent(
    modifier: Modifier = Modifier,
    message: AmityMessage,
    onAddReaction: (AmityMessage, String) -> Unit,
    onRemoveReaction: (AmityMessage, String) -> Unit,
    onDismiss: () -> Unit,
) {
    val myReaction = message.getMyReactions().firstOrNull()
    val reactions = remember { AmityMessageReactions.getList() }

    val items = reactions.map { reaction ->
        AmityReactionPopoverItem(
            reaction = AmityReactionItem(name = reaction.name, icon = reaction.icon),
            active = myReaction == reaction.name,
            displayName = amityChatReactionDisplayName(reaction.name),
        )
    }

    // Extra top padding gives the tooltip room to animate upward without being clipped.
    Box(modifier = modifier.padding(top = 40.dp)) {
        AmityReaction(
            variant = AmityReactionVariant.POPOVER,
            popoverContext = AmityReactionPopoverContext.LIVE_STREAM_CHAT,
            items = items,
            onSelect = { reaction ->
                if (myReaction == reaction.name) {
                    onRemoveReaction(message, reaction.name)
                } else {
                    onAddReaction(message, reaction.name)
                }
            },
            onDismiss = onDismiss,
        )
    }
}

@Composable
fun amityChatReactionDisplayName(reactionKey: String): String {
    val stringKey = when(reactionKey) {
        "like" -> "chat.reaction.label.like"
        "love" -> "chat.reaction.label.love"
        "fire" -> "chat.reaction.label.fire"
        "happy" -> "chat.reaction.label.happy"
        "sad" -> "chat.reaction.label.sad"
        else -> "chat.reaction.label.${reactionKey}"
    }
    val resolved = LocalAmityCommonStringProvider.current.getString(stringKey)
    // If getString returns the raw key, it means no translation was found — fall back to title-case
    return if (resolved == stringKey) {
        reactionKey.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    } else {
        resolved
    }
}
