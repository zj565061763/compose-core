package com.sd.lib.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * [collectAsStateWithLifecycle]
 */
@Composable
fun <T> Flow<T>.fCollectAsStateWithLifecycle(
    /** 预览模式的值 */
    inspectionValue: T,
    /** 初始值 */
    initialValue: T,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
): State<T> {
    val inspectionMode = LocalInspectionMode.current
    if (inspectionMode) {
        remember { mutableStateOf(inspectionValue) }
    }
    return collectAsStateWithLifecycle(
        initialValue = initialValue,
        lifecycleOwner = lifecycleOwner,
        minActiveState = minActiveState,
        context = context,
    )
}

/**
 * [collectAsStateWithLifecycle]
 */
@Composable
fun <T> StateFlow<T>.fCollectAsStateWithLifecycle(
    /** 预览模式的值 */
    inspectionValue: T,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
): State<T> {
    val inspectionMode = LocalInspectionMode.current
    if (inspectionMode) {
        remember { mutableStateOf(inspectionValue) }
    }
    return collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner,
        minActiveState = minActiveState,
        context = context,
    )
}