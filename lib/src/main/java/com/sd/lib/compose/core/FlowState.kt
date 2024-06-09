package com.sd.lib.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * [collectAsStateWithLifecycle]
 */
@Composable
fun <T, F : StateFlow<T>> fFlowStateWithLifecycle(
    /** 预览模式的值 */
    inspectionValue: T,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    /** 要监听的[Flow] */
    getFlow: (CoroutineScope) -> F,
): State<T> {
    val inspectionMode = LocalInspectionMode.current
    if (inspectionMode) {
        remember { mutableStateOf(inspectionValue) }
    }
    val coroutineScope = rememberCoroutineScope()
    return remember { getFlow(coroutineScope) }
        .collectAsStateWithLifecycle(
            lifecycleOwner = lifecycleOwner,
            minActiveState = minActiveState,
            context = context,
        )
}

/**
 * [collectAsStateWithLifecycle]
 */
@Composable
fun <T, F : Flow<T>> fFlowStateWithLifecycle(
    /** 预览模式的值 */
    inspectionValue: T,
    /** 初始值 */
    getInitialValue: () -> T,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    /** 要监听的[Flow] */
    getFlow: (CoroutineScope) -> F,
): State<T> {
    val inspectionMode = LocalInspectionMode.current
    if (inspectionMode) {
        remember { mutableStateOf(inspectionValue) }
    }
    val coroutineScope = rememberCoroutineScope()
    return remember { getFlow(coroutineScope) }
        .collectAsStateWithLifecycle(
            initialValue = remember { getInitialValue() },
            lifecycleOwner = lifecycleOwner,
            minActiveState = minActiveState,
            context = context,
        )
}