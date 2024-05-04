package com.sd.lib.compose.core.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * 是否刷新中
 */
fun LazyPagingItems<*>.fIsRefreshing(): Boolean {
    return loadState.refresh == LoadState.Loading
}

/**
 * 是否加载更多中
 */
fun LazyPagingItems<*>.fIsAppending(): Boolean {
    return loadState.append == LoadState.Loading
}

/**
 * 加载更多错误
 */
fun LazyPagingItems<*>.fAppendError(): Throwable? {
    val loadState = loadState.append
    return if (loadState is LoadState.Error) loadState.error else null
}

/**
 * 是否显示没有更多数据
 */
@Composable
fun LazyPagingItems<*>.fAppendShowNoMoreData(): Boolean {
    return !fIsEmpty() && loadState.append.endOfPaginationReached
}

/**
 * 数据是否为空
 */
@Composable
fun LazyPagingItems<*>.fIsEmpty(): Boolean {
    val items = this
    return remember(items) {
        derivedStateOf { items.itemCount == 0 }
    }.value
}

/**
 * 加载状态
 */
@Composable
fun LazyPagingItems<*>.FStateLoading(
    content: @Composable () -> Unit,
) {
    if (fIsEmpty()) {
        if (loadState.refresh == LoadState.Loading) {
            content()
        }
    }
}

/**
 * 无数据状态
 */
@Composable
fun LazyPagingItems<*>.FStateNoData(
    content: @Composable () -> Unit,
) {
    if (fIsEmpty()) {
        if (loadState.refresh is LoadState.NotLoading) {
            content()
        }
    }
}

/**
 * 加载错误状态
 */
@Composable
fun LazyPagingItems<*>.FStateLoadError(
    content: @Composable (Throwable) -> Unit,
) {
    if (fIsEmpty()) {
        val refresh = loadState.refresh
        if (refresh is LoadState.Error) {
            content(refresh.error)
        }
    }
}