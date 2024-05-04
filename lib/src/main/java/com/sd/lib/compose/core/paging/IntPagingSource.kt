package com.sd.lib.compose.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CancellationException

/**
 * Key为Int的数据源
 */
abstract class IntPagingSource<T : Any> : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return runCatching {
            loadImpl(params)
        }.getOrElse { e ->
            if (e is CancellationException) throw e
            LoadResult.Error(e)
        }
    }

    abstract suspend fun loadImpl(params: LoadParams<Int>): LoadResult<Int, T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}