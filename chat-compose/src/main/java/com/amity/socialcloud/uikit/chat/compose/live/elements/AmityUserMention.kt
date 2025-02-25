package com.amity.socialcloud.uikit.chat.compose.live.elements

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.mentions.Mentionable.MentionDisplayMode
import java.util.UUID

class AmityUserMention private constructor() : Mentionable {

    private var user: AmityUser? = null

    constructor(user: AmityUser) : this() {
        this.user = user
    }

    internal constructor(parcel: Parcel) : this() {
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcel.readParcelable(AmityUser::class.java.classLoader, AmityUser::class.java)
        } else {
            parcel.readParcelable(AmityUser::class.java.classLoader)
        }
    }

    companion object CREATOR : Parcelable.Creator<AmityUserMention> {
        const val CHAR_MENTION = "@"

        override fun createFromParcel(parcel: Parcel): AmityUserMention {
            return AmityUserMention(parcel)
        }

        override fun newArray(size: Int): Array<AmityUserMention?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(user, 0)
    }

    override fun getSuggestibleId(): Int {
        return user.hashCode()
    }

    override fun getSuggestiblePrimaryText(): String {
        return getDisplayName() + UUID.randomUUID()
    }

    override fun getTextForDisplayMode(mode: MentionDisplayMode): String {
        return user?.let {
            "$CHAR_MENTION${it.getDisplayName()}"
        } ?: kotlin.run {
            "$CHAR_MENTION${user?.getUserId()}"
        }
    }

    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle {
        return Mentionable.MentionDeleteStyle.PARTIAL_NAME_DELETE
    }

    fun getUserId(): String {
        return user?.getUserId() ?: ""
    }

    fun getDisplayName(): String {
        return user?.getDisplayName() ?: ""
    }

    fun getAvatar(): AmityImage? {
        return user?.getAvatar()
    }
}