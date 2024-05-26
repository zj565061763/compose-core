package com.sd.lib.compose.core.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
    /** 开始透明度 */
    startAlpha: Float = 1f,
    /** 结束透明度 */
    endAlpha: Float = 0f,
    /** 闪烁重复次数 */
    repeatCount: Int = 2,
    /** 执行动画到开始透明度 */
    animateToStartAlpha: suspend (Animatable<Float, AnimationVector1D>, Float) -> Unit = { animatable, alpha ->
        animatable.animateTo(alpha)
    },
    /** 执行动画到结束透明度 */
    animateToEndAlpha: suspend (Animatable<Float, AnimationVector1D>, Float) -> Unit = { animatable, alpha ->
        animatable.animateTo(alpha)
    },
    /** 闪烁结束回调 */
    onFlickerFinish: () -> Unit,
): Modifier = if (enabled) {
    composed {
        require(repeatCount > 0)

        val startAlphaUpdated by rememberUpdatedState(startAlpha)
        val endAlphaUpdated by rememberUpdatedState(endAlpha)
        val repeatCountUpdated by rememberUpdatedState(repeatCount)
        val animateToStartAlphaUpdated by rememberUpdatedState(animateToStartAlpha)
        val animateToEndAlphaUpdated by rememberUpdatedState(animateToEndAlpha)

        val animatable = remember { Animatable(1f) }
        val onFlickerFinishUpdated by rememberUpdatedState(onFlickerFinish)

        if (flicker) {
            LaunchedEffect(animatable) {
                animatable.snapTo(startAlphaUpdated)
                if (repeatCountUpdated == Int.MAX_VALUE) {
                    while (true) {
                        animateToEndAlphaUpdated(animatable, endAlphaUpdated)
                        animateToStartAlphaUpdated(animatable, startAlphaUpdated)
                    }
                } else {
                    repeat(repeatCountUpdated) {
                        animateToEndAlphaUpdated(animatable, endAlphaUpdated)
                        animateToStartAlphaUpdated(animatable, startAlphaUpdated)
                    }
                    onFlickerFinishUpdated()
                }
            }
        }

        graphicsLayer {
            this.alpha = animatable.value
        }
    }
} else {
    this
}