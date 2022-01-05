package com.amity.socialcloud.uikit.logging

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class AmityProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        return if (this.context != null) {
            val enabled = this.context?.resources?.getBoolean(R.bool.enableLogging) ?: false
            if (enabled) {
                LogHelper.setupTimberLogging(this.context!!)
            }
            true
        } else {
            false
        }

    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}