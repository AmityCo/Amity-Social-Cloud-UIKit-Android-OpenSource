package com.amity.socialcloud.uikit.community.compose.notificationtray

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.notificationtray.AmityNotificationTrayItem
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class NotificationTrayViewModel : AmityBaseViewModel() {

    fun getNotificationTrayItem(): Flow<PagingData<NotificationTrayListItem>> {
        return AmityCoreClient
            .notificationTray()
            .getNotificationTrayItems()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .asFlow()
            .catch {
            }
            .map { pagingData ->
                pagingData.map { NotificationTrayListItem.NotificationItem(it) }
                    .insertSeparators { before, after ->
                        // When beginning of list, add header based on first item
                        if (before == null && after is NotificationTrayListItem.NotificationItem) {
                            if (after.item.isRecent() == true) {
                                return@insertSeparators NotificationTrayListItem.Header("Recent")
                            } else {
                                return@insertSeparators NotificationTrayListItem.Header("Older")
                            }
                        }
                        // Insert header if there's a switch from recent to older items
                        if (before is NotificationTrayListItem.NotificationItem &&
                            after is NotificationTrayListItem.NotificationItem &&
                            before.item.isRecent() != after.item.isRecent()
                        ) {
                            if (after.item.isRecent() == true) {
                                return@insertSeparators NotificationTrayListItem.Header("Recent")
                            } else {
                                return@insertSeparators NotificationTrayListItem.Header("Older")
                            }
                        }
                        null
                    }
            }
    }

    fun markNotificationItemAsSeen(notiTray: AmityNotificationTrayItem) {
        addDisposable(
            notiTray.markSeen()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    sealed class NotificationTrayListItem {
        data class Header(val title: String) : NotificationTrayListItem()
        data class NotificationItem(val item: AmityNotificationTrayItem) :
            NotificationTrayListItem()
    }

}