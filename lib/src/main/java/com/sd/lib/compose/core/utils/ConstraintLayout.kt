package com.sd.lib.compose.core.utils

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension

/**
 * [ConstrainScope.linkTo]
 */
fun ConstrainScope.fVerticallyTo(
    target: ConstrainedLayoutReference,
    topMargin: Dp = 0.dp,
    bottomMargin: Dp = 0.dp,
    topGoneMargin: Dp = 0.dp,
    bottomGoneMargin: Dp = 0.dp,
    @FloatRange(from = 0.0, to = 1.0) bias: Float = 0.5f,
) {
    linkTo(
        top = target.top,
        bottom = target.bottom,
        topMargin = topMargin,
        bottomMargin = bottomMargin,
        topGoneMargin = topGoneMargin,
        bottomGoneMargin = bottomGoneMargin,
        bias = bias,
    )
}

/**
 * [ConstrainScope.linkTo]
 */
fun ConstrainScope.fHorizontallyTo(
    target: ConstrainedLayoutReference,
    startMargin: Dp = 0.dp,
    endMargin: Dp = 0.dp,
    startGoneMargin: Dp = 0.dp,
    endGoneMargin: Dp = 0.dp,
    @FloatRange(from = 0.0, to = 1.0) bias: Float = 0.5f,
) {
    linkTo(
        start = target.start,
        end = target.end,
        startMargin = startMargin,
        endMargin = endMargin,
        startGoneMargin = startGoneMargin,
        endGoneMargin = endGoneMargin,
        bias = bias,
    )
}

/**
 * 横向参考线，距离目标[target]的top百分比[bias]，如果[target]为null默认为parent
 */
@Composable
fun ConstraintLayoutScope.fHorizontalLine(
    bias: Float,
    target: ConstrainedLayoutReference?,
    color: Color = Color.Red,
): ConstrainedLayoutReference {
    return createRef().also { line ->
        val inspectionMode = LocalInspectionMode.current
        Box(
            modifier = Modifier
                .constrainAs(line) {
                    val finalTarget = target ?: parent
                    linkTo(
                        top = finalTarget.top,
                        bottom = finalTarget.bottom,
                        bias = bias,
                    )
                    if (inspectionMode) {
                        height = Dimension.value(1.dp)
                        linkTo(
                            start = finalTarget.start,
                            end = finalTarget.end,
                        )
                        width = Dimension.fillToConstraints
                    }
                }
                .let {
                    if (inspectionMode) {
                        it.background(color)
                    } else {
                        it
                    }
                }
        )
    }
}

/**
 * 竖向参考线，距离目标[target]的start百分比[bias]，如果[target]为null默认为parent
 */
@Composable
fun ConstraintLayoutScope.fVerticalLine(
    bias: Float,
    target: ConstrainedLayoutReference?,
    color: Color = Color.Red,
): ConstrainedLayoutReference {
    return createRef().also { line ->
        val inspectionMode = LocalInspectionMode.current
        Box(
            modifier = Modifier
                .constrainAs(line) {
                    val finalTarget = target ?: parent
                    linkTo(
                        start = finalTarget.start,
                        end = finalTarget.end,
                        bias = bias,
                    )
                    if (inspectionMode) {
                        width = Dimension.value(1.dp)
                        linkTo(
                            top = finalTarget.top,
                            bottom = finalTarget.bottom,
                        )
                        height = Dimension.fillToConstraints
                    }
                }
                .let {
                    if (inspectionMode) {
                        it.background(color)
                    } else {
                        it
                    }
                }
        )
    }
}