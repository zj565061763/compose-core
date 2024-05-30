package com.sd.lib.compose.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal object AppLifecycle {
    private val _startedFlow = MutableStateFlow(fAppIsStarted)

    val startedFlow: StateFlow<Boolean>
        get() = _startedFlow.asStateFlow()

    init {
        fAppLifecycle.addObserver(
            LifecycleEventObserver { _, _ ->
                _startedFlow.value = fAppIsStarted
            }
        )
    }
}

/** App生命周期 */
val fAppLifecycle: Lifecycle
    get() = ProcessLifecycleOwner.get().lifecycle

/** App生命周期绑定的[CoroutineScope] */
val fAppLifecycleScope: CoroutineScope
    get() = ProcessLifecycleOwner.get().lifecycleScope

/** App生命周期是否至少处于[Lifecycle.State.STARTED]状态 */
val fAppIsStarted: Boolean
    get() = fAppLifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

/** 监听App生命周期是否至少处于[Lifecycle.State.STARTED]状态 */
val fAppIsStartedFlow: StateFlow<Boolean>
    get() = AppLifecycle.startedFlow

/**
 * 等待状态大于等于[state]
 */
suspend fun LifecycleOwner.fAwait(
    state: Lifecycle.State = Lifecycle.State.STARTED,
) {
    lifecycle.fAwait(state = state)
}

/**
 * 等待状态大于等于[state]
 */
suspend fun Lifecycle.fAwait(
    state: Lifecycle.State = Lifecycle.State.STARTED,
) {
    if (currentState == Lifecycle.State.DESTROYED) return
    if (currentState.isAtLeast(state)) return
    suspendCancellableCoroutine { continuation ->
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (currentState.isAtLeast(state)) {
                    removeObserver(this)
                    continuation.resume(Unit)
                }
            }
        }
        addObserver(observer)
        continuation.invokeOnCancellation { removeObserver(observer) }
    }
}