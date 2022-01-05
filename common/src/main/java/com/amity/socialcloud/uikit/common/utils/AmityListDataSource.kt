package com.amity.socialcloud.uikit.common.utils

import android.os.Handler
import android.os.Looper
import androidx.paging.PositionalDataSource
import java.util.concurrent.Executor

class AmityListDataSource<T : Any>(private val items: List<T>) : PositionalDataSource<T>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        val start = params.startPosition
        val end = if (params.startPosition + params.loadSize > items.size)
            items.size
        else
            params.startPosition + params.loadSize

        callback.onResult(items.subList(start, end))
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        callback.onResult(items, 0, items.size)
    }

    class AmityUiThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }
}