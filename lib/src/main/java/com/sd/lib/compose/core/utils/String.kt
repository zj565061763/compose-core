package com.sd.lib.compose.core.utils

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

/**
 * 把[items]拼接为字符串，如果[items]为空返回空字符串，[items]内容对应的字符串规则如下：
 * * null -> 空字符串
 * * String -> 字符串本身
 * * Int -> 当作字符串资源ID读取字符串
 * * 其他 -> 调用对象的toString()
 */
@Composable
fun fString(vararg items: Any?): String {
    val resources = resources()
    return when (items.size) {
        0 -> ""
        1 -> items.first().anyToString(resources)
        else -> {
            var result by remember { mutableStateOf(items.itemsToString(resources)) }
            LaunchedEffect(items) {
                result = items.itemsToString(resources)
            }
            result
        }
    }
}

private fun Array<*>.itemsToString(resources: Resources): String {
    return buildString {
        for (item in this@itemsToString) {
            append(item.anyToString(resources))
        }
    }
}

private fun Any?.anyToString(resources: Resources): String {
    return when (this) {
        null -> ""
        is String -> this
        is Int -> resources.getString(this)
        else -> this.toString()
    }
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}