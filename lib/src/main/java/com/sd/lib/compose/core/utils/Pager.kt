package com.sd.lib.compose.core.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.sd.lib.compose.core.fAwait
import kotlinx.coroutines.delay

/**
 * 滚动到指定[page]
 */
@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerState.fScrollToPage(
    page: Int,
) {
    val state = this
    LaunchedEffect(state, page) {
        if (state.targetPage != page) {
            state.scrollToPage(page)
        }
    }
}

/**
 * 动画滚动到指定[page]
 */
@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerState.fAnimateScrollToPage(
    page: Int,
) {
    val state = this
    LaunchedEffect(state, page) {
        if (state.targetPage != page) {
            state.animateScrollToPage(page)
        }
    }
}

/**
 * 监听[PagerState.currentPage]
 */
@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerState.fCurrentPage(
    onChange: (Int) -> Unit,
) {
    val state = this
    val onChangeUpdated by rememberUpdatedState(onChange)
    LaunchedEffect(state) {
        snapshotFlow { state.currentPage }.collect {
            onChangeUpdated(it)
        }
    }
}

/**
 * 监听[PagerState.settledPage]
 */
@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerState.fSettledPage(
    onChange: (Int) -> Unit,
) {
    val state = this
    val onChangeUpdated by rememberUpdatedState(onChange)
    LaunchedEffect(state) {
        snapshotFlow { state.settledPage }.collect {
            onChangeUpdated(it)
        }
    }
}

/**
 * 轮播
 */
@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerState.fLoopPlay(
    delay: suspend () -> Unit = { delay(3_000) },
) {
    val state = this

    val reachPlayCount by remember { derivedStateOf { state.pageCount > 1 } }
    if (!reachPlayCount) {
        // 未达到轮播数量
        return
    }

    val isDragged by state.interactionSource.collectIsDraggedAsState()
    if (isDragged) {
        // 正在拖动中
        return
    }

    val delayUpdated by rememberUpdatedState(delay)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(state, lifecycleOwner) {
        while (true) {
            val pageCount = state.pageCount
            if (pageCount <= 1) break

            delayUpdated()
            lifecycleOwner.fAwait()

            val nextPage = (state.currentPage + 1).takeIf { it < pageCount } ?: 0
            state.animateScrollToPage(nextPage)
        }
    }
}