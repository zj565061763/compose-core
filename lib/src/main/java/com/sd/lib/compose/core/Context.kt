package com.sd.lib.compose.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * 优先从[LocalContext]中查找[Activity]返回，如果找不到的话使用当前[LocalContext]
 */
@Composable
fun fPreferActivityContext(): Context {
    val context = LocalContext.current
    return remember(context) { context.fPreferActivityContext() }
}

/**
 * 优先从[Context]中查找[Activity]返回，如果找不到的话使用当前[Context]
 */
private fun Context.fPreferActivityContext(): Context = fFindActivityOrNull() ?: this

/**
 * 从[Context]中查找[Activity]，如果找不到的话返回null
 */
private tailrec fun Context.fFindActivityOrNull(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.fFindActivityOrNull()
        else -> null
    }