package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sd.demo.compose.core.logMsg
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.fFlicker

class SampleFlicker : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    /** 是否闪烁 */
    var flicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fFlicker(
                    flicker = flicker,
                    onFlickerFinish = {
                        logMsg { "onFlickerFinish" }
                        flicker = false
                    },
                )
                .size(100.dp)
                .background(Color.Red)
        )

        Button(
            onClick = {
                logMsg { "click flicker" }
                flicker = true
            }
        ) {
            Text(text = "开始闪烁")
        }
    }
}