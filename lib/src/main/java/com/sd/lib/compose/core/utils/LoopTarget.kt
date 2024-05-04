package com.sd.lib.compose.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.sd.lib.compose.core.fAwait
import kotlinx.coroutines.delay

/**
 * 循环返回[list]的项
 */
@Composable
fun <T> fLoopTarget(
    list: List<T>,
    getDelay: () -> Long = { 3_000 },
): State<T?> {
    return remember { mutableStateOf(list.firstOrNull()) }.also { state ->
        if (list.isEmpty()) {
            state.value = null
        } else {
            val listUpdated by rememberUpdatedState(list)
            val getDelayUpdated by rememberUpdatedState(getDelay)
            val lifecycle = LocalLifecycleOwner.current.lifecycle
            LaunchedEffect(lifecycle) {
                var index = 0
                while (true) {
                    delay(getDelayUpdated())
                    lifecycle.fAwait()
                    index = (index + 1).takeIf { it <= listUpdated.lastIndex } ?: 0
                    state.value = listUpdated.getOrNull(index)
                }
            }
        }
    }
}