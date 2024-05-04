package com.sd.lib.compose.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

/**
 * 循环返回[list]的项
 */
@Composable
fun <T> fLoopTarget(
    list: List<T>,
    getDelay: () -> Long = { 3_000 },
): T? {

    val getDelayUpdated by rememberUpdatedState(getDelay)

    if (list.isEmpty()) {
        return null
    } else {
        var current by remember { mutableStateOf(list.first()) }
        LaunchedEffect(list) {
            var index = 0
            while (true) {
                delay(getDelayUpdated())
                index = (index + 1).let { nextIndex ->
                    if (nextIndex > list.lastIndex) 0 else nextIndex
                }
                current = list[index]
            }
        }
        return current
    }
}