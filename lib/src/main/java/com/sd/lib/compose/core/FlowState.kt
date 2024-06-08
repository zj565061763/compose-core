package com.sd.lib.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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
    getFlow: () -> F,
): State<T> {
    val inspectionMode = LocalInspectionMode.current
    if (inspectionMode) {
        remember { mutableStateOf(inspectionValue) }
    }

    val initialValue = remember { getInitialValue() }
    val flow = remember { getFlow() }
    val lifecycle = lifecycleOwner.lifecycle

    return produceState(initialValue, flow, lifecycle, minActiveState, context) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                flow.collect { this@produceState.value = it }
            } else withContext(context) {
                flow.collect { this@produceState.value = it }
            }
        }
    }
}