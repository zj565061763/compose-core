package com.sd.lib.compose.core.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.sd.lib.compose.core.rememberDrawablePainter

@Composable
fun fDrawablePainter(
    @DrawableRes id: Int,
): Painter {
    val drawable = ContextCompat.getDrawable(LocalContext.current, id)
    return rememberDrawablePainter(drawable = drawable)
}

fun Modifier.fPainter(
    painter: Painter,
    sizeToIntrinsics: Boolean = true,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.FillBounds,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
): Modifier {
    return this.paint(
        painter = painter,
        sizeToIntrinsics = sizeToIntrinsics,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}