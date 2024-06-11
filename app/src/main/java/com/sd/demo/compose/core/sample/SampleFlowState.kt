package com.sd.demo.compose.core.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.fFlowStateWithLifecycle
import com.sd.lib.compose.core.fStateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class SampleFlowState : ComponentActivity() {
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
    val countdown by fFlowStateWithLifecycle(999) { scope ->
        getCountDownFlow(scope)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = countdown.toString())
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content()
}

private fun getCountDownFlow(
    scope: CoroutineScope,
): StateFlow<Int> {
    var seconds = 100
    return flow {
        while (seconds > 0) {
            delay(1_000)
            seconds--
            emit(seconds)
        }
    }.fStateIn(
        scope = scope,
        initialValue = seconds
    )
}