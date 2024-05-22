package com.sd.lib.compose.core.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope

/**
 * 横向参考线，距离目标[target]的top百分比[bias]，如果[target]为null默认为parent
 */
@Composable
fun ConstraintLayoutScope.fHorizontalLine(
    bias: Float,
    target: ConstrainedLayoutReference? = null,
): ConstrainedLayoutReference {
    return createRef().also { line ->
        Spacer(modifier = Modifier.constrainAs(line) {
            val finalTarget = target ?: parent
            linkTo(
                top = finalTarget.top,
                bottom = finalTarget.bottom,
                bias = bias,
            )
        })
    }
}

/**
 * 竖向参考线，距离目标[target]的start百分比[bias]，如果[target]为null默认为parent
 */
@Composable
fun ConstraintLayoutScope.fVerticalLine(
    bias: Float,
    target: ConstrainedLayoutReference? = null,
): ConstrainedLayoutReference {
    return createRef().also { line ->
        Spacer(modifier = Modifier.constrainAs(line) {
            val finalTarget = target ?: parent
            linkTo(
                start = finalTarget.start,
                end = finalTarget.end,
                bias = bias,
            )
        })
    }
}