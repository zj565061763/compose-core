package com.sd.lib.compose.core.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.fFlicker(
    /** 是否闪烁 */
    flicker: Boolean,
    /** 开始透明度 */
    startAlpha: Float = 1f,
    /** 结束透明度 */
    endAlpha: Float = 0f,
    /** 闪烁重复次数 */
    repeatCount: Int = 2,
    /** 是否可用 */
    enabled: Boolean = true,
    /** 闪烁结束回调 */
    onFlickerFinish: () -> Unit,
): Modifier = this.composed {
    require(repeatCount > 0)

    val startAlphaUpdated by rememberUpdatedState(startAlpha)
    val endAlphaUpdated by rememberUpdatedState(endAlpha)
    val repeatCountUpdated by rememberUpdatedState(repeatCount)

    val animatable = remember { Animatable(1f) }
    val onFlickerFinishUpdated by rememberUpdatedState(onFlickerFinish)

    if (enabled && flicker) {
        LaunchedEffect(Unit) {
            animatable.snapTo(startAlphaUpdated)
            if (repeatCountUpdated == Int.MAX_VALUE) {
                while (true) {
                    animatable.animateTo(endAlphaUpdated)
                    animatable.animateTo(startAlphaUpdated)
                }
            } else {
                repeat(repeatCountUpdated) {
                    animatable.animateTo(endAlphaUpdated)
                    animatable.animateTo(startAlphaUpdated)
                }
                onFlickerFinishUpdated()
            }
        }
    }

    if (enabled) {
        graphicsLayer {
            this.alpha = animatable.value
        }
    } else {
        this@fFlicker
    }
}