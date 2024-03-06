package com.amity.socialcloud.uikit.community.views.mention

import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig

class MentionViewWordTokenizer {
    fun create(): WordTokenizer {
        return CustomWordTokenizer(Builder.create().build())
    }

    private object Builder {
        fun create(): WordTokenizerConfig.Builder {
            return WordTokenizerConfig.Builder()
                .setWordBreakChars(" \n")
                .setExplicitChars(AmityUserMention.CHAR_MENTION)
                .setMaxNumKeywords(1)
                .setThreshold(1)
        }
    }
}