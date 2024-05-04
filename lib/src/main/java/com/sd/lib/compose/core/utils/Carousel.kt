package com.sd.lib.compose.core.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds

/**
 * 轮播滚动
 */
@Composable
fun <T> FCarouselVertical(
    /** 目标 */
    target: T,
    modifier: Modifier = Modifier,
    /** 切换动画时长 */
    duration: Int = 1000,
    /** 内容 */
    content: @Composable (target: T) -> Unit,
) {
    AnimatedContent(
        modifier = modifier.clipToBounds(),
        targetState = target,
        label = "FCarouselVertical",
        contentAlignment = Alignment.Center,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(duration),
                ),
                initialContentExit = slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(duration),
                ),
            )
        },
    ) { targetState ->
        content(targetState)
    }
}