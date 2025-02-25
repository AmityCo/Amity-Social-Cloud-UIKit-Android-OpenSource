package com.amity.socialcloud.uikit.chat.compose.live.elements

import android.text.Spanned
import android.text.TextUtils
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig

class AmityWordTokenizer(val mConfig: WordTokenizerConfig) : WordTokenizer(mConfig) {
    override fun isValidMention(text: Spanned, start: Int, end: Int): Boolean {
        // Get the token
        val token = text.subSequence(start, end)

        // Null or empty string is not a valid mention
        if (TextUtils.isEmpty(token)) {
            return false
        }

        // Handle explicit mentions first, then implicit mentions
        val threshold = mConfig.THRESHOLD
        val multipleWords = containsWordBreakingChar(token)
        val containsExplicitChar = containsExplicitChar(token)
        if (!multipleWords && containsExplicitChar) {

            // If it is one word and has an explicit char, the explicit char must be the first char
            if (!isExplicitChar(token[0])) {
                return false
            }

            // Ensure that the character before the explicit character is a word-breaking character
            // Note: Checks the explicit character closest in front of the cursor
            if (!hasWordBreakingCharBeforeExplicitChar(text, end)) {
                return false
            }

            // Return true if string is just an explicit character
            return if (token.length == 1) {
                true
            } else !isWordBreakingChar(token[1])

            // If input has length greater than one, the second character must be a letter or digit
            // Return true if and only if second character is a letter or digit, i.e. "@d"
        } else if (token.length >= threshold) {

            // Change behavior depending on if keywords is one or more words
            return if (!multipleWords) {
                // One word, no explicit characters
                // input is only one word, i.e. "u41"
                onlyLettersOrDigits(token, threshold, 0)
            } else if (containsExplicitChar) {
                // Multiple words, has explicit character
                // Must have a space, the explicit character, then a letter or digit
                (hasWordBreakingCharBeforeExplicitChar(text, end)
                        && isExplicitChar(token[0])
                        && !isWordBreakingChar(token[1]))
            } else {
                // Multiple words, no explicit character
                // Either the first or last couple of characters must be letters/digits
                val firstCharactersValid = onlyLettersOrDigits(token, threshold, 0)
                val lastCharactersValid =
                    onlyLettersOrDigits(token, threshold, token.length - threshold)
                firstCharactersValid || lastCharactersValid
            }
        }
        return false
    }
}