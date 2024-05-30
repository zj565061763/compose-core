package com.sd.lib.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.CoroutineContext

/**
 * 把[getValue]的返回值转为[State]
 */
@Composable
fun <T> fProduceState(
    getValue: () -> T,
    vararg keys: Any?,
    producer: suspend FProduceStateScope<T>.() -> Unit,
): State<T> {
    val getValueUpdated by rememberUpdatedState(getValue)
    val result = remember { mutableStateOf(getValue()) }
    LaunchedEffect(*keys) {
        object : ProduceStateScopeImpl<T>(result, coroutineContext) {
            override fun updateValue(): T {
                return getValueUpdated().also {
                    this.value = it
                }
            }
        }.producer()
    }
    return result
}

interface FProduceStateScope<T> : ProduceStateScope<T> {
    /**
     * 更新[value]
     */
    fun updateValue(): T
}

abstract class ProduceStateScopeImpl<T>(
    state: MutableState<T>,
    override val coroutineContext: CoroutineContext,
) : FProduceStateScope<T>, MutableState<T> by state {

    override suspend fun awaitDispose(onDispose: () -> Unit): Nothing {
        try {
            suspendCancellableCoroutine<Nothing> { }
        } finally {
            onDispose()
        }
    }
}