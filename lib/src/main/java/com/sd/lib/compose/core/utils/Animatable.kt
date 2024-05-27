package com.sd.lib.compose.core.utils

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

/**
 * 动画
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T, V : AnimationVector> Animatable<T, V>.fRepeat(
    /** 是否执行动画 */
    anim: Boolean,
    /** 是否可用 */
    enabled: Boolean = true,
    /** 闪烁重复次数 */
    repeatCount: Int,
    /** 开始回调 */
    onStart: suspend (Animatable<T, V>) -> Unit,
    /** 重复回调 */
    onRepeat: suspend (Animatable<T, V>) -> Unit,
    /** 结束回调 */
    onFinish: suspend (Animatable<T, V>) -> Unit,
) {
    require(repeatCount > 0)

    val repeatCountUpdated by rememberUpdatedState(repeatCount)
    val onStartUpdated by rememberUpdatedState(onStart)
    val onRepeatUpdated by rememberUpdatedState(onRepeat)
    val onFinishUpdated by rememberUpdatedState(onFinish)

    val animatable = this

    if (anim && enabled) {
        LaunchedEffect(animatable) {
            onStartUpdated(animatable)
            if (repeatCountUpdated == Int.MAX_VALUE) {
                while (true) {
                    onRepeatUpdated(animatable)
                }
            } else {
                repeat(repeatCountUpdated) {
                    onRepeatUpdated(animatable)
                }
            }
            onFinishUpdated(animatable)
        }
    }
}