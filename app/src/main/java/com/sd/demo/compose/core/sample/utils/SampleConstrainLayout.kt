package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.fHorizontalLine
import com.sd.lib.compose.core.utils.fVerticalLine

class SampleConstrainLayout : ComponentActivity() {
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
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        fHorizontalLine(bias = 0.5f)
        fVerticalLine(bias = 0.5f)
    }
}

@Preview
@Composable
private fun PreviewContentView() {
    Content()
}