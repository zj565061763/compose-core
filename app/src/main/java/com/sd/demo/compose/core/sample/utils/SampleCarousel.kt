package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.demo.compose.core.logMsg
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.FCarouselVertical
import com.sd.lib.compose.core.utils.fLoopTarget
import java.util.UUID

/**
 * [FCarouselVertical]，[fLoopTarget]
 */
class SampleCarousel : ComponentActivity() {

    private val _list = MutableList(10) { index ->
        if (index % 2 == 0) {
            index.toString()
        } else {
            UUID.randomUUID().toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content(
                    list = _list,
                )
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    list: List<String>,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        fLoopTarget(list).value?.let { targetValue ->
            Box(
                modifier = modifier.background(
                    color = Color(0xFF114460).copy(0.5f),
                    shape = CircleShape,
                )
            ) {
                FCarouselVertical(targetValue) { target ->
                    Box(
                        modifier = Modifier
                            .height(26.dp)
                            .widthIn(110.dp)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = target,
                            color = Color.White,
                            fontSize = 13.sp,
                            maxLines = 1,
                        )
                    }
                }
            }

            LaunchedEffect(targetValue) {
                logMsg { "target:$targetValue" }
            }
        }
    }
}