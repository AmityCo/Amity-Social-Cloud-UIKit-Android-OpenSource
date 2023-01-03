package com.amity.socialcloud.uikit.markup

import android.text.Spanned
import com.amity.socialcloud.sdk.social.feed.AmityPost

interface PostMarkupProcessor {
  fun toSpannedText(post: AmityPost): Spanned
}
