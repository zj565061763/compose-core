package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
        val refBox = createRef()
        val refHLine = fHorizontalLine(bias = 0.5f)
        val refVLine = fVerticalLine(bias = 0.5f)

        Box(
            modifier = Modifier
                .constrainAs(refBox) {
                    width = Dimension.value(50.dp)
                    height = Dimension.value(50.dp)
                    centerHorizontallyTo(refVLine)
                    centerVerticallyTo(refHLine)
                }
                .background(Color.Red)
        )
    }
}