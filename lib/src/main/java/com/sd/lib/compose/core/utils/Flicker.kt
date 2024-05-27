package com.sd.lib.compose.core.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * 闪烁
 */
fun Modifier.fFlicker(
    /** 是否闪烁 */
    flicker: Boolean,
    /** 是否可用 */
    enabled: Boolean = true,
    /** 重复次数 */
    repeatCount: Int = 2,
    /** 初始透明度 */
    initialAlpha: Float = 1f,
    /** 开始回调 */
    onStart: suspend (Animatable<Float, AnimationVector1D>) -> Unit = {
        it.snapTo(1f)
    },
    /** 重复回调 */
    onRepeat: suspend (Animatable<Float, AnimationVector1D>) -> Unit = {
        it.animateTo(0f)
        it.animateTo(1f)
    },
    /** 结束回调 */
    onFinish: suspend (Animatable<Float, AnimationVector1D>) -> Unit = {},
): Modifier = if (enabled) {
    composed {
        val animatable = remember(initialAlpha) { Animatable(initialAlpha) }
        animatable.fRepeat(
            anim = flicker,
            repeatCount = repeatCount,
            onStart = { onStart(it) },
            onRepeat = { onRepeat(it) },
            onFinish = { onFinish(it) }
        )

        graphicsLayer {
            this.alpha = animatable.value
        }
    }
} else {
    this
}