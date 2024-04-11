package com.amity.socialcloud.uikit.common.ui.elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataGetter
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityAnnotatedText(
	modifier: Modifier = Modifier,
	text: String,
	mentionGetter: AmityMentionMetadataGetter,
	mentionees: List<AmityMentionee>,
	style: TextStyle = AmityTheme.typography.body,
) {
	val annotatedText = buildAnnotatedString {
		append(text)
		mentionGetter.getMentionedUsers().forEach { mentionItem ->
			if (mentionees.any { (it as? AmityMentionee.USER)?.getUserId() == mentionItem.getUserId() }
				&& mentionItem.getIndex() < text.length
			) {
				addStyle(
					style = SpanStyle(AmityTheme.colors.primary),
					start = mentionItem.getIndex(),
					end = mentionItem.getIndex().plus(mentionItem.getLength()).inc(),
				)
			}
		}
		mentionGetter.getMentionedChannels().forEach { mentionItem ->
			addStyle(
				style = SpanStyle(AmityTheme.colors.primary),
				start = mentionItem.getIndex(),
				end = mentionItem.getIndex().plus(mentionItem.getLength()).inc(),
			)
		}
	}
	Text(
		text = annotatedText,
		style = style,
		modifier = modifier,
	)
}