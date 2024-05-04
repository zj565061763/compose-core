package com.sd.lib.compose.core.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow

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