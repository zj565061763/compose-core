package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.fAnnotatedTarget

/**
 * [fAnnotatedTarget]
 */
class SampleAnnotatedTarget : ComponentActivity() {

    private val _text = "123456789 123456789 123456789 123456789"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content(
                    text = _text
                )
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    text: String,
) {
    var annotated by remember { mutableStateOf<AnnotatedString?>(null) }

    LaunchedEffect(text) {
        annotated = text.fAnnotatedTarget(target = "456") { target ->
            withStyle(SpanStyle(color = Color.Red)) {
                append(target)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        annotated?.let {
            Text(text = it)
        }
    }
}